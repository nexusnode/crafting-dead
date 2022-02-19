/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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
      Inventory inventory, int rows, BiPredicate<PredicateItemHandlerSlot, ItemStack> predicate) {
    this(menuType, windowId, inventory, new ItemStackHandler(9 * rows), rows, predicate);
  }

  public GenericMenu(MenuType<?> type, int id,
      Inventory inventory, IItemHandler itemHandler, int rows,
      BiPredicate<PredicateItemHandlerSlot, ItemStack> predicate) {
    super(type, id, inventory, itemHandler);
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
  public boolean stillValid(Player player) {
    return true;
  }

  public static GenericMenu createVest(int windowId, Inventory inventory,
      IItemHandler itemHandler) {
    return new GenericMenu(ModMenuTypes.VEST.get(), windowId, inventory,
        itemHandler, 2,
        (slot, itemStack) -> !(itemStack.getCapability(Storage.CAPABILITY).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericMenu createVest(int windowId, Inventory inventory) {
    return new GenericMenu(ModMenuTypes.VEST.get(), windowId, inventory, 2,
        (slot, itemStack) -> !(itemStack.getCapability(Storage.CAPABILITY).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericMenu createSmallBackpack(int windowId, Inventory inventory,
      IItemHandler itemHandler) {
    return new GenericMenu(ModMenuTypes.SMALL_BACKPACK.get(), windowId, inventory,
        itemHandler, 2,
        (slot, itemStack) -> !(itemStack.getCapability(Storage.CAPABILITY).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericMenu createSmallBackpack(int windowId,
      Inventory inventory) {
    return new GenericMenu(ModMenuTypes.SMALL_BACKPACK.get(), windowId, inventory,
        2, (slot, itemStack) -> !(itemStack.getCapability(Storage.CAPABILITY).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericMenu createMediumBackpack(int windowId, Inventory inventory,
      IItemHandler itemHandler) {
    return new GenericMenu(ModMenuTypes.MEDIUM_BACKPACK.get(), windowId, inventory,
        itemHandler, 4,
        (slot, itemStack) -> !(itemStack.getCapability(Storage.CAPABILITY).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericMenu createMediumBackpack(int windowId,
      Inventory inventory) {
    return new GenericMenu(ModMenuTypes.MEDIUM_BACKPACK.get(), windowId, inventory,
        4, (slot, itemStack) -> !(itemStack.getCapability(Storage.CAPABILITY).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericMenu createLargeBackpack(int windowId, Inventory inventory,
      IItemHandler itemHandler) {
    return new GenericMenu(ModMenuTypes.LARGE_BACKPACK.get(), windowId, inventory,
        itemHandler, 6,
        (slot, itemStack) -> !(itemStack.getCapability(Storage.CAPABILITY).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericMenu createLargeBackpack(int windowId,
      Inventory inventory) {
    return new GenericMenu(ModMenuTypes.LARGE_BACKPACK.get(), windowId, inventory,
        6, (slot, itemStack) -> !(itemStack.getCapability(Storage.CAPABILITY).isPresent()
            || itemStack.getItem() instanceof GunItem));
  }

  public static GenericMenu createGunBag(int windowId, Inventory inventory,
      IItemHandler itemHandler) {
    return new GenericMenu(ModMenuTypes.GUN_BAG.get(), windowId, inventory,
        itemHandler, 4,
        (slot, itemStack) -> !(itemStack.getCapability(Storage.CAPABILITY).isPresent()));
  }

  public static GenericMenu createGunBag(int windowId, Inventory inventory) {
    return new GenericMenu(ModMenuTypes.GUN_BAG.get(), windowId, inventory, 4,
        (slot, itemStack) -> !(itemStack.getCapability(Storage.CAPABILITY).isPresent()));
  }
}
