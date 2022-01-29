/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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
 */

package com.craftingdead.core.world.inventory.storage;

import javax.annotation.Nullable;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerStorage extends ItemStackHandler implements Storage {

  private final int size;
  private final ModEquipmentSlot slotType;
  private final ItemHandlerMenuConstructor containerProvider;

  public ItemStackHandlerStorage() {
    this(1, null, (windowId, inventory, itemHandler) -> null);
  }

  public ItemStackHandlerStorage(int size, ModEquipmentSlot slotType,
      ItemHandlerMenuConstructor constructor) {
    super(size);
    this.size = size;
    this.slotType = slotType;
    this.containerProvider = constructor;
  }

  @Override
  protected void onLoad() {
    if (this.getSlots() != this.size) {
      this.setSize(this.size);
    }
  }

  @Override
  public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
    return this.containerProvider.createMenu(windowId, inventory, this);
  }

  @Override
  public boolean isValidForSlot(ModEquipmentSlot slotType) {
    return slotType == this.slotType;
  }

  @FunctionalInterface
  public interface ItemHandlerMenuConstructor {

    @Nullable
    AbstractContainerMenu createMenu(int windowId, Inventory player, IItemHandler itemHandler);
  }
}
