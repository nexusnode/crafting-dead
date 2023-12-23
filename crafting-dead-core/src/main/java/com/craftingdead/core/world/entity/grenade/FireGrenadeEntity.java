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

import com.craftingdead.core.ServerConfig;
import com.craftingdead.core.world.entity.ExplosionSource;
import com.craftingdead.core.world.entity.ModEntityTypes;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;

public class FireGrenadeEntity extends Grenade implements ExplosionSource {

  private static final BounceSound FIRE_GRENADE_BOUNCE_SOUND =
      new BounceSound(SoundEvents.GLASS_BREAK, 1.0F, 0.9F);

  public FireGrenadeEntity(EntityType<? extends Grenade> entityIn, Level worldIn) {
    super(entityIn, worldIn);
  }

  public FireGrenadeEntity(Level worldIn) {
    super(ModEntityTypes.FIRE_GRENADE.get(), worldIn);
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
        var fireRadius = ServerConfig.instance.explosivesFireGrenadeRadius.get().floatValue();
        this.level.explode(this, this.createDamageSource(), null,
            this.getX(), this.getY() + this.getBbHeight(), this.getZ(), fireRadius, true,
            ServerConfig.instance.explosivesFireGrenadeExplosionMode.get());

        BlockPos.betweenClosedStream(this.blockPosition().offset(-fireRadius, 0, -fireRadius),
            this.blockPosition().offset(fireRadius, 0, fireRadius)).forEach(blockPos -> {
              if (this.level.getBlockState(blockPos).isAir()) {
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
  public BounceSound getBounceSound(BlockHitResult blockRayTraceResult) {
    return blockRayTraceResult.getDirection() == Direction.UP
        ? FIRE_GRENADE_BOUNCE_SOUND
        : super.getBounceSound(blockRayTraceResult);
  }

  @Override
  public void onMotionStop(int stopsCount) {}

  @Override
  public float getDamageMultiplier() {
    return ServerConfig.instance.explosivesFireGrenadeDamageMultiplier.get().floatValue();
  }

  @Override
  public double getKnockbackMultiplier() {
    return ServerConfig.instance.explosivesFireGrenadeKnockbackMultiplier.get();
  }

  protected record BounceSound(SoundEvent soundEvent, float volume, float pitch) {}
}
