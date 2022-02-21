/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.inventory.storage;

import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

public interface Storage extends MenuConstructor, IItemHandler, INBTSerializable<CompoundTag> {

  Capability<Storage> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  boolean isValidForSlot(ModEquipmentSlot slotType);

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
