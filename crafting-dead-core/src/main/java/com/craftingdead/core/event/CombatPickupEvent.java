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

package com.craftingdead.core.event;

import javax.annotation.Nullable;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class CombatPickupEvent extends Event {

  private final ItemStack itemStack;
  @Nullable
  private final CombatSlot combatSlot;

  public CombatPickupEvent(ItemStack itemStack, CombatSlot combatSlot) {
    this.itemStack = itemStack;
    this.combatSlot = combatSlot;
  }

  public ItemStack getItemStack() {
    return this.itemStack;
  }

  public CombatSlot getCombatSlot() {
    return this.combatSlot;
  }
}
