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

package com.craftingdead.core.world.item.crafting;

import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.gun.magazine.Magazine;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

public class DuplicateMagazineRecipe extends SpecialRecipe {

  public DuplicateMagazineRecipe(ResourceLocation id) {
    super(id);
  }

  @Override
  public boolean matches(CraftingInventory inventory, World world) {
    for (int i = 0; i < inventory.getContainerSize(); i++) {
      switch (i) {
        case 0:
        case 2:
        case 6:
        case 8:
          if (!Tags.Items.NUGGETS_IRON.contains(inventory.getItem(i).getItem())) {
            return false;
          }
          break;
        case 4:
          if (!inventory.getItem(i).getCapability(Capabilities.MAGAZINE).isPresent()) {
            return false;
          }
          break;
        default:
          if (!inventory.getItem(i).isEmpty()) {
            return false;
          }
          break;
      }
    }
    return true;
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingInventory inventory) {
    NonNullList<ItemStack> remainingItems =
        NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);

    for (int i = 0; i < remainingItems.size(); ++i) {
      ItemStack item = inventory.getItem(i);
      if (item.getCapability(Capabilities.MAGAZINE).isPresent()) {
        remainingItems.set(i, item.copy());
      } else if (item.hasContainerItem()) {
        remainingItems.set(i, item.getContainerItem());
      }
    }

    return remainingItems;
  }

  @Override
  public ItemStack assemble(CraftingInventory inventory) {
    ItemStack result = inventory.getItem(4).copy();
    Capabilities.getOrThrow(Capabilities.MAGAZINE, result, Magazine.class).setSize(0);
    return result;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height == 9;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return ModRecipeSerializers.DUPLICATE_MAGAZINE.get();
  }
}
