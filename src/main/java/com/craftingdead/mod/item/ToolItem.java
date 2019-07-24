package com.craftingdead.mod.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ToolItem extends Item {

  public ToolItem(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack getContainerItem(ItemStack itemStack) {
    ItemStack damagedStack = itemStack.copy();
    damagedStack.setDamage(damagedStack.getDamage() + 1);
    return damagedStack.getDamage() >= damagedStack.getMaxDamage() ? ItemStack.EMPTY : damagedStack;
  }

  @Override
  public boolean hasContainerItem(ItemStack stack) {
    return true;
  }
}
