package com.craftingdead.mod.capability.storage;

import com.craftingdead.mod.inventory.InventorySlotType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.items.ItemStackHandler;

public class DefaultStorage extends ItemStackHandler implements IStorage {

  private final InventorySlotType slotType;
  private final IItemHandlerContainerProvider containerProvider;

  public DefaultStorage() {
    this(1, null, (windowId, playerInventory, backpack) -> null);
  }

  public DefaultStorage(int size, InventorySlotType slotType,
      IItemHandlerContainerProvider containerProvider) {
    super(size);
    this.slotType = slotType;
    this.containerProvider = containerProvider;
  }

  @Override
  public Container createMenu(int windowId, PlayerInventory playerInventory,
      PlayerEntity playerEntity) {
    return this.containerProvider.createMenu(windowId, playerInventory, this);
  }

  @Override
  public boolean isValidForSlot(InventorySlotType slotType) {
    return slotType == this.slotType;
  }
}
