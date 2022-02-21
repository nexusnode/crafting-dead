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

package com.craftingdead.core.world.item;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.NonNullList;

public class ItemUtils {

  /**
   * Stores the provided inventory to the provided compound under the given name
   *
   * @param compound the compound to save the inventory to
   * @param name the name of the tag list in the compound
   * @param inventory the inventory
   */
  public static void saveInventory(CompoundTag compound, String name, Container inventory) {
    ListTag tagList = new ListTag();
    for (int i = 0; i < inventory.getContainerSize(); i++) {
      if (!inventory.getItem(i).isEmpty()) {
        CompoundTag slot = new CompoundTag();
        slot.putInt("slot", i);
        inventory.getItem(i).save(slot);
        tagList.add(slot);
      }
    }
    compound.put(name, tagList);
  }

  /**
   * Stores the provided item list to the provided compound under the given name
   *
   * @param compound the compound to save the inventory to
   * @param name the name of the tag list in the compound
   * @param inventory the item list
   */
  public static void saveInventory(CompoundTag compound, String name,
      NonNullList<ItemStack> inventory) {
    ListTag tagList = new ListTag();
    for (int i = 0; i < inventory.size(); i++) {
      if (!inventory.get(i).isEmpty()) {
        CompoundTag slot = new CompoundTag();
        slot.putInt("slot", i);
        inventory.get(i).save(slot);
        tagList.add(slot);
      }
    }
    compound.put(name, tagList);
  }

  /**
   * Stores the provided item list to the provided compound under the given name Stores empty items
   *
   * @param compound the compound to save the inventory to
   * @param name the name of the tag list in the compound
   * @param list the item list
   */
  public static void saveItemList(CompoundTag compound, String name, NonNullList<ItemStack> list) {
    saveItemList(compound, name, list, true);
  }

  /**
   * Stores the provided item list to the provided compound under the given name Stores empty items
   *
   * @param compound the compound to save the inventory to
   * @param name the name of the tag list in the compound
   * @param list the item list
   * @param includeEmpty if empty item stacks should be included
   */
  public static void saveItemList(CompoundTag compound, String name, NonNullList<ItemStack> list,
      boolean includeEmpty) {
    ListTag itemList = new ListTag();
    for (ItemStack stack : list) {
      if (!includeEmpty && stack.isEmpty()) {
        continue;
      }
      itemList.add(stack.save(new CompoundTag()));
    }
    compound.put(name, itemList);
  }

  /**
   * Loads the provided compound to the provided inventory Does not clear inventory - empty stacks
   * will not overwrite existing items
   *
   * @param compound the compound to read the inventory from
   * @param name the name of the tag list in the compound
   * @param inv the inventory
   */
  public static void readInventory(CompoundTag compound, String name, Container inv) {
    if (!compound.contains(name)) {
      return;
    }
    ListTag tagList = compound.getList(name, 10);
    for (int i = 0; i < tagList.size(); i++) {
      CompoundTag slot = tagList.getCompound(i);
      int j = slot.getInt("slot");

      if (j >= 0 && j < inv.getContainerSize()) {
        inv.setItem(j, ItemStack.of(slot));
      }
    }
  }

  /**
   * Loads the provided compound to the provided item list Does not clear the list - empty stacks
   * will not overwrite existing items
   *
   * @param compound the compound to read the inventory from
   * @param name the name of the tag list in the compound
   * @param inventory the item list
   */
  public static void readInventory(CompoundTag compound, String name,
      NonNullList<ItemStack> inventory) {
    if (!compound.contains(name)) {
      return;
    }

    ListTag tagList = compound.getList(name, 10);

    for (int i = 0; i < tagList.size(); i++) {
      CompoundTag slot = tagList.getCompound(i);
      int j = slot.getInt("slot");

      if (j >= 0 && j < inventory.size()) {
        inventory.set(j, ItemStack.of(slot));
      }
    }
  }

  /**
   * Loads the provided compound to the provided item list
   *
   * @param compound the compound to read the item list from
   * @param name the name of the tag list in the compound
   * @param includeEmpty if empty stacks should be included
   * @return the item list
   */
  public static NonNullList<ItemStack> readItemList(CompoundTag compound, String name,
      boolean includeEmpty) {
    NonNullList<ItemStack> items = NonNullList.create();
    if (!compound.contains(name)) {
      return items;
    }

    ListTag itemList = compound.getList(name, 10);
    for (int i = 0; i < itemList.size(); i++) {
      ItemStack item = ItemStack.of(itemList.getCompound(i));
      if (!includeEmpty) {
        if (!item.isEmpty()) {
          items.add(item);
        }
      } else {
        items.add(item);
      }
    }
    return items;
  }

  /**
   * Loads the provided compound to the provided item list Inclues empty items
   *
   * @param compound the compound to read the item list from
   * @param name the name of the tag list in the compound
   * @return the item list
   */
  public static NonNullList<ItemStack> readItemList(CompoundTag compound, String name) {
    return readItemList(compound, name, true);
  }

  /**
   * Reads the compound into the item list Only fills the list to its maximum capacity
   *
   * @param compound the compound to read the item list from
   * @param name the name of the tag list in the compound
   * @param list the item list
   */
  public static void readItemList(CompoundTag compound, String name, NonNullList<ItemStack> list) {
    if (!compound.contains(name)) {
      return;
    }

    ListTag itemList = compound.getList(name, 10);
    for (int i = 0; i < itemList.size(); i++) {
      if (i >= list.size()) {
        break;
      }
      list.set(i, ItemStack.of(itemList.getCompound(i)));
    }
  }
}
