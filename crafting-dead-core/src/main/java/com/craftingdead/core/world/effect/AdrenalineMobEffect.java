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
