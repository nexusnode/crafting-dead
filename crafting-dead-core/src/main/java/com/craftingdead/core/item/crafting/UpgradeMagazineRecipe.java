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

package com.craftingdead.core.item.crafting;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.magazine.IMagazine;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

public class UpgradeMagazineRecipe extends SpecialRecipe {

  public UpgradeMagazineRecipe(ResourceLocation id) {
    super(id);
  }

  @Override
  public boolean matches(CraftingInventory inventory, World world) {
    ItemStack magazineStack = ItemStack.EMPTY;
    int ironNuggetCount = 0;
    for (int i = 0; i < inventory.getContainerSize(); ++i) {
      ItemStack itemStack = inventory.getItem(i);
      boolean isSingleMagazine = itemStack.getCapability(ModCapabilities.MAGAZINE).isPresent()
          && itemStack.getCount() == 1 && i == (inventory.getContainerSize()) / 2;
      boolean isIronNuggets = Tags.Items.NUGGETS_IRON.contains(itemStack.getItem());
      if (isSingleMagazine) {
        if (!magazineStack.isEmpty()) {
          return false;
        } else {
          magazineStack = itemStack;
        }
      } else if (isIronNuggets) {
        ironNuggetCount++;
      } else if (!itemStack.isEmpty()) {
        return false;
      }
    }

    boolean isValidMagazine = magazineStack.getCapability(ModCapabilities.MAGAZINE).isPresent();
    boolean canUpgrade = ironNuggetCount >= 8 && !magazineStack
        .getCapability(ModCapabilities.MAGAZINE)
        .map(IMagazine::getNextTier)
        .map(ItemStack::new)
        .orElse(ItemStack.EMPTY)
        .isEmpty();
    return isValidMagazine && canUpgrade;
  }

  @Override
  public ItemStack assemble(CraftingInventory inventory) {
    ItemStack magazineStack = ItemStack.EMPTY;
    int ironNuggetCount = 0;
    for (int i = 0; i < inventory.getContainerSize(); ++i) {
      ItemStack itemStack = inventory.getItem(i);
      if (itemStack.getCapability(ModCapabilities.MAGAZINE).isPresent()) {
        magazineStack = itemStack;
      } else if (Tags.Items.NUGGETS_IRON.contains(itemStack.getItem())) {
        ironNuggetCount++;
      }
    }

    int tiers = ironNuggetCount / 8;

    ItemStack nextTier = magazineStack
        .getCapability(ModCapabilities.MAGAZINE)
        .map(IMagazine::getNextTier)
        .map(ItemStack::new)
        .orElse(ItemStack.EMPTY);
    int oldSize =
        magazineStack.getCapability(ModCapabilities.MAGAZINE).map(IMagazine::getSize).orElse(0);
    while (tiers > 0 && !nextTier.isEmpty()) {
      magazineStack = nextTier;
      magazineStack
          .getCapability(ModCapabilities.MAGAZINE)
          .ifPresent(magazine -> magazine.setSize(oldSize));
      tiers--;
    }

    return magazineStack.copy();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height >= 9;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return ModRecipeSerializers.UPGRADE_MAGAZINE.get();
  }
}
