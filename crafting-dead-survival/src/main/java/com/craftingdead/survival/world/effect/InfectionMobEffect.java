/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
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
