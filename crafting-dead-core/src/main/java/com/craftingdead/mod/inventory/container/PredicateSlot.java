package com.craftingdead.mod.inventory.container;

import java.util.function.BiPredicate;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class PredicateSlot extends Slot {

  private final BiPredicate<PredicateSlot, ItemStack> predicate;

  public PredicateSlot(IInventory inventory, int index, int xPosition, int yPosition,
      BiPredicate<PredicateSlot, ItemStack> predicate) {
    super(inventory, index, xPosition, yPosition);
    this.predicate = predicate;
  }

  @Override
  public boolean isItemValid(ItemStack itemStack) {
    return this.predicate.test(this, itemStack);
  }
}
