package com.craftingdead.core.world.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;

public class ItemUtils {

  /**
   * Stores the provided inventory to the provided compound under the given name
   *
   * @param compound the compound to save the inventory to
   * @param name the name of the tag list in the compound
   * @param inventory the inventory
   */
  public static void saveInventory(CompoundNBT compound, String name, IInventory inventory) {
    ListNBT tagList = new ListNBT();
    for (int i = 0; i < inventory.getContainerSize(); i++) {
      if (!inventory.getItem(i).isEmpty()) {
        CompoundNBT slot = new CompoundNBT();
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
  public static void saveInventory(CompoundNBT compound, String name,
      NonNullList<ItemStack> inventory) {
    ListNBT tagList = new ListNBT();
    for (int i = 0; i < inventory.size(); i++) {
      if (!inventory.get(i).isEmpty()) {
        CompoundNBT slot = new CompoundNBT();
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
  public static void saveItemList(CompoundNBT compound, String name, NonNullList<ItemStack> list) {
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
  public static void saveItemList(CompoundNBT compound, String name, NonNullList<ItemStack> list,
      boolean includeEmpty) {
    ListNBT itemList = new ListNBT();
    for (ItemStack stack : list) {
      if (!includeEmpty && stack.isEmpty()) {
        continue;
      }
      itemList.add(stack.save(new CompoundNBT()));
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
  public static void readInventory(CompoundNBT compound, String name, IInventory inv) {
    if (!compound.contains(name)) {
      return;
    }
    ListNBT tagList = compound.getList(name, 10);
    for (int i = 0; i < tagList.size(); i++) {
      CompoundNBT slot = tagList.getCompound(i);
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
  public static void readInventory(CompoundNBT compound, String name,
      NonNullList<ItemStack> inventory) {
    if (!compound.contains(name)) {
      return;
    }

    ListNBT tagList = compound.getList(name, 10);

    for (int i = 0; i < tagList.size(); i++) {
      CompoundNBT slot = tagList.getCompound(i);
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
  public static NonNullList<ItemStack> readItemList(CompoundNBT compound, String name,
      boolean includeEmpty) {
    NonNullList<ItemStack> items = NonNullList.create();
    if (!compound.contains(name)) {
      return items;
    }

    ListNBT itemList = compound.getList(name, 10);
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
  public static NonNullList<ItemStack> readItemList(CompoundNBT compound, String name) {
    return readItemList(compound, name, true);
  }

  /**
   * Reads the compound into the item list Only fills the list to its maximum capacity
   *
   * @param compound the compound to read the item list from
   * @param name the name of the tag list in the compound
   * @param list the item list
   */
  public static void readItemList(CompoundNBT compound, String name, NonNullList<ItemStack> list) {
    if (!compound.contains(name)) {
      return;
    }

    ListNBT itemList = compound.getList(name, 10);
    for (int i = 0; i < itemList.size(); i++) {
      if (i >= list.size()) {
        break;
      }
      list.set(i, ItemStack.of(itemList.getCompound(i)));
    }
  }
}
