package com.craftingdead.mod.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

public class BackpackInventory extends Inventory {

  private final ItemStack stack;

  public BackpackInventory(ItemStack stack, int count) {
    super(count);
    this.stack = stack;
    this.readItemStack();
  }

  public ItemStack getStack() {
    return this.stack;
  }

  public void readItemStack() {
    if (this.stack.getOrCreateTag() == null) {
      return;
    }
    this.readNBT(this.stack.getOrCreateTag());
  }

  public void writeItemStack() {
    if (this.isEmpty()) {
      this.stack.removeChildTag("Items");
      return;
    }
    this.writeNBT(this.stack.getTag());
  }

  private void readNBT(CompoundNBT compound) {
    NonNullList list = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
    ItemStackHelper.loadAllItems(compound, list);
    int i = 0;
    while (i < list.size()) {
      this.setInventorySlotContents(i, (ItemStack) list.get(i));
      ++i;
    }
  }

  private void writeNBT(CompoundNBT compound) {
    NonNullList list = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
    int i = 0;
    do {
      if (i >= list.size()) {
        ItemStackHelper.saveAllItems(compound, list, false);
        return;
      }
      list.set(i, this.removeStackFromSlot(i));
      ++i;
    } while (true);
  }

}
