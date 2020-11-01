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

import javax.annotation.Nullable;
import com.craftingdead.core.capability.living.IPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.InstantEffect;

public class HydrateEffect extends InstantEffect {

  protected HydrateEffect() {
    super(EffectType.BENEFICIAL, 0x0077BE);
  }

  @Override
  public void performEffect(LivingEntity livingEntity, int amplifier) {
    if (livingEntity instanceof PlayerEntity) {
      IPlayer.getOptional((PlayerEntity) livingEntity)
          .ifPresent(player -> player.setWater(player.getWater() + amplifier + 1));
    }
  }

  @Override
  public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource,
      LivingEntity entityLivingBaseIn, int amplifier, double health) {
    this.performEffect(entityLivingBaseIn, amplifier);
  }

  @Override
  public boolean shouldRender(EffectInstance effect) {
    return false;
  }

  @Override
  public boolean shouldRenderInvText(EffectInstance effect) {
    return false;
  }

  @Override
  public boolean shouldRenderHUD(EffectInstance effect) {
    return false;
  }
}
