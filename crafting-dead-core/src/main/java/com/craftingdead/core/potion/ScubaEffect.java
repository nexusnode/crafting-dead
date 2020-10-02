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
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.Vec3d;

public class ScubaEffect extends Effect {

  protected ScubaEffect() {
    super(EffectType.BENEFICIAL, 0x1F1FA1);
  }

  @Override
  public void performEffect(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.isInWater()) {
      livingEntity.setAir(livingEntity.getMaxAir());
      livingEntity.moveRelative(0.1F, new Vec3d(0.0D, 0.0D, 0.2D));
    }
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return true;
  }
}
