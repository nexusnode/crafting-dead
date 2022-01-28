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
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.survival.world.damagesource.SurvivalDamageSource;
import com.craftingdead.survival.world.item.SurvivalItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BleedingMobEffect extends MobEffect {

  private static final int MAXIMUM_DELAY = 20 * 6;
  private static final int MINIMUM_DELAY = 10;
  private static final int DELAY_REDUCTION_PER_LEVEL = 20;

  protected BleedingMobEffect() {
    super(MobEffectCategory.HARMFUL, 0x8B0000);
  }

  @Override
  public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.getHealth() > 1.0F) {
      livingEntity.hurt(SurvivalDamageSource.BLEEDING, 1.0F);
    }
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    int ticksBetweenHits = (MAXIMUM_DELAY - (amplifier * DELAY_REDUCTION_PER_LEVEL));
    return duration % Math.max(ticksBetweenHits, MINIMUM_DELAY) == 0;
  }

  @Override
  public List<ItemStack> getCurativeItems() {
    List<ItemStack> items = new ArrayList<ItemStack>();
    items.add(new ItemStack(ModItems.BANDAGE::get));
    items.add(new ItemStack(SurvivalItems.CLEAN_RAG::get));
    items.add(new ItemStack(ModItems.FIRST_AID_KIT::get));
    return items;
  }
}
