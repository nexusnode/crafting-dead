/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.entity.grenade;

import java.util.OptionalInt;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.particle.GrenadeSmokeParticleData;
import com.craftingdead.core.world.entity.ModEntityTypes;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SmokeGrenadeEntity extends Grenade {

  public static final GrenadeSmokeParticleData LARGE_WHITE_SMOKE =
      new GrenadeSmokeParticleData(1F, 1F, 1F, 10F);
  private static final float START_DECREASING_PITCH_AT = 0.65F; // in %
  private static final int FIRE_BLOCK_STATE_FLAGS = 1;

  public SmokeGrenadeEntity(EntityType<? extends Grenade> entityIn, Level worldIn) {
    super(entityIn, worldIn);
  }

  public SmokeGrenadeEntity(Level worldIn) {
    super(ModEntityTypes.SMOKE_GRENADE.get(), worldIn);
  }

  public SmokeGrenadeEntity(LivingEntity thrower, Level worldIn) {
    super(ModEntityTypes.SMOKE_GRENADE.get(), thrower, worldIn);
  }

  @Override
  public OptionalInt getMinimumTicksUntilAutoDeactivation() {
    return OptionalInt.of(
        CraftingDead.serverConfig.explosivesSmokeGrenadeTicksBeforeDeactivation.get());
  }

  @Override
  public void onMotionStop(int stopsCount) {
    if (stopsCount == 1) {
      this.setActivated(true);
    }
  }

  @Override
  public void activatedChanged(boolean activated) {
    if (!activated) {
      if (!this.level.isClientSide()) {
        this.kill();
      }
    }
  }

  @Override
  public void onGrenadeTick() {
    int activatedTicksCount = this.getActivatedTicksCount();
    double radius = Mth.lerp(Math.min(activatedTicksCount, 30D) / 30D, 2D, 5D)
        * CraftingDead.serverConfig.explosivesSmokeGrenadeRadius.get();

    if (this.level.isClientSide()) {
      if (activatedTicksCount % 10 == 0) {
        this.getMinimumTicksUntilAutoDeactivation().ifPresent(maximumDuration -> {
          float progress =
              Math.max(activatedTicksCount - (maximumDuration * START_DECREASING_PITCH_AT), 0)
                  / (float) (maximumDuration * (1F - START_DECREASING_PITCH_AT));
          float gradualPitch = Mth.lerp(1F - progress, 0.5F, 1.7F);
          this.level.playLocalSound(this.getX(), this.getY(), this.getZ(),
              SoundEvents.FIRE_EXTINGUISH, SoundSource.HOSTILE, 1.5F, gradualPitch, false);
        });
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
        BlockPos to =
            this.blockPosition().offset(detectionRadius, detectionRadius, detectionRadius);
        BlockPos.betweenClosedStream(from, to).forEach(blockPos -> {

          double xDiff = this.getX() - blockPos.getX();
          double zDiff = this.getZ() - blockPos.getZ();
          double distance2D = Mth.sqrt((float) (xDiff * xDiff + zDiff * zDiff));

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
