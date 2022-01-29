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

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.particle.RGBFlashParticleData;
import com.craftingdead.core.world.effect.FlashBlindnessMobEffect;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.craftingdead.core.world.entity.EntityUtil;
import com.craftingdead.core.world.entity.ModEntityTypes;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.hat.Hat;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlashGrenadeEntity extends Grenade {

  public static final double FLASH_MAX_RANGE = 50D;
  public static final int EFFECT_MAX_DURATION = 110;

  public FlashGrenadeEntity(EntityType<? extends Grenade> entityIn, Level worldIn) {
    super(entityIn, worldIn);
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
  public Integer getMinimumTicksUntilAutoActivation() {
    return 30;
  }

  @Override
  public Integer getMinimumTicksUntilAutoDeactivation() {
    return 5;
  }

  @Override
  public void onGrenadeTick() {}

  @Override
  public void onMotionStop(int stopsCount) {}

  private void flash() {
    if (this.level.isClientSide()) {
      this.level.addParticle(new RGBFlashParticleData(1F, 1F, 1F, 2F), this.getX(),
          this.getY(), this.getZ(), 0D, 0D, 0D);
      CraftingDead.getInstance().getClientDist().checkApplyFlashEffects(this);
    } else {
      this.playSound(SoundEvents.GENERIC_EXPLODE, 3F, 1.2F);

      this.level.getEntities(this, this.getBoundingBox().inflate(FLASH_MAX_RANGE),
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
  public int calculateDuration(LivingEntity viewerEntity, boolean insideFOV) {
    if (!viewerEntity.hasLineOfSight(this)) {
      return 0;
    }

    ItemStack hatItemStack = viewerEntity
        .getCapability(LivingExtension.CAPABILITY)
        .map(living -> living.getItemHandler().getStackInSlot(ModEquipmentSlot.HAT.getIndex()))
        .orElse(ItemStack.EMPTY);

    final boolean isImmuneToFlashes =
        hatItemStack.getCapability(Hat.CAPABILITY).map(Hat::isImmuneToFlashes).orElse(false);

    if (insideFOV && !isImmuneToFlashes) {
      double distanceProportion =
          Mth.clamp(this.distanceTo(viewerEntity) / FLASH_MAX_RANGE, 0F, 1F);
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
