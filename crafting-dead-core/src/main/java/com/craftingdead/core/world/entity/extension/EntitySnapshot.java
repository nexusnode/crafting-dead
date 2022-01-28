/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.world.entity.extension;

import java.util.Optional;
import java.util.Random;
import com.craftingdead.core.util.RayTraceUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class EntitySnapshot {

  private boolean complete;
  private final AABB boundingBox;
  private Vec3 pos;
  private final Vec2 pitchYaw;
  private final float eyeHeight;

  public EntitySnapshot(AABB boundingBox, Vec2 pitchYaw) {
    this(boundingBox, pitchYaw, -1);
    this.complete = false;
  }

  public EntitySnapshot(Entity entity) {
    this(entity.getBoundingBox(), entity.getRotationVector(), entity.getEyeHeight());
  }

  public EntitySnapshot(AABB boundingBox, Vec2 pitchYaw, float eyeHeight) {
    this.pos = new Vec3((boundingBox.minX + boundingBox.maxX) / 2.0D, boundingBox.minY,
        (boundingBox.minZ + boundingBox.maxZ) / 2.0D);
    this.boundingBox = boundingBox;
    this.pitchYaw = pitchYaw;
    this.eyeHeight = eyeHeight;
    this.complete = true;
  }

  public Optional<Vec3> rayTrace(Level world, EntitySnapshot fromSnapshot, double distance,
      float accuracy, int shotCount, Random random) {
    if (!fromSnapshot.complete || !this.complete) {
      return Optional.empty();
    }

    Vec3 start = fromSnapshot.getPos().add(0.0D, fromSnapshot.eyeHeight, 0.0D);
    Vec3 look = RayTraceUtil.getVectorForRotation(
        fromSnapshot.getPitchYaw().x + RayTraceUtil.getAccuracyOffset(accuracy, shotCount, random),
        fromSnapshot.getPitchYaw().y + RayTraceUtil.getAccuracyOffset(accuracy, shotCount, random));

    Optional<BlockHitResult> blockRayTraceResult =
        RayTraceUtil.rayTraceBlocksPiercing(start, distance, look, world);

    Vec3 scaledLook = look.scale(distance);

    Vec3 end = blockRayTraceResult
        .map(HitResult::getLocation)
        .orElse(start.add(scaledLook));

    Optional<Vec3> potentialHit = this.getCollisionBox().clip(start, end);
    if (this.getCollisionBox().contains(start)) {
      return Optional.of(potentialHit.orElse(start));
    } else {
      return potentialHit;
    }
  }


  public Vec3 getPos() {
    return this.pos;
  }

  public AABB getCollisionBox() {
    return this.boundingBox;
  }

  public Vec2 getPitchYaw() {
    return this.pitchYaw;
  }

  public float getEyeHeight() {
    return this.eyeHeight;
  }

  public EntitySnapshot combineUntrustedSnapshot(EntitySnapshot snapshot) {
    if (!this.complete) {
      throw new UnsupportedOperationException("Snapshot not complete");
    }

    AABB boundingBox = snapshot.boundingBox;
    if (this.pos.distanceTo(snapshot.pos) > 1.0D
        || Math.abs(this.boundingBox.getSize()
            - snapshot.boundingBox.getSize()) > 1.0E-10D) {
      boundingBox = this.boundingBox;
    }

    Vec2 pitchYaw = snapshot.pitchYaw;
    if (Mth.degreesDifferenceAbs(this.pitchYaw.x, snapshot.pitchYaw.x) > 10.0D
        || Mth.degreesDifferenceAbs(this.pitchYaw.y, snapshot.pitchYaw.y) > 10.0D) {
      pitchYaw = this.pitchYaw;
    }

    return new EntitySnapshot(boundingBox, pitchYaw, this.eyeHeight);
  }

  @Override
  public String toString() {
    return "snapshot[pitch = " + this.pitchYaw.x + ", yaw = " + this.pitchYaw.y
        + ", " + this.boundingBox.toString() + "]";
  }

  public void write(FriendlyByteBuf out) {
    out.writeDouble(this.boundingBox.minX);
    out.writeDouble(this.boundingBox.minY);
    out.writeDouble(this.boundingBox.minZ);
    out.writeDouble(this.boundingBox.maxX);
    out.writeDouble(this.boundingBox.maxY);
    out.writeDouble(this.boundingBox.maxZ);
    out.writeFloat(this.pitchYaw.x);
    out.writeFloat(this.pitchYaw.y);
  }

  public static EntitySnapshot read(FriendlyByteBuf in) {
    double minX = in.readDouble();
    double minY = in.readDouble();
    double minZ = in.readDouble();
    double maxX = in.readDouble();
    double maxY = in.readDouble();
    double maxZ = in.readDouble();
    float pitch = in.readFloat();
    float yaw = in.readFloat();
    return new EntitySnapshot(new AABB(minX, minY, minZ, maxX, maxY, maxZ),
        new Vec2(pitch, yaw));
  }
}
