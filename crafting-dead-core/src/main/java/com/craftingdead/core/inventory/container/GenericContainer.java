package com.craftingdead.core.inventory.container;

import java.util.function.BiPredicate;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.item.GunItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GenericContainer extends Container {

  private final int rows;

  public GenericContainer(ContainerType<?> containerType, int windowId,
      PlayerInventory playerInventory, int rows,
      BiPredicate<PredicateItemHandlerSlot, ItemStack> predicate) {
    this(containerType, windowId, playerInventory, new ItemStackHandler(9 * rows), rows, predicate);
  }

  public GenericContainer(ContainerType<?> containerType, int windowId,
      PlayerInventory playerInventory, IItemHandler itemHandler, int rows,
      BiPredicate<PredicateItemHandlerSlot, ItemStack> predicate) {
    super(containerType, windowId);
    assert itemHandler.getSlots() >= rows * 9;
    this.rows = rows;

    int i = (this.rows - 4) * 18;

    // Container inventory
    for (int j = 0; j < this.rows; ++j) {
      for (int k = 0; k < 9; ++k) {
        this
            .addSlot(new PredicateItemHandlerSlot(itemHandler, k + j * 9, 8 + k * 18, 18 + j * 18,
                predicate));
      }
    }

    // Player inventory
    for (int l = 0; l < 3; ++l) {
      for (int j1 = 0; j1 < 9; ++j1) {
        this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
      }
    }

    // Hot bar
    for (int i1 = 0; i1 < 9; ++i1) {
      this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
    }
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerEntity) {
    return true;
  }

  @Override
  public ItemStack transferStackInSlot(PlayerEntity playerEntity, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (index < this.rows * 9) {
        if (!this.mergeItemStack(itemstack1, this.rows * 9, this.inventorySlots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.mergeItemStack(itemstack1, 0, this.rows * 9, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }
    }

    return itemstack;
  }

  public int getRowCount() {
    return this.rows;
  }

  public static GenericContainer createVest(int windowId, PlayerInventory playerInventory,
      IItemHandler itemHandler) {
    return new GenericContainer(ModContainerTypes.VEST.get(), windowId, playerInventory,
        itemHandler, 2,
        (slot, itemStack) -> !(itemStack.getCapability(ModCapabilities.STORAGE).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericContainer createVest(int windowId, PlayerInventory playerInventory) {
    return new GenericContainer(ModContainerTypes.VEST.get(), windowId, playerInventory, 2,
        (slot, itemStack) -> !(itemStack.getCapability(ModCapabilities.STORAGE).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericContainer createSmallBackpack(int windowId, PlayerInventory playerInventory,
      IItemHandler itemHandler) {
    return new GenericContainer(ModContainerTypes.SMALL_BACKPACK.get(), windowId, playerInventory,
        itemHandler, 2,
        (slot, itemStack) -> !(itemStack.getCapability(ModCapabilities.STORAGE).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericContainer createSmallBackpack(int windowId,
      PlayerInventory playerInventory) {
    return new GenericContainer(ModContainerTypes.SMALL_BACKPACK.get(), windowId, playerInventory,
        2, (slot, itemStack) -> !(itemStack.getCapability(ModCapabilities.STORAGE).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericContainer createMediumBackpack(int windowId, PlayerInventory playerInventory,
      IItemHandler itemHandler) {
    return new GenericContainer(ModContainerTypes.MEDIUM_BACKPACK.get(), windowId, playerInventory,
        itemHandler, 4,
        (slot, itemStack) -> !(itemStack.getCapability(ModCapabilities.STORAGE).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericContainer createMediumBackpack(int windowId,
      PlayerInventory playerInventory) {
    return new GenericContainer(ModContainerTypes.MEDIUM_BACKPACK.get(), windowId, playerInventory,
        4, (slot, itemStack) -> !(itemStack.getCapability(ModCapabilities.STORAGE).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericContainer createLargeBackpack(int windowId, PlayerInventory playerInventory,
      IItemHandler itemHandler) {
    return new GenericContainer(ModContainerTypes.LARGE_BACKPACK.get(), windowId, playerInventory,
        itemHandler, 6,
        (slot, itemStack) -> !(itemStack.getCapability(ModCapabilities.STORAGE).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericContainer createLargeBackpack(int windowId,
      PlayerInventory playerInventory) {
    return new GenericContainer(ModContainerTypes.LARGE_BACKPACK.get(), windowId, playerInventory,
        6, (slot, itemStack) -> !(itemStack.getCapability(ModCapabilities.STORAGE).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericContainer createGunBag(int windowId, PlayerInventory playerInventory,
      IItemHandler itemHandler) {
    return new GenericContainer(ModContainerTypes.GUN_BAG.get(), windowId, playerInventory,
        itemHandler, 4,
        (slot, itemStack) -> !(itemStack.getCapability(ModCapabilities.STORAGE).isPresent()));
  }

  public static GenericContainer createGunBag(int windowId, PlayerInventory playerInventory) {
    return new GenericContainer(ModContainerTypes.GUN_BAG.get(), windowId, playerInventory, 4,
        (slot, itemStack) -> !(itemStack.getCapability(ModCapabilities.STORAGE).isPresent()));
  }

  public static GenericContainer createQuiver(int windowId, PlayerInventory playerInventory,
      IItemHandler itemHandler) {
    return new GenericContainer(ModContainerTypes.QUIVER.get(), windowId, playerInventory,
        itemHandler, 6, (slot, itemStack) -> itemStack.getItem() instanceof ArrowItem);
  }

  public static GenericContainer createQuiver(int windowId, PlayerInventory playerInventory) {
    return new GenericContainer(ModContainerTypes.QUIVER.get(), windowId, playerInventory, 6,
        (slot, itemStack) -> itemStack.getItem() instanceof ArrowItem);
  }
}
