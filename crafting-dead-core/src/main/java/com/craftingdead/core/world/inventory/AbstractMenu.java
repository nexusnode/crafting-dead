package com.craftingdead.core.world.inventory;

import javax.annotation.Nullable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public abstract class AbstractMenu extends Container {

  public static final int SLOT_SIZE = 18;

  @Nullable
  protected IItemHandler contents;
  protected IInventory playerInventory;

  public AbstractMenu(ContainerType<?> type, int id, IInventory playerInventory,
      IItemHandler contents) {
    super(type, id);
    this.playerInventory = playerInventory;
    this.contents = contents;
  }

  protected void addPlayerInventorySlots() {
    if (this.playerInventory != null) {
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 9; j++) {
          this.addSlot(new Slot(this.playerInventory, j + i * 9 + 9, 8 + j * SLOT_SIZE,
              84 + i * SLOT_SIZE + this.getInventoryOffset()));
        }
      }

      // Hot bar
      for (int k = 0; k < 9; k++) {
        this.addSlot(
            new Slot(this.playerInventory, k, 8 + k * SLOT_SIZE, 142 + this.getInventoryOffset()));
      }
    }
  }

  public int getInventoryOffset() {
    return 0;
  }

  public int getContentsSize() {
    return this.contents == null ? 0 : this.contents.getSlots();
  }

  @Nullable
  public IInventory getPlayerInventory() {
    return this.playerInventory;
  }

  @Override
  public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);

    if (slot != null && slot.hasItem()) {
      ItemStack stack = slot.getItem();
      itemstack = stack.copy();

      if (index < this.getContentsSize()) {
        if (!this.moveItemStackTo(stack, this.getContentsSize(), this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.moveItemStackTo(stack, 0, this.getContentsSize(), false)) {
        return ItemStack.EMPTY;
      }

      if (stack.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
    }
    return itemstack;
  }
}
