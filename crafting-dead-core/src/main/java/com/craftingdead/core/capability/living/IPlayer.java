/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.capability.living;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.inventory.InventorySlotType;
import net.minecraft.entity.player.PlayerEntity;

public interface IPlayer<E extends PlayerEntity>
    extends ILiving<E, IPlayerHandler>, IPlayerHandler {

  void openInventory();

  void openStorage(InventorySlotType slotType);

  void infect(float chance);

  int getWater();

  void setWater(int water);

  int getMaxWater();

  void setMaxWater(int maxWater);

  @SuppressWarnings("unchecked")
  public static <E extends PlayerEntity> IPlayer<E> getExpected(E livingEntity) {
    return livingEntity.getCapability(ModCapabilities.LIVING)
        .filter(living -> living instanceof IPlayer)
        .map(living -> (IPlayer<E>) living)
        .orElseThrow(() -> new IllegalStateException("Missing living capability"));
  }
}
