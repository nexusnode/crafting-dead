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

package com.craftingdead.core.inventory.container;

import java.util.function.BiPredicate;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class PredicateSlot extends Slot {

  private final BiPredicate<PredicateSlot, ItemStack> predicate;

  public PredicateSlot(IInventory inventory, int index, int xPosition, int yPosition,
      BiPredicate<PredicateSlot, ItemStack> predicate) {
    super(inventory, index, xPosition, yPosition);
    this.predicate = predicate;
  }

  @Override
  public boolean mayPlace(ItemStack itemStack) {
    return this.predicate.test(this, itemStack);
  }
}
