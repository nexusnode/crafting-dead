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

package com.craftingdead.immerse.data.recipes;

import java.util.function.Consumer;
import com.craftingdead.immerse.world.item.ImmerseItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;

public class ImmerseRecipeProvider extends RecipeProvider {

  public ImmerseRecipeProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
    ShapelessRecipeBuilder.shapeless(ImmerseItems.OPEN_CANNED_CORN.get())
        .requires(ImmerseItems.CANNED_CORN.get())
        .requires(Ingredient.of(
            ImmerseItems.CAN_OPENER.get(),
            ImmerseItems.SCREWDRIVER.get(),
            ImmerseItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_corn", has(ImmerseItems.CANNED_CORN.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(ImmerseItems.OPEN_CANNED_BEANS.get())
        .requires(ImmerseItems.CANNED_BEANS.get())
        .requires(Ingredient.of(
            ImmerseItems.CAN_OPENER.get(),
            ImmerseItems.SCREWDRIVER.get(),
            ImmerseItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_beans", has(ImmerseItems.CANNED_BEANS.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(ImmerseItems.OPEN_CANNED_TUNA.get())
        .requires(ImmerseItems.CANNED_TUNA.get())
        .requires(Ingredient.of(
            ImmerseItems.CAN_OPENER.get(),
            ImmerseItems.SCREWDRIVER.get(),
            ImmerseItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_tuna", has(ImmerseItems.CANNED_TUNA.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(ImmerseItems.OPEN_CANNED_PEACHES.get())
        .requires(ImmerseItems.CANNED_PEACHES.get())
        .requires(Ingredient.of(
            ImmerseItems.CAN_OPENER.get(),
            ImmerseItems.SCREWDRIVER.get(),
            ImmerseItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_peaches", has(ImmerseItems.CANNED_PEACHES.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(ImmerseItems.OPEN_CANNED_PASTA.get())
        .requires(ImmerseItems.CANNED_PASTA.get())
        .requires(Ingredient.of(
            ImmerseItems.CAN_OPENER.get(),
            ImmerseItems.SCREWDRIVER.get(),
            ImmerseItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_pasta", has(ImmerseItems.CANNED_PASTA.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(ImmerseItems.OPEN_CANNED_BACON.get())
        .requires(ImmerseItems.CANNED_BACON.get())
        .requires(Ingredient.of(
            ImmerseItems.CAN_OPENER.get(),
            ImmerseItems.SCREWDRIVER.get(),
            ImmerseItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_bacon", has(ImmerseItems.CANNED_BACON.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(ImmerseItems.OPEN_CANNED_CUSTARD.get())
        .requires(ImmerseItems.CANNED_CUSTARD.get())
        .requires(Ingredient.of(
            ImmerseItems.CAN_OPENER.get(),
            ImmerseItems.SCREWDRIVER.get(),
            ImmerseItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_custard", has(ImmerseItems.CANNED_CUSTARD.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(ImmerseItems.OPEN_CANNED_PICKLES.get())
        .requires(ImmerseItems.CANNED_PICKLES.get())
        .requires(Ingredient.of(
            ImmerseItems.CAN_OPENER.get(),
            ImmerseItems.SCREWDRIVER.get(),
            ImmerseItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_pickles", has(ImmerseItems.CANNED_PICKLES.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(ImmerseItems.OPEN_CANNED_DOG_FOOD.get())
        .requires(ImmerseItems.CANNED_DOG_FOOD.get())
        .requires(Ingredient.of(
            ImmerseItems.CAN_OPENER.get(),
            ImmerseItems.SCREWDRIVER.get(),
            ImmerseItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_dog_food", has(ImmerseItems.CANNED_DOG_FOOD.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(ImmerseItems.OPEN_CANNED_TOMATO_SOUP.get())
        .requires(ImmerseItems.CANNED_TOMATO_SOUP.get())
        .requires(Ingredient.of(
            ImmerseItems.CAN_OPENER.get(),
            ImmerseItems.SCREWDRIVER.get(),
            ImmerseItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_tomato_soup", has(ImmerseItems.CANNED_TOMATO_SOUP.get()))
        .save(consumer);
  }

  @Override
  public String getName() {
    return "Crafting Dead Immerse Recipes";
  }
}
