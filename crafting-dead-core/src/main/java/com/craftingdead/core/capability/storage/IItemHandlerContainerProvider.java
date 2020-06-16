package com.craftingdead.core.capability.storage;

import javax.annotation.Nullable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.items.IItemHandler;

@FunctionalInterface
public interface IItemHandlerContainerProvider {
  @Nullable
  Container createMenu(int windowId, PlayerInventory playerInventory, IItemHandler backpack);
}
