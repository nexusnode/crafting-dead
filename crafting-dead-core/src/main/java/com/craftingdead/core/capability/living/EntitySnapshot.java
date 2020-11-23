/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.capability.living;

import java.util.Optional;
import java.util.Random;
import com.craftingdead.core.util.RayTraceUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntitySnapshot {

  private boolean complete;
  private final AxisAlignedBB boundingBox;
  private Vec3d pos;
  private final Vec2f pitchYaw;
  private final float eyeHeight;

  public EntitySnapshot(AxisAlignedBB boundingBox, Vec2f pitchYaw) {
    this(boundingBox, pitchYaw, -1);
    this.complete = false;
  }

  public EntitySnapshot(Entity entity) {
    this(entity.getBoundingBox(), entity.getPitchYaw(), entity.getEyeHeight());
  }

  public EntitySnapshot(AxisAlignedBB boundingBox, Vec2f pitchYaw, float eyeHeight) {
    this.pos = new Vec3d((boundingBox.minX + boundingBox.maxX) / 2.0D, boundingBox.minY,
        (boundingBox.minZ + boundingBox.maxZ) / 2.0D);
    this.boundingBox = boundingBox;
    this.pitchYaw = pitchYaw;
    this.eyeHeight = eyeHeight;
    this.complete = true;
  }

  public Optional<Vec3d> rayTrace(World world, EntitySnapshot fromSnapshot, double distance,
      float accuracy, Random random) {
    if (!fromSnapshot.complete || !this.complete) {
      return Optional.empty();
    }

    Vec3d start = fromSnapshot.getPos().add(0.0D, fromSnapshot.eyeHeight, 0.0D);
    Vec3d look = fromSnapshot.getVectorForRotation(fromSnapshot.getPitchYaw().x,
        fromSnapshot.getPitchYaw().y);

    look = look.add(
        (1.0F - accuracy) / (random.nextInt(5) + 3) * (random.nextInt(5) % 2 == 0 ? -1.0F : 1.0F),
        0,
        (1.0F - accuracy) / (random.nextInt(5) + 3) * (random.nextInt(5) % 2 == 0 ? -1.0F : 1.0F));

    Optional<BlockRayTraceResult> blockRayTraceResult =
        RayTraceUtil.rayTraceBlocksPiercing(start, distance, look, world);

    Vec3d scaledLook = look.scale(distance);

    Vec3d end = blockRayTraceResult
        .map(RayTraceResult::getHitVec)
        .orElse(start.add(scaledLook));

    Optional<Vec3d> potentialHit = this.getCollisionBox().rayTrace(start, end);
    if (this.getCollisionBox().contains(start)) {
      return Optional.of(potentialHit.orElse(start));
    } else {
      return potentialHit;
    }
  }

  protected final Vec3d getVectorForRotation(float pitch, float yaw) {
    float f = pitch * ((float) Math.PI / 180F);
    float f1 = -yaw * ((float) Math.PI / 180F);
    float f2 = MathHelper.cos(f1);
    float f3 = MathHelper.sin(f1);
    float f4 = MathHelper.cos(f);
    float f5 = MathHelper.sin(f);
    return new Vec3d((double) (f3 * f4), (double) (-f5), (double) (f2 * f4));
  }

  public Vec3d getPos() {
    return this.pos;
  }

  public AxisAlignedBB getCollisionBox() {
    return this.boundingBox;
  }

  public Vec2f getPitchYaw() {
    return this.pitchYaw;
  }

  public float getEyeHeight() {
    return this.eyeHeight;
  }

  public EntitySnapshot combineUntrustedSnapshot(EntitySnapshot snapshot) {
    if (!this.complete) {
      throw new UnsupportedOperationException("Snapshot not complete");
    }

    AxisAlignedBB boundingBox = snapshot.boundingBox;
    if (this.pos.distanceTo(snapshot.pos) > 1.0D
        || Math.abs(this.boundingBox.getAverageEdgeLength()
            - snapshot.boundingBox.getAverageEdgeLength()) > 1.0E-10D) {
      boundingBox = this.boundingBox;
    }

    Vec2f pitchYaw = snapshot.pitchYaw;
    if (MathHelper.degreesDifferenceAbs(this.pitchYaw.x, snapshot.pitchYaw.x) > 10.0D
        || MathHelper.degreesDifferenceAbs(this.pitchYaw.y, snapshot.pitchYaw.y) > 10.0D) {
      pitchYaw = this.pitchYaw;
    }

    return new EntitySnapshot(boundingBox, pitchYaw, this.eyeHeight);
  }

  @Override
  public String toString() {
    return "snapshot[pitch = " + this.pitchYaw.x + ", yaw = " + this.pitchYaw.y
        + ", " + this.boundingBox.toString() + "]";
  }

  public void write(PacketBuffer out) {
    out.writeDouble(this.boundingBox.minX);
    out.writeDouble(this.boundingBox.minY);
    out.writeDouble(this.boundingBox.minZ);
    out.writeDouble(this.boundingBox.maxX);
    out.writeDouble(this.boundingBox.maxY);
    out.writeDouble(this.boundingBox.maxZ);
    out.writeFloat(this.pitchYaw.x);
    out.writeFloat(this.pitchYaw.y);
  }

  public static EntitySnapshot read(PacketBuffer in) {
    double minX = in.readDouble();
    double minY = in.readDouble();
    double minZ = in.readDouble();
    double maxX = in.readDouble();
    double maxY = in.readDouble();
    double maxZ = in.readDouble();
    float pitch = in.readFloat();
    float yaw = in.readFloat();
    return new EntitySnapshot(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ),
        new Vec2f(pitch, yaw));
  }
}
