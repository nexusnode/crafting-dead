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

package com.craftingdead.core.world.entity.grenade;

import org.apache.commons.lang3.tuple.Triple;
import com.craftingdead.core.world.entity.ModEntityTypes;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class FireGrenadeEntity extends Grenade {

  private static final double FIRE_RADIUS = 2D;
  private static final Triple<SoundEvent, Float, Float> FIRE_GRENADE_BOUNCE_SOUND =
      Triple.of(SoundEvents.GLASS_BREAK, 1.0F, 0.9F);

  public FireGrenadeEntity(EntityType<? extends Grenade> entityIn, Level worldIn) {
    super(entityIn, worldIn);
  }

  public FireGrenadeEntity(LivingEntity thrower, Level worldIn) {
    super(ModEntityTypes.FIRE_GRENADE.get(), thrower, worldIn);
  }

  @Override
  public void onSurfaceHit(BlockHitResult blockRayTraceResult) {
    super.onSurfaceHit(blockRayTraceResult);
    if (blockRayTraceResult.getDirection() == Direction.UP) {
      this.setActivated(true);
    }
  }

  @Override
  public void activatedChanged(boolean activated) {
    if (activated) {
      if (!this.level.isClientSide()) {
        this.kill();
        this.level.explode(this,
            this.createDamageSource(), null,
            this.getX(), this.getY() + this.getBbHeight(), this.getZ(), 2F, true,
            Explosion.BlockInteraction.NONE);

        BlockPos.betweenClosedStream(this.blockPosition().offset(-FIRE_RADIUS, 0, -FIRE_RADIUS),
            this.blockPosition().offset(FIRE_RADIUS, 0, FIRE_RADIUS)).forEach(blockPos -> {
              if (this.level.getBlockState(blockPos).getBlock() == Blocks.AIR) {
                if (Math.random() <= 0.8D) {
                  this.level.setBlockAndUpdate(blockPos, Blocks.FIRE.defaultBlockState());
                }
              }
            });
      }
    }
  }

  @Override
  public void onGrenadeTick() {}

  @Override
  public GrenadeItem asItem() {
    return ModItems.FIRE_GRENADE.get();
  }

  @Override
  public Triple<SoundEvent, Float, Float> getBounceSound(BlockHitResult blockRayTraceResult) {
    return blockRayTraceResult.getDirection() == Direction.UP
        ? FIRE_GRENADE_BOUNCE_SOUND
        : super.getBounceSound(blockRayTraceResult);
  }

  @Override
  public void onMotionStop(int stopsCount) {}
}
