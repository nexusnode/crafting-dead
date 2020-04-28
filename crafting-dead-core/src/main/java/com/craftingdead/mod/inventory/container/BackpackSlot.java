package com.craftingdead.mod.inventory.container;

import com.craftingdead.mod.item.BackpackItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class BackpackSlot extends Slot {

  public BackpackSlot(IInventory inventory, int slotIndex, int x, int y) {
    super(inventory, slotIndex, x, y);
  }

  @Override
  public boolean isItemValid(ItemStack itemStack) {
    return itemStack.getItem() instanceof BackpackItem;
  }
}
