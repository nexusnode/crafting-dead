package com.craftingdead.mod.capability.storage;

import com.craftingdead.mod.inventory.InventorySlotType;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

public interface IStorage extends IContainerProvider, IItemHandler, INBTSerializable<CompoundNBT> {

  boolean isValidForSlot(InventorySlotType slotType);

  /**
   * Whether this storage is empty or not.
   * @return <code>true</code> if it is empty, <code>false</code> otherwise.
   */
  default boolean isEmpty() {
    for (int i = 0; i < this.getSlots(); i++) {
      if (!this.getStackInSlot(i).isEmpty()) {
        return false;
      }
    }
    return true;
  }
}
