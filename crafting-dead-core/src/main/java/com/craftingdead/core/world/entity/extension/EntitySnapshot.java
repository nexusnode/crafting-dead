/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.entity.extension;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public record EntitySnapshot(Vec3 position, AABB boundingBox, Vec2 rotation, float eyeHeight,
    boolean complete) {

  public EntitySnapshot(Vec3 position, AABB boundingBox, Vec2 rotation) {
    this(position, boundingBox, rotation, -1, false);
  }

  public EntitySnapshot(Entity entity, float partialTick) {
    this(entity.getPosition(partialTick),
        entity.getBoundingBox(),
        new Vec2(entity.getViewXRot(partialTick), entity.getViewYRot(partialTick)),
        entity.getEyeHeight(), true);
  }

  public EntitySnapshot combineUntrustedSnapshot(EntitySnapshot snapshot) {
    if (!this.complete) {
      throw new UnsupportedOperationException("Snapshot not complete");
    }

    var position = snapshot.position;
    if (this.position.distanceTo(snapshot.position) > 1.0D) {
      position = this.position;
    }

    var boundingBox = snapshot.boundingBox;
    if (Math.abs(this.boundingBox.getSize()
        - snapshot.boundingBox.getSize()) > 1.0E-10D) {
      boundingBox = this.boundingBox;
    }

    var rotation = snapshot.rotation;
    if (Mth.degreesDifferenceAbs(this.rotation.x, snapshot.rotation.x) > 10.0D ||
        Mth.degreesDifferenceAbs(this.rotation.y, snapshot.rotation.y) > 10.0D) {
      rotation = this.rotation;
    }

    return new EntitySnapshot(position, boundingBox, rotation, this.eyeHeight, true);
  }

  public void encode(FriendlyByteBuf out) {
    out.writeDouble(this.position.x());
    out.writeDouble(this.position.y());
    out.writeDouble(this.position.z());
    out.writeDouble(this.boundingBox.minX);
    out.writeDouble(this.boundingBox.minY);
    out.writeDouble(this.boundingBox.minZ);
    out.writeDouble(this.boundingBox.maxX);
    out.writeDouble(this.boundingBox.maxY);
    out.writeDouble(this.boundingBox.maxZ);
    out.writeFloat(this.rotation.x);
    out.writeFloat(this.rotation.y);
  }

  public static EntitySnapshot decode(FriendlyByteBuf in) {
    var position = new Vec3(in.readDouble(), in.readDouble(), in.readDouble());
    var minX = in.readDouble();
    var minY = in.readDouble();
    var minZ = in.readDouble();
    var maxX = in.readDouble();
    var maxY = in.readDouble();
    var maxZ = in.readDouble();
    var rotation = new Vec2(in.readFloat(), in.readFloat());
    return new EntitySnapshot(position, new AABB(minX, minY, minZ, maxX, maxY, maxZ), rotation);
  }
}
