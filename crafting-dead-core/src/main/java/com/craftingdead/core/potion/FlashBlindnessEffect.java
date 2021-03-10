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

package com.craftingdead.core.potion;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

public class FlashBlindnessEffect extends Effect {

  protected FlashBlindnessEffect() {
    super(EffectType.HARMFUL, 0xFFFFFF);
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    return true;
  }

  @Override
  public boolean shouldRender(EffectInstance effect) {
    return false;
  }

  @Override
  public boolean shouldRenderHUD(EffectInstance effect) {
    return false;
  }

  @Override
  public boolean shouldRenderInvText(EffectInstance effect) {
    return false;
  }
}
