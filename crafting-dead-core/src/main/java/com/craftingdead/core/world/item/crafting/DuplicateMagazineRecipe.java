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

import com.craftingdead.core.world.item.gun.magazine.Magazine;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

public class DuplicateMagazineRecipe extends CustomRecipe {

  public DuplicateMagazineRecipe(ResourceLocation id) {
    super(id);
  }

  @Override
  public boolean matches(CraftingContainer inventory, Level world) {
    for (int i = 0; i < inventory.getContainerSize(); i++) {
      switch (i) {
        case 0:
        case 2:
        case 6:
        case 8:
          if (!Tags.Items.INGOTS_IRON.contains(inventory.getItem(i).getItem())) {
            return false;
          }
          break;
        case 4:
          if (!inventory.getItem(i).getCapability(Magazine.CAPABILITY).isPresent()) {
            return false;
          }
          break;
        default:
          if (!Tags.Items.INGOTS_IRON.contains(inventory.getItem(i).getItem())) {
            return false;
          }
          break;
      }
    }
    return true;
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingContainer inventory) {
    NonNullList<ItemStack> remainingItems =
        NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);

    for (int i = 0; i < remainingItems.size(); ++i) {
      ItemStack item = inventory.getItem(i);
      if (item.getCapability(Magazine.CAPABILITY).isPresent()) {
        remainingItems.set(i, item.copy());
      } else if (item.hasContainerItem()) {
        remainingItems.set(i, item.getContainerItem());
      }
    }

    return remainingItems;
  }

  @Override
  public ItemStack assemble(CraftingContainer inventory) {
    ItemStack result = inventory.getItem(4).copy();
    // Sometimes this isn't present for some reason...
    result.getCapability(Magazine.CAPABILITY).ifPresent(magazine -> magazine.setSize(0));
    return result;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height == 9;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModRecipeSerializers.DUPLICATE_MAGAZINE.get();
  }
}
