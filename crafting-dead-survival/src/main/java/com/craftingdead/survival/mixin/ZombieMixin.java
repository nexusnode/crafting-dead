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

package com.craftingdead.survival.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.survival.world.entity.extension.ZombieHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {

  protected ZombieMixin(EntityType<? extends Monster> type, Level level) {
    super(type, level);
  }

  @Override
  public float getWalkTargetValue(BlockPos pos, LevelReader level) {
    return 0.0F;
  }

  @Inject(at = @At("RETURN"), method = "setBaby")
  public void setBaby(boolean baby, CallbackInfo callbackInfo) {
    var zombie = (Zombie) (Object) this;
    zombie.getCapability(LivingExtension.CAPABILITY).resolve()
        .flatMap(extension -> extension.getHandler(ZombieHandler.TYPE))
        .ifPresent(handler -> handler.handleSetBaby(baby));
  }

  @Inject(at = @At("RETURN"), method = "isSunSensitive", cancellable = true)
  public void isSunSensitive(CallbackInfoReturnable<Boolean> callbackInfo) {
    callbackInfo.setReturnValue(false);
  }

  @Inject(at = @At("RETURN"), method = "populateDefaultEquipmentSlots")
  public void populateDefaultEquipmentSlots(DifficultyInstance difficulty,
      CallbackInfo callbackInfo) {
    var zombie = (Zombie) (Object) this;
    zombie.getCapability(LivingExtension.CAPABILITY).resolve()
        .flatMap(extension -> extension.getHandler(ZombieHandler.TYPE))
        .ifPresent(handler -> handler.populateDefaultEquipmentSlots(difficulty));
  }
}
