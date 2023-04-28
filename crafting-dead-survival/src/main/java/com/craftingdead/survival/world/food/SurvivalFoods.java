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

package com.craftingdead.survival.world.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class SurvivalFoods {

  public static final FoodProperties POWER_BAR =
      new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build();
  public static final FoodProperties CANDY_BAR =
      new FoodProperties.Builder().nutrition(6).saturationMod(0.3F).build();
  public static final FoodProperties CEREAL =
      new FoodProperties.Builder().nutrition(10).saturationMod(0.6F).build();
  public static final FoodProperties CANNED_CORN =
      new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).build();
  public static final FoodProperties CANNED_BEANS =
      new FoodProperties.Builder().nutrition(8).saturationMod(0.6F).build();
  public static final FoodProperties CANNED_TUNA =
      new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).build();
  public static final FoodProperties CANNED_PEACHES = new FoodProperties.Builder()
      .nutrition(6)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 1), 1.0F)
      .build();
  public static final FoodProperties CANNED_PASTA =
      new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).build();
  public static final FoodProperties CANNED_BACON =
      new FoodProperties.Builder().nutrition(8).saturationMod(0.6F).build();
  public static final FoodProperties CANNED_CUSTARD = new FoodProperties.Builder()
      .nutrition(4)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
      .build();
  public static final FoodProperties CANNED_PICKLES =
      new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).build();
  public static final FoodProperties CANNED_DOG_FOOD =
      new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).build();
  public static final FoodProperties CANNED_TOMATO_SOUP = new FoodProperties.Builder()
      .nutrition(4)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 2), 1.0F)
      .build();
  public static final FoodProperties MRE = new FoodProperties.Builder()
      .nutrition(16)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 7), 1.0F)
      .build();
  public static final FoodProperties ORANGE = new FoodProperties.Builder()
      .nutrition(5)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 1), 1.0F)
      .build();
  public static final FoodProperties ROTTEN_ORANGE = new FoodProperties.Builder()
      .nutrition(2)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 0), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 1), 0.2F)
      .build();
  public static final FoodProperties PEAR = new FoodProperties.Builder()
      .nutrition(4)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 2), 1.0F)
      .build();
  public static final FoodProperties ROTTEN_PEAR = new FoodProperties.Builder()
      .nutrition(1)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 0), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 1), 0.2F)
      .build();
  public static final FoodProperties RICE_BAG =
      new FoodProperties.Builder().nutrition(8).saturationMod(0.6F).build();
  public static final FoodProperties ROTTEN_APPLE = new FoodProperties.Builder()
      .nutrition(1)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 0), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 1), 0.2F)
      .build();
  public static final FoodProperties NOODLES = new FoodProperties.Builder()
      .nutrition(6)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
      .build();
  public static final FoodProperties ROTTEN_MELON_SLICE = new FoodProperties.Builder()
      .nutrition(3)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 1), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 1), 0.2F)
      .build();
  public static final FoodProperties BLUEBERRY = new FoodProperties.Builder()
      .nutrition(4)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
      .build();
  public static final FoodProperties ROTTEN_BLUEBERRY = new FoodProperties.Builder()
      .nutrition(1)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 1), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 1), 0.2F)
      .build();
  public static final FoodProperties RASPBERRY = new FoodProperties.Builder()
      .nutrition(3)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
      .build();
  public static final FoodProperties ROTTEN_RASPBERRY = new FoodProperties.Builder()
      .nutrition(1)
      .saturationMod(0.6F)
      // .effect(() -> new MobEffectInstance(ModEffects.HYDRATE.get(), 1, 1), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 1), 0.2F)
      .build();
  public static final FoodProperties CHIPS =
      new FoodProperties.Builder().nutrition(3).saturationMod(0.6F).build();
  public static final FoodProperties RANCH_CHIPS =
      new FoodProperties.Builder().nutrition(3).saturationMod(0.6F).build();
  public static final FoodProperties CHEESY_CHIPS =
      new FoodProperties.Builder().nutrition(3).saturationMod(0.6F).build();
  public static final FoodProperties SALTED_CHIPS =
      new FoodProperties.Builder().nutrition(3).saturationMod(0.6F).build();
  public static final FoodProperties POPCORN =
      new FoodProperties.Builder().nutrition(3).saturationMod(0.6F).build();
  public static final FoodProperties NUTTY_CEREAL =
      new FoodProperties.Builder().nutrition(10).saturationMod(0.6F).build();
  public static final FoodProperties EMERALD_CEREAL =
      new FoodProperties.Builder().nutrition(10).saturationMod(0.6F).build();
  public static final FoodProperties FLAKE_CEREAL =
      new FoodProperties.Builder().nutrition(10).saturationMod(0.6F).build();
}
