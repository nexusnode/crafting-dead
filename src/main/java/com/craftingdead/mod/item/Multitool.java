package com.craftingdead.mod.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class Multitool extends SwordItem {

  public Multitool(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
    super(tier, attackDamageIn, attackSpeedIn, builder);
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
