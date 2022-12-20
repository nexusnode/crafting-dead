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

package com.craftingdead.core.world.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.common.ForgeMod;

public class ParachuteMobEffect extends MobEffect {

  public ParachuteMobEffect() {
    super(MobEffectCategory.BENEFICIAL, 0xFFEFD1);
    this.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "c5a9e5c2-bd74-11eb-8529-0242ac130003",
        -0.07, AttributeModifier.Operation.ADDITION);
  }

  @Override
  public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
    livingEntity.resetFallDistance();
    if (livingEntity.isOnGround()) {
      livingEntity.removeEffect(ModMobEffects.PARACHUTE.get());
      return;
    }
    super.applyEffectTick(livingEntity, amplifier);
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    return true;
  }

  @Override
  public boolean isInstantenous() {
    return false;
  }
}
