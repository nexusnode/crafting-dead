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

package com.craftingdead.core.world.item.crafting;

import com.craftingdead.core.CraftingDead;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {

  public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
      DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CraftingDead.ID);

  public static final RegistryObject<RecipeSerializer<?>> UPGRADE_MAGAZINE =
      RECIPE_SERIALIZERS.register("upgrade_magazine",
          UpgradeMagazineRecipe.Serializer::new);

  public static final RegistryObject<SimpleRecipeSerializer<?>> DUPLICATE_MAGAZINE =
      RECIPE_SERIALIZERS.register("duplicate_magazine",
          () -> new SimpleRecipeSerializer<>(DuplicateMagazineRecipe::new));
}
