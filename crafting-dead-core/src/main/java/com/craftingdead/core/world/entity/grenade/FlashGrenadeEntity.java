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
import com.craftingdead.core.particle.FlashParticleOptions;
import com.craftingdead.core.world.effect.FlashBlindnessMobEffect;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.craftingdead.core.world.entity.EntityUtil;
import com.craftingdead.core.world.entity.ModEntityTypes;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.equipment.Equipment;
import com.craftingdead.core.world.item.equipment.Hat;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FlashGrenadeEntity extends Grenade {

  public static final int EFFECT_MAX_DURATION = 110;

  public FlashGrenadeEntity(EntityType<? extends Grenade> entityIn, Level worldIn) {
    super(entityIn, worldIn);
  }

  public FlashGrenadeEntity(Level worldIn) {
    super(ModEntityTypes.FLASH_GRENADE.get(), worldIn);
  }

  public FlashGrenadeEntity(LivingEntity thrower, Level worldIn) {
    super(ModEntityTypes.FLASH_GRENADE.get(), thrower, worldIn);
  }

  @Override
  public void activatedChanged(boolean activated) {
    if (activated) {
      this.flash();
    } else {
      if (!this.level.isClientSide()) {
        this.kill();
      }
    }
  }

  @Override
  public OptionalInt getMinimumTicksUntilAutoActivation() {
    return OptionalInt.of(
        CraftingDead.serverConfig.explosivesFlashGrenadeTicksBeforeActivation.get());
  }

  @Override
  public OptionalInt getMinimumTicksUntilAutoDeactivation() {
    return OptionalInt.of(
        CraftingDead.serverConfig.explosivesFlashGrenadeTicksBeforeDeactivation.get());
  }

  @Override
  public void onGrenadeTick() {}

  @Override
  public void onMotionStop(int stopsCount) {}

  private void flash() {
    if (this.level.isClientSide()) {
      this.level.addParticle(new FlashParticleOptions(1F, 1F, 1F, 2F), this.getX(),
          this.getY(), this.getZ(), 0D, 0D, 0D);
      CraftingDead.getInstance().getClientDist().checkApplyFlashEffects(this);
    } else {
      this.playSound(SoundEvents.GENERIC_EXPLODE, 3F, 1.2F);

      var flashRange = CraftingDead.serverConfig.explosivesFlashRadius.get();
      this.level.getEntities(this, this.getBoundingBox().inflate(flashRange),
          (entity) -> entity instanceof LivingEntity && !(entity instanceof Player))
          .stream()
          .map(entity -> (LivingEntity) entity)
          .forEach(livingEntity -> {
            int duration = this
                .calculateDuration(livingEntity, EntityUtil.canSee(livingEntity, this, 90F));
            if (duration > 0) {
              boolean wasFlashApplied = ModMobEffects
                  .applyOrOverrideIfLonger(livingEntity,
                      new MobEffectInstance(ModMobEffects.FLASH_BLINDNESS.get(), duration));
              if (wasFlashApplied && livingEntity instanceof Mob) {
                Mob mobEntity = (Mob) livingEntity;
                // Removes the attack target
                mobEntity.setTarget(null);
              }
            }
          });
    }
  }

  /**
   * Calculates the amount of ticks that must be used in a {@link FlashBlindnessMobEffect} in
   * according to the possible variables, like blocks in front of view, resistance from equipments
   * and others.
   *
   * @return int - The amount in ticks. Zero if it should not be applied.
   */
  public int calculateDuration(LivingEntity viewerEntity, boolean visible) {
    if (!viewerEntity.hasLineOfSight(this)) {
      return 0;
    }

    var immuneToFlashes = viewerEntity
        .getCapability(LivingExtension.CAPABILITY)
        .resolve()
        .flatMap(living -> living.getEquipmentInSlot(Equipment.Slot.HAT, Hat.class))
        .map(Hat::immuneToFlashes)
        .orElse(false);

    var flashRange = CraftingDead.serverConfig.explosivesFlashRadius.get();
    if (visible && !immuneToFlashes) {
      double distanceProportion =
          Mth.clamp(this.distanceTo(viewerEntity) / flashRange, 0F, 1F);
      int calculatedDuration =
          (int) Mth.lerp(1F - distanceProportion, 0, EFFECT_MAX_DURATION);

      if (!(viewerEntity instanceof Player)) {
        // Non-player entities has extra duration
        calculatedDuration *= 4;
      }

      return calculatedDuration;
    }

    // Put a minimum duration for players, so they can see
    // a cool and short flash effect behind them
    return (viewerEntity instanceof Player) ? 5 : 0;
  }

  @Override
  public GrenadeItem asItem() {
    return ModItems.FLASH_GRENADE.get();
  }
}
