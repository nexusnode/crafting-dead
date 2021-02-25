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

package com.craftingdead.core.inventory;

import java.util.Optional;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.combatitem.ICombatItem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public enum CombatSlotType {

  PRIMARY(true), SECONDARY(true), MELEE(false), GRENADE(false) {
    @Override
    protected int getAvailableSlot(PlayerInventory playerInventory, boolean ignoreEmpty) {
      int index = super.getAvailableSlot(playerInventory, false);
      return index == -1 ? 3 : index;
    }
  },
  EXTRA(false);

  private final boolean dropExistingItems;

  private CombatSlotType(boolean dropExistingItems) {
    this.dropExistingItems = dropExistingItems;
  }

  protected int getAvailableSlot(PlayerInventory playerInventory, boolean ignoreEmpty) {
    for (int i = 0; i < 6; i++) {
      if ((ignoreEmpty || playerInventory.getStackInSlot(i).isEmpty()) && getSlotType(i) == this) {
        return i;
      }
    }
    return -1;
  }

  public boolean addToInventory(ItemStack itemStack, PlayerInventory playerInventory,
      boolean ignoreEmpty) {
    int index = this.getAvailableSlot(playerInventory, ignoreEmpty);
    if (index == -1) {
      return false;
    }
    if (this.dropExistingItems && !playerInventory.getStackInSlot(index).isEmpty()) {
      ItemStack oldStack = playerInventory.removeStackFromSlot(index);
      playerInventory.player.dropItem(oldStack, true, true);
    }
    playerInventory.setInventorySlotContents(index, itemStack);
    return true;
  }

  public static Optional<CombatSlotType> getSlotType(ItemStack itemStack) {
    return itemStack.getCapability(ModCapabilities.COMBAT_ITEM)
        .map(ICombatItem::getSlotType);
  }

  public boolean isItemValid(ItemStack itemStack) {
    return itemStack.isEmpty() || getSlotType(itemStack)
        .map(this::equals)
        .orElse(false);
  }

  public static boolean isInventoryValid(PlayerInventory inventory) {
    for (int i = 0; i < 7; i++) {
      if (!CombatSlotType
          .isItemValidForSlot(inventory.getStackInSlot(i), i)) {
        return false;
      }
    }
    return true;
  }

  public static boolean isItemValidForSlot(ItemStack itemStack, int slot) {
    return getSlotType(slot).isItemValid(itemStack);
  }

  public static CombatSlotType getSlotType(int slot) {
    switch (slot) {
      case 0:
        return PRIMARY;
      case 1:
        return SECONDARY;
      case 2:
        return MELEE;
      case 3:
      case 4:
      case 5:
        return GRENADE;
      case 6:
        return EXTRA;
      default:
        throw new IllegalArgumentException("Invalid slot");
    }
  }
}
