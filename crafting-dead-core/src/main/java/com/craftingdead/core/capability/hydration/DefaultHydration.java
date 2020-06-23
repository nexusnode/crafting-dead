package com.craftingdead.core.capability.hydration;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;

public class DefaultHydration implements IHydration {

  @Override
  public int getHydration(ItemStack itemStack) {
    return PotionUtils.getPotionFromItem(itemStack) == Potions.WATER ? 5 : 0;
  }
}
