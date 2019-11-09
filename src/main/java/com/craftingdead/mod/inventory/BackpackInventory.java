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
    NonNullList<ItemStack> list = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
    ItemStackHelper.loadAllItems(compound, list);
    for (int i = 0; i < list.size(); i++) {
      this.setInventorySlotContents(i, list.get(i));
    }
  }

  private void writeNBT(CompoundNBT compound) {
    NonNullList<ItemStack> list = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
    for (int i = 0; i < this.getSizeInventory(); i++) {
      list.add(this.removeStackFromSlot(i));
    }
    ItemStackHelper.saveAllItems(compound, list, false);
  }
}
