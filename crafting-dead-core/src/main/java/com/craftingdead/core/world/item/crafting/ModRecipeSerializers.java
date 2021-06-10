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

import com.craftingdead.core.CraftingDead;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeSerializers {

  public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS =
      DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CraftingDead.ID);

  public static final RegistryObject<IRecipeSerializer<?>> UPGRADE_MAGAZINE =
      RECIPE_SERIALIZERS.register("upgrade_magazine",
          UpgradeMagazineRecipe.Serializer::new);

  public static final RegistryObject<SpecialRecipeSerializer<?>> DUPLICATE_MAGAZINE =
      RECIPE_SERIALIZERS.register("duplicate_magazine",
          () -> new SpecialRecipeSerializer<>(DuplicateMagazineRecipe::new));
}
