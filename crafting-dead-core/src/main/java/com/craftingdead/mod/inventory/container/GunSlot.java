package com.craftingdead.mod.inventory.container;

import com.craftingdead.mod.item.GunItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class GunSlot extends Slot {

  public GunSlot(IInventory inventory, int slotIndex, int x, int y) {
    super(inventory, slotIndex, x, y);
  }

  @Override
  public boolean isItemValid(ItemStack itemStack) {
    return itemStack.getItem() instanceof GunItem;
  }
}
