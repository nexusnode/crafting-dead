/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class AdrenalineEffect extends Effect {

  protected AdrenalineEffect() {
    super(EffectType.BENEFICIAL, 0xFFD000);
    this.addAttributesModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635",
        0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
  }

  @Override
  public void removeAttributesModifiersFromEntity(LivingEntity livingEntity,
      AttributeModifierManager attributes, int amplifier) {
    super.removeAttributesModifiersFromEntity(livingEntity, attributes, amplifier);
    livingEntity
        .setAbsorptionAmount(livingEntity.getAbsorptionAmount() - (float) (4 * (amplifier + 1)));
  }

  @Override
  public void applyAttributesModifiersToEntity(LivingEntity livingEntity,
      AttributeModifierManager attributes, int amplifier) {
    super.applyAttributesModifiersToEntity(livingEntity, attributes, amplifier);
    livingEntity
        .setAbsorptionAmount(livingEntity.getAbsorptionAmount() + (float) (4 * (amplifier + 1)));
  }
}
