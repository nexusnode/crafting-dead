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
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class PredicateItemHandlerSlot extends SlotItemHandler {

  private final BiPredicate<PredicateItemHandlerSlot, ItemStack> predicate;

  public PredicateItemHandlerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition,
      BiPredicate<PredicateItemHandlerSlot, ItemStack> predicate) {
    super(itemHandler, index, xPosition, yPosition);
    this.predicate = predicate;
  }

  @Override
  public boolean mayPlace(ItemStack itemStack) {
    System.out.println(this.getSlotIndex() + " - " + itemStack.getDisplayName().getString());
    return this.predicate.test(this, itemStack);
  }
}
