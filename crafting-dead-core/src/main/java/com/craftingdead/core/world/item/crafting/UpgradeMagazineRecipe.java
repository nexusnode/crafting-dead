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

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.world.gun.magazine.IMagazine;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
    for (int i = 0; i < inventory.getContainerSize(); ++i) {
      switch (i) {
        case 4: // Middle slot
          ItemStack itemStack = inventory.getItem(i);
          if (itemStack.getCapability(ModCapabilities.MAGAZINE)
              .map(IMagazine::getNextTier)
              .map(item -> item == Items.AIR)
              .orElse(true)) {
            return false;
          }
          break;
        default: // All other slots
          if (!Tags.Items.NUGGETS_IRON.contains(inventory.getItem(i).getItem())) {
            return false;
          }
          break;
      }
    }
    return true;
  }

  @Override
  public ItemStack assemble(CraftingInventory inventory) {
    IMagazine magazine = ModCapabilities.getExpected(
        ModCapabilities.MAGAZINE,
        inventory.getItem(4),
        IMagazine.class);

    ItemStack nextTier = magazine.getNextTier().getDefaultInstance();

    ModCapabilities.getExpected(
        ModCapabilities.MAGAZINE,
        nextTier,
        IMagazine.class).setSize(magazine.getSize());

    return nextTier;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height == 9;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return ModRecipeSerializers.UPGRADE_MAGAZINE.get();
  }
}
