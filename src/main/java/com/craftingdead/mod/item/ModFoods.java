package com.craftingdead.mod.item;

import com.craftingdead.mod.potion.ModEffects;
import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModFoods {

  public static final Food POWER_BAR = new Food.Builder().hunger(4).saturation(0.3F).build();
  public static final Food CANDY_BAR = new Food.Builder().hunger(6).saturation(0.3F).build();
  public static final Food CEREAL = new Food.Builder().hunger(10).saturation(0.6F).build();
  public static final Food CANNED_CORN = new Food.Builder().hunger(6).saturation(0.6F).build();
  public static final Food CANNED_BEANS = new Food.Builder().hunger(8).saturation(0.6F).build();
  public static final Food CANNED_TUNA = new Food.Builder().hunger(6).saturation(0.6F).build();
  public static final Food CANNED_PEACH = new Food.Builder()
      .hunger(6)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 1), 1.0F)
      .build();
  public static final Food CANNED_PASTA = new Food.Builder().hunger(6).saturation(0.6F).build();
  public static final Food CANNED_BACON = new Food.Builder().hunger(8).saturation(0.6F).build();
  public static final Food CANNED_CUSTARD = new Food.Builder()
      .hunger(4)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
      .build();
  public static final Food CANNED_PICKLES = new Food.Builder().hunger(4).saturation(0.6F).build();
  public static final Food CANNED_DOG_FOOD = new Food.Builder().hunger(2).saturation(0.6F).build();
  public static final Food CANNED_TOMATO_SOUP = new Food.Builder()
      .hunger(4)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 2), 1.0F)
      .build();
  public static final Food MRE = new Food.Builder()
      .hunger(16)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 7), 1.0F)
      .build();
  public static final Food ORANGE = new Food.Builder()
      .hunger(5)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 1), 1.0F)
      .build();
  public static final Food ROTTEN_ORANGE = new Food.Builder()
      .hunger(2)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 0), 1.0F)
      .effect(() -> new EffectInstance(Effects.HUNGER, 600, 1), 0.2F)
      .build();
  public static final Food PEAR = new Food.Builder()
      .hunger(4)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 2), 1.0F)
      .build();
  public static final Food ROTTEN_PEAR = new Food.Builder()
      .hunger(1)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 0), 1.0F)
      .effect(() -> new EffectInstance(Effects.HUNGER, 600, 1), 0.2F)
      .build();
  public static final Food RICE_BAG = new Food.Builder().hunger(8).saturation(0.6F).build();
  public static final Food APPLE = new Food.Builder()
      .hunger(5)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 2), 1.0F)
      .build();
  public static final Food ROTTEN_APPLE = new Food.Builder()
      .hunger(1)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 0), 1.0F)
      .effect(() -> new EffectInstance(Effects.HUNGER, 600, 1), 0.2F)
      .build();
  public static final Food NOODLE_CUP = new Food.Builder()
      .hunger(6)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
      .build();
  public static final Food WATERMELON = new Food.Builder()
      .hunger(6)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 5), 1.0F)
      .build();
  public static final Food ROTTEN_WATERMELON = new Food.Builder()
      .hunger(3)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 1), 1.0F)
      .effect(() -> new EffectInstance(Effects.HUNGER, 600, 1), 0.2F)
      .build();
  public static final Food BLUEBERRY = new Food.Builder()
      .hunger(4)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
      .build();
  public static final Food ROTTEN_BLUEBERRY = new Food.Builder()
      .hunger(1)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 1), 1.0F)
      .effect(() -> new EffectInstance(Effects.HUNGER, 600, 1), 0.2F)
      .build();
  public static final Food RASPBERRY = new Food.Builder()
      .hunger(3)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
      .build();
  public static final Food ROTTEN_RASPBERRY = new Food.Builder()
      .hunger(1)
      .saturation(0.6F)
      .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 1), 1.0F)
      .effect(() -> new EffectInstance(Effects.HUNGER, 600, 1), 0.2F)
      .build();
  public static final Food CHIPS = new Food.Builder().hunger(3).saturation(0.6F).build();
  public static final Food RANCH_CHIPS = new Food.Builder().hunger(3).saturation(0.6F).build();
  public static final Food CHEESE_CHIPS = new Food.Builder().hunger(3).saturation(0.6F).build();
  public static final Food SALT_CHIPS = new Food.Builder().hunger(3).saturation(0.6F).build();
  public static final Food POPCORN = new Food.Builder().hunger(3).saturation(0.6F).build();
  public static final Food NUTTY_CEREAL = new Food.Builder().hunger(10).saturation(0.6F).build();
  public static final Food EMERALD_CEREAL = new Food.Builder().hunger(10).saturation(0.6F).build();
  public static final Food FLAKE_CEREAL = new Food.Builder().hunger(10).saturation(0.6F).build();
}
