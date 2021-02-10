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

package com.craftingdead.core.item;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.inventory.InventorySlotType;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;

public class ModItemModelsProperties {

  public static void register() {
    ItemModelsProperties.registerGlobalProperty(new ResourceLocation("wearing"),
        (itemStack, world, entity) -> entity.getCapability(ModCapabilities.LIVING)
            .map(living -> living.getItemHandler()
                .getStackInSlot(InventorySlotType.HAT.getIndex()) == itemStack ? 1.0F : 0.0F)
            .orElse(0.0F));
  }
}
