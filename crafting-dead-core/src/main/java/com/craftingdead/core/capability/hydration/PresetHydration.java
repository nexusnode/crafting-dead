package com.craftingdead.core.capability.hydration;

import net.minecraft.item.ItemStack;

public class PresetHydration implements IHydration {

  private final int hydration;

  public PresetHydration(int hydration) {
    this.hydration = hydration;
  }

  @Override
  public int getHydration(ItemStack itemStack) {
    return this.hydration;
  }
}
