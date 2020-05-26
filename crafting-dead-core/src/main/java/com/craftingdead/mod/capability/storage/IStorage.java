package com.craftingdead.mod.capability.storage;

import com.craftingdead.mod.inventory.InventorySlotType;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

public interface IStorage extends IContainerProvider, IItemHandler, INBTSerializable<CompoundNBT> {

  boolean isValidForSlot(InventorySlotType slotType);
}
