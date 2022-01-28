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

import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

public interface Storage extends MenuConstructor, IItemHandler, INBTSerializable<CompoundTag> {

  Capability<Storage> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  boolean isValidForSlot(ModEquipmentSlotType slotType);

  /**
   * Whether this storage is empty or not.
   * 
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
