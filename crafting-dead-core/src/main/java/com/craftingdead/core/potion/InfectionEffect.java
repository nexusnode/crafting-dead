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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.util.ModDamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class InfectionEffect extends Effect {

  private static final Random random = new Random();

  protected InfectionEffect() {
    super(EffectType.HARMFUL, 0x4e9331);
  }

  @Override
  public void performEffect(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.getHealth() > 1.0F) {
      livingEntity.attackEntityFrom(ModDamageSource.INFECTION, 1.0F);
    }
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return random.nextFloat() < 0.5F;
  }

  @Override
  public List<ItemStack> getCurativeItems() {
    List<ItemStack> items = new ArrayList<ItemStack>();
    items.add(new ItemStack(ModItems.CURE_SYRINGE::get));
    return items;
  }
}
