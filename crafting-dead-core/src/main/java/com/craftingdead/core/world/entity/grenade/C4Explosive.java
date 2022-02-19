/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.core.world.entity.grenade;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.entity.ModEntityTypes;
import com.craftingdead.core.world.explosion.ExplosionHook;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.ModItems;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.tuple.Triple;

public class C4Explosive extends Grenade implements ExplosionHook {

  private static final Triple<SoundEvent, Float, Float> C4_BOUNCE_SOUND =
      Triple.of(SoundEvents.PLAYER_SMALL_FALL, 1.0F, 1.5F);

  public C4Explosive(EntityType<? extends Grenade> entityIn, Level worldIn) {
    super(entityIn, worldIn);
  }

  public C4Explosive(LivingEntity thrower, Level worldIn) {
    super(ModEntityTypes.C4_EXPLOSIVE.get(), thrower, worldIn);
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    if (source.isExplosion() || source.isFire()) {
      // TODO Save who activated the grenade, so the true source
      // of this DamageSource could be used when the grenade explodes.
      this.setActivated(true);
    }
    return super.hurt(source, amount);
  }

  @Override
  public void onGrenadeTick() {}

  @Override
  public void activatedChanged(boolean activated) {
    if (activated) {
      if (!this.level.isClientSide()) {
        this.kill();
        this.level.explode(this, this.createDamageSource(), null,
            this.getX(), this.getY() + this.getBbHeight(), this.getZ(),
            CraftingDead.serverConfig.explosivesC4Radius.get().floatValue(), false,
            CraftingDead.serverConfig.explosivesC4ExplosionMode.get());
      }
    }
  }

  @Override
  public boolean canBePickedUp(Player playerFrom) {
    return this.isStoppedInGround();
  }

  @Override
  public Optional<Float> getBounceFactor(BlockHitResult blockRayTraceResult) {
    return blockRayTraceResult.getDirection() == Direction.UP
        ? Optional.empty()
        : super.getBounceFactor(blockRayTraceResult);
  }

  @Override
  public boolean canBeRemotelyActivated() {
    return true;
  }

  @Override
  public Triple<SoundEvent, Float, Float> getBounceSound(BlockHitResult blockRayTraceResult) {
    return C4_BOUNCE_SOUND;
  }

  @Override
  public GrenadeItem asItem() {
    return ModItems.C4.get();
  }

  @Override
  public void onMotionStop(int stopsCount) {}

  @Override
  public double getDamageMultiplier() {
    return CraftingDead.serverConfig.explosivesC4KnockbackMultiplier.get();
  }

  @Override
  public double getKnockbackMultiplier() {
    return CraftingDead.serverConfig.explosivesC4DamageMultiplier.get();
  }
}
