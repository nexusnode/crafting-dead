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

package com.craftingdead.survival.world.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public abstract class AbstractCorpseEntity extends Entity {

  private static final AxisAlignedBB NULL_AABB = new AxisAlignedBB(0D, 0D, 0D, 0D, 0D, 0D);

  private AxisAlignedBB boundingBox;

  public AbstractCorpseEntity(EntityType<?> entityType, World world) {
    super(entityType, world);
    this.boundingBox = NULL_AABB;
  }

  public void recalculateBoundingBox() {
    Direction facing = this.entityData == null ? Direction.NORTH : Direction.fromYRot(this.yRot);
    this.boundingBox = new AxisAlignedBB(
        this.getX() - (facing.getStepX() != 0 ? 1D : 0.5D),
        this.getY(),
        this.getZ() - (facing.getStepZ() != 0 ? 1D : 0.5D),
        this.getX() + (facing.getStepX() != 0 ? 1D : 0.5D),
        this.getY() + 0.5D,
        this.getZ() + (facing.getStepZ() != 0 ? 1D : 0.5D));
  }

  @Override
  public void tick() {
    super.tick();
    this.recalculateBoundingBox();
  }

  @Override
  public AxisAlignedBB getBoundingBox() {
    return this.boundingBox;
  }

  @Override
  public void setBoundingBox(AxisAlignedBB boundingBox) {
    this.boundingBox = boundingBox;
  }

  @Override
  public void setPos(double x, double y, double z) {
    super.setPos(x, y, z);
    this.recalculateBoundingBox();
  }
}
