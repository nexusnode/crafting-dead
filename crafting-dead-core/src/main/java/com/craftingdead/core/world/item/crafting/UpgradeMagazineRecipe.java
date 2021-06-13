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
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class UpgradeMagazineRecipe extends SpecialRecipe {

  private static final int MIDDLE_SLOT_INDEX = 4;

  private final Ingredient magazine;
  private final ItemStack nextTier;

  public UpgradeMagazineRecipe(ResourceLocation id, Ingredient magazine, ItemStack nextTier) {
    super(id);
    this.magazine = magazine;
    this.nextTier = nextTier;
  }

  @Override
  public boolean matches(CraftingInventory inventory, World world) {
    for (int i = 0; i < inventory.getContainerSize(); ++i) {
      switch (i) {
        case MIDDLE_SLOT_INDEX: // Middle slot
          if (!this.magazine.test(inventory.getItem(i))) {
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
    Magazine magazine = Capabilities.getOrThrow(
        Capabilities.MAGAZINE, inventory.getItem(MIDDLE_SLOT_INDEX), Magazine.class);

    Capabilities.getOrThrow(
        Capabilities.MAGAZINE, this.nextTier, Magazine.class).setSize(magazine.getSize());

    return this.nextTier;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height == 9;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return ModRecipeSerializers.UPGRADE_MAGAZINE.get();
  }

  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
      implements IRecipeSerializer<UpgradeMagazineRecipe> {

    @Override
    public UpgradeMagazineRecipe fromJson(ResourceLocation id, JsonObject json) {
      return new UpgradeMagazineRecipe(id, Ingredient.fromJson(json.get("magazine")),
          ShapedRecipe.itemFromJson(json.getAsJsonObject("nextTier")));
    }

    @Override
    public UpgradeMagazineRecipe fromNetwork(ResourceLocation id, PacketBuffer buf) {
      return new UpgradeMagazineRecipe(id, Ingredient.fromNetwork(buf), buf.readItem());
    }

    @Override
    public void toNetwork(PacketBuffer buf, UpgradeMagazineRecipe recipe) {
      recipe.magazine.toNetwork(buf);
      buf.writeItem(recipe.nextTier);
    }
  }
}
