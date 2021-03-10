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

package com.craftingdead.core.entity.grenade;

import com.craftingdead.core.entity.ModEntityTypes;
import com.craftingdead.core.item.GrenadeItem;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.particle.GrenadeSmokeParticleData;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SmokeGrenadeEntity extends GrenadeEntity {

  public static final GrenadeSmokeParticleData LARGE_WHITE_SMOKE =
      new GrenadeSmokeParticleData(1F, 1F, 1F, 10F);
  private static final float START_DECREASING_PITCH_AT = 0.65F; // in %
  private static final int FIRE_BLOCK_STATE_FLAGS = 1;

  public SmokeGrenadeEntity(EntityType<? extends GrenadeEntity> entityIn, World worldIn) {
    super(entityIn, worldIn);
  }

  public SmokeGrenadeEntity(LivingEntity thrower, World worldIn) {
    super(ModEntityTypes.smokeGrenade, thrower, worldIn);
  }

  @Override
  public Integer getMinimumTicksUntilAutoDeactivation() {
    return 500;
  }

  @Override
  public void onMotionStop(int stopsCount) {
    if (stopsCount == 1) {
      this.setActivated(true);
    }
  }

  @Override
  public void onActivationStateChange(boolean activated) {
    if (!activated) {
      if (!this.level.isClientSide()) {
        this.remove();
      }
    }
  }

  @Override
  public void onGrenadeTick() {
    if (!this.isActivated()) {
      return;
    }

    int activatedTicksCount = this.getActivatedTicksCount();
    double radius = MathHelper.lerp(Math.min(activatedTicksCount, 30D) / 30D, 2D, 5D);

    if (this.level.isClientSide()) {
      if (activatedTicksCount % 10 == 0) {
        int maximumDuration = this.getMinimumTicksUntilAutoDeactivation();
        float progress =
            Math.max(activatedTicksCount - (maximumDuration * START_DECREASING_PITCH_AT), 0)
                / (float) (maximumDuration * (1F - START_DECREASING_PITCH_AT));
        float gradualPitch = MathHelper.lerp(1F - progress, 0.5F, 1.7F);
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(),
            SoundEvents.FIRE_EXTINGUISH, SoundCategory.HOSTILE, 1.5F, gradualPitch, false);
      }

      if (activatedTicksCount % 5 == 0) {
        double halfSphereVolume = (4D / 3D) * Math.PI * Math.pow(radius / 2D, 3);
        for (int i = 0; i < halfSphereVolume; i++) {
          double theta = this.random.nextDouble() * 2 * Math.PI;
          double phi = Math.acos(2 * this.random.nextDouble() - 1);

          double extraX = radius * Math.sin(phi) * Math.cos(theta);
          double extraY =
              radius * Math.abs(Math.sin(phi) * Math.sin(theta)) * this.random.nextDouble();
          double extraZ = radius * Math.cos(phi);

          this.level.addParticle(LARGE_WHITE_SMOKE, true, this.getX() + extraX,
              this.getY() + extraY, this.getZ() + extraZ, 0, 0, 0);
        }
      }
    } else {
      if (activatedTicksCount % 10 == 0) {
        // Extra size for better detection
        double detectionRadius = radius + 1.5D;

        BlockPos from = this.blockPosition().offset(-detectionRadius, 0, -detectionRadius);
        BlockPos to = this.blockPosition().offset(detectionRadius, detectionRadius, detectionRadius);
        BlockPos.betweenClosedStream(from, to).forEach(blockPos -> {

          double xDiff = this.getX() - blockPos.getX();
          double zDiff = this.getZ() - blockPos.getZ();
          double distance2D = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);

          if (distance2D <= detectionRadius) {
            BlockState blockState = this.level.getBlockState(blockPos);
            if (blockState.getBlock() instanceof FireBlock) {
              // Sets the fire block to its final age.
              // It should be extinguished in a short time.
              this.level.setBlock(blockPos, blockState.setValue(FireBlock.AGE, 15),
                  FIRE_BLOCK_STATE_FLAGS);
            }
          }
        });
      }
    }
  }

  @Override
  public GrenadeItem asItem() {
    return ModItems.SMOKE_GRENADE.get();
  }
}
