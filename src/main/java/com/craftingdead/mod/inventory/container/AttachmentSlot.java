package com.craftingdead.mod.inventory.container;

import com.craftingdead.mod.item.AttachmentItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class AttachmentSlot extends Slot {

  public AttachmentSlot(IInventory inventory, int slotIndex, int x, int y) {
    super(inventory, slotIndex, x, y);
  }

  @Override
  public boolean isItemValid(ItemStack itemStack) {
    return itemStack.getItem() instanceof AttachmentItem
        && ((AttachmentItem) itemStack.getItem()).getInventorySlot().getIndex() == this
            .getSlotIndex();
  }
}
