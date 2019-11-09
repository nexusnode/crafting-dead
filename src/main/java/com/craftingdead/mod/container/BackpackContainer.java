package com.craftingdead.mod.container;


import com.craftingdead.mod.inventory.BackpackInventory;
import com.craftingdead.mod.type.Backpack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;


public class BackpackContainer extends Container {

  private final IInventory backpackInventory;
  private final Backpack backpack;

  public static BackpackContainer createClientContainer(int id, PlayerInventory playerInventory,
      PacketBuffer buffer) {
    Backpack backpack = buffer.readEnumValue(Backpack.class);
    return new BackpackContainer(id, playerInventory, new Inventory(backpack.getInventorySize()),
        backpack);
  }

  public BackpackContainer(int id, PlayerInventory playerInventory, IInventory backpackInventory,
      Backpack backpack) {
    super(ModContainerType.backpack, id);
    this.backpackInventory = backpackInventory;
    this.backpack = backpack;
    this.appendBackpackInventory(backpack.getSlotBackpackX(), backpack.getSlotBackpackY());
    this.appendPlayerInventory(playerInventory, backpack.getSlotPlayerX(),
        backpack.getSlotPlayerY());
  }

  public void appendPlayerInventory(PlayerInventory playerInventory, int x, int y) {
    int height = 0;
    while (height < 4) {
      for (int width = 0; width < 9; ++width) {
        if (height == 3) {
          this.addSlot(new Slot(playerInventory, width, width * 18 + x, height * 18 + 4 + y));
          continue;
        }
        this.addSlot(
            new Slot(playerInventory, width + height * 9 + 9, width * 18 + x, height * 18 + y));
      }
      ++height;
    }
  }

  public void appendBackpackInventory(int x, int y) {
    int height = 0;
    while (height < this.backpack.getInventoryHeight()) {
      for (int width = 0; width < this.backpack.getInventoryWidth(); ++width) {
        this.addSlot(
            new Slot(this.backpackInventory, width + height * this.backpack.getInventoryWidth(),
                width * 18 + x, height * 18 + y));
      }
      ++height;
    }
  }

  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    Slot tmpSlot =
        slotId >= 0 && slotId < this.inventorySlots.size() ? this.inventorySlots.get(slotId) : null;
    if (tmpSlot != null && tmpSlot.inventory == player.inventory
        && tmpSlot.getSlotIndex() == player.inventory.currentItem) {
      return tmpSlot.getStack();
    }
      if (clickTypeIn != ClickType.SWAP) {
          return super.slotClick(slotId, dragType, clickTypeIn, player);
      }
    ItemStack stack = player.inventory.getStackInSlot(dragType);
      if (stack != player.inventory.getItemStack()) {
          return super.slotClick(slotId, dragType, clickTypeIn, player);
      }
    return ItemStack.EMPTY;
  }

  @Override
  public void onContainerClosed(PlayerEntity playerIn) {
    super.onContainerClosed(playerIn);
      if (!(this.backpackInventory instanceof BackpackInventory)) {
          return;
      }
    ((BackpackInventory) this.backpackInventory).writeItemStack();
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }

  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);
      if (slot == null) {
          return itemstack;
      }
      if (!slot.getHasStack()) {
          return itemstack;
      }
    ItemStack itemstack1 = slot.getStack();
    itemstack = itemstack1.copy();
    if (index < this.backpack.getInventorySize() ? !this
        .mergeItemStack(itemstack1, this.backpack.getInventorySize(), this.inventorySlots.size(),
            true) : !this.mergeItemStack(itemstack1, 0, this.backpack.getInventorySize(), false)) {
      return ItemStack.EMPTY;
    }
    if (itemstack1.isEmpty()) {
      slot.putStack(ItemStack.EMPTY);
      return itemstack;
    }
    slot.onSlotChanged();
    return itemstack;
  }

  public Backpack getBackpack() {
    return backpack;
  }
}
