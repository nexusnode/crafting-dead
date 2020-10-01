/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.craftingdead.core.capability.storage;

import com.craftingdead.core.inventory.InventorySlotType;
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
