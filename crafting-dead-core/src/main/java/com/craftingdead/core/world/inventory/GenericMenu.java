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

package com.craftingdead.core.world.inventory;

import java.util.function.BiPredicate;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.item.GunItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GenericMenu extends Container {

  private final int rows;

  public GenericMenu(ContainerType<?> menuType, int windowId,
      PlayerInventory playerInventory, int rows,
      BiPredicate<PredicateItemHandlerSlot, ItemStack> predicate) {
    this(menuType, windowId, playerInventory, new ItemStackHandler(9 * rows), rows, predicate);
  }

  public GenericMenu(ContainerType<?> containerType, int windowId,
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
  public boolean stillValid(PlayerEntity playerEntity) {
    return true;
  }

  @Override
  public ItemStack quickMoveStack(PlayerEntity playerEntity, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);
    if (slot != null && slot.hasItem()) {
      ItemStack itemstack1 = slot.getItem();
      itemstack = itemstack1.copy();
      if (index < this.rows * 9) {
        if (!this.moveItemStackTo(itemstack1, this.rows * 9, this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.moveItemStackTo(itemstack1, 0, this.rows * 9, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
    }

    return itemstack;
  }

  public int getRowCount() {
    return this.rows;
  }

  public static GenericMenu createVest(int windowId, PlayerInventory playerInventory,
      IItemHandler itemHandler) {
    return new GenericMenu(ModMenuTypes.VEST.get(), windowId, playerInventory,
        itemHandler, 2,
        (slot, itemStack) -> !(itemStack.getCapability(Capabilities.STORAGE).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericMenu createVest(int windowId, PlayerInventory playerInventory) {
    return new GenericMenu(ModMenuTypes.VEST.get(), windowId, playerInventory, 2,
        (slot, itemStack) -> !(itemStack.getCapability(Capabilities.STORAGE).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }
}
