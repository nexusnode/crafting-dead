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
          if (!inventory.getItem(i).is(Tags.Items.INGOTS_IRON)) {
            return false;
          }
          break;
        case 4:
          if (!inventory.getItem(i).getCapability(Magazine.CAPABILITY).isPresent()) {
            return false;
          }
          break;
        default:
          if (!inventory.getItem(i).is(Tags.Items.INGOTS_IRON)) {
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
