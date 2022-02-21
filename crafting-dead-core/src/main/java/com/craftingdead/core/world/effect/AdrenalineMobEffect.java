/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

package com.craftingdead.core.world.effect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class AdrenalineMobEffect extends MobEffect {

  protected AdrenalineMobEffect() {
    super(MobEffectCategory.BENEFICIAL, 0xFFD000);
    this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635",
        0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
  }

  @Override
  public void removeAttributeModifiers(LivingEntity livingEntity,
      AttributeMap attributes, int amplifier) {
    super.removeAttributeModifiers(livingEntity, attributes, amplifier);
    livingEntity
        .setAbsorptionAmount(livingEntity.getAbsorptionAmount() - (float) (4 * (amplifier + 1)));
  }

  @Override
  public void addAttributeModifiers(LivingEntity livingEntity,
      AttributeMap attributes, int amplifier) {
    super.addAttributeModifiers(livingEntity, attributes, amplifier);
    livingEntity
        .setAbsorptionAmount(livingEntity.getAbsorptionAmount() + (float) (4 * (amplifier + 1)));
  }
}
