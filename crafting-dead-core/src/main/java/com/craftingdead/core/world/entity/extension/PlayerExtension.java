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

package com.craftingdead.core.world.entity.extension;

import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import net.minecraft.entity.player.PlayerEntity;

public interface PlayerExtension<E extends PlayerEntity>
    extends LivingExtension<E, PlayerHandler>, PlayerHandler {

  static <E extends PlayerEntity> PlayerExtension<E> create(E entity) {
    return new PlayerExtensionImpl<>(entity);
  }

  @SuppressWarnings("unchecked")
  static <E extends PlayerEntity> PlayerExtension<E> getExpected(E livingEntity) {
    return livingEntity.getCapability(Capabilities.LIVING)
        .filter(living -> living instanceof PlayerExtension)
        .map(living -> (PlayerExtension<E>) living)
        .orElseThrow(() -> new IllegalStateException("Missing living capability"));
  }

  boolean isCombatModeEnabled();

  void setCombatModeEnabled(boolean combatModeEnabled);

  void openEquipmentMenu();

  void openStorage(ModEquipmentSlotType slotType);
}
