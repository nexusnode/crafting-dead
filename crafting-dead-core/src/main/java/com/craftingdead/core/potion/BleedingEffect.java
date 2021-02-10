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
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.util.ModDamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class BleedingEffect extends Effect {

  private static final int MAXIMUM_DELAY = 20 * 6;
  private static final int MINIMUM_DELAY = 10;
  private static final int DELAY_REDUCTION_PER_LEVEL = 20;

  protected BleedingEffect() {
    super(EffectType.HARMFUL, 0x8B0000);
  }

  @Override
  public void performEffect(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.getHealth() > 1.0F) {
      livingEntity.attackEntityFrom(ModDamageSource.BLEEDING, 1.0F);
    }
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    int ticksBetweenHits = (MAXIMUM_DELAY - (amplifier * DELAY_REDUCTION_PER_LEVEL));
    return duration % Math.max(ticksBetweenHits, MINIMUM_DELAY) == 0;
  }

  @Override
  public List<ItemStack> getCurativeItems() {
    List<ItemStack> items = new ArrayList<ItemStack>();
    items.add(new ItemStack(ModItems.BANDAGE::get));
    items.add(new ItemStack(ModItems.CLEAN_RAG::get));
    items.add(new ItemStack(ModItems.FIRST_AID_KIT::get));
    return items;
  }
}
