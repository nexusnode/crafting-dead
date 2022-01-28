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

package com.craftingdead.survival.world.effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.craftingdead.survival.world.damagesource.SurvivalDamageSource;
import com.craftingdead.survival.world.item.SurvivalItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class InfectionMobEffect extends MobEffect {

  private static final Random random = new Random();

  protected InfectionMobEffect() {
    super(MobEffectCategory.HARMFUL, 0x4e9331);
  }

  @Override
  public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.getHealth() > 1.0F) {
      livingEntity.hurt(SurvivalDamageSource.INFECTION, 1.0F);
    }
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    return duration % ((20 * 10) + random.nextInt(120)) == 0;
  }

  @Override
  public List<ItemStack> getCurativeItems() {
    List<ItemStack> items = new ArrayList<ItemStack>();
    items.add(new ItemStack(SurvivalItems.CURE_SYRINGE::get));
    return items;
  }
}
