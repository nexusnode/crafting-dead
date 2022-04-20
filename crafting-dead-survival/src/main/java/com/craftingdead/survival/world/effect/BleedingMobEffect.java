/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
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
