package com.craftingdead.mod.item;

import net.minecraft.item.Food;

public class ModFoods {

  public static final Food POWER_BAR = (new Food.Builder()).hunger(4).saturation(0.3F).build();
  public static final Food CANDY_BAR = (new Food.Builder()).hunger(6).saturation(0.3F).build();
}
