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
