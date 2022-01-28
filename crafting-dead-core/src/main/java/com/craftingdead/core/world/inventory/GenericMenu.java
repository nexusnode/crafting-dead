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
import com.craftingdead.core.world.inventory.storage.Storage;
import com.craftingdead.core.world.item.gun.GunItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GenericMenu extends AbstractMenu {

  private final int rows;

  public GenericMenu(MenuType<?> menuType, int windowId,
      Inventory playerInventory, int rows,
      BiPredicate<PredicateItemHandlerSlot, ItemStack> predicate) {
    this(menuType, windowId, playerInventory, new ItemStackHandler(9 * rows), rows, predicate);
  }

  public GenericMenu(MenuType<?> type, int id,
      Inventory playerInventory, IItemHandler itemHandler, int rows,
      BiPredicate<PredicateItemHandlerSlot, ItemStack> predicate) {
    super(type, id, playerInventory, itemHandler);
    assert itemHandler.getSlots() >= rows * 9;
    this.rows = rows;

    this.addPlayerInventorySlots();

    // Contents
    for (int i = 0; i < this.rows; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlot(new PredicateItemHandlerSlot(itemHandler, j + i * 9, 8 + j * SLOT_SIZE,
            SLOT_SIZE + i * SLOT_SIZE, predicate));
      }
    }
  }

  public int getRows() {
    return this.rows;
  }

  @Override
  public int getInventoryOffset() {
    return 1 + SLOT_SIZE + (this.rows - 4) * SLOT_SIZE;
  }

  @Override
  public boolean stillValid(Player playerEntity) {
    return true;
  }

  public static GenericMenu createVest(int windowId, Inventory playerInventory,
      IItemHandler itemHandler) {
    return new GenericMenu(ModMenuTypes.VEST.get(), windowId, playerInventory,
        itemHandler, 2,
        (slot, itemStack) -> !(itemStack.getCapability(Storage.CAPABILITY).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericMenu createVest(int windowId, Inventory playerInventory) {
    return new GenericMenu(ModMenuTypes.VEST.get(), windowId, playerInventory, 2,
        (slot, itemStack) -> !(itemStack.getCapability(Storage.CAPABILITY).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }
}
