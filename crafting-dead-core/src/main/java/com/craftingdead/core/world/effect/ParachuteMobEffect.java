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

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.ForgeMod;

public class ParachuteMobEffect extends Effect {

  public ParachuteMobEffect() {
    super(EffectType.BENEFICIAL, 0xFFEFD1);
    this.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "c5a9e5c2-bd74-11eb-8529-0242ac130003",
        -0.07, AttributeModifier.Operation.ADDITION);
  }

  @Override
  public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
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
