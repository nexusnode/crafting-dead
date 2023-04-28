/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.survival.data;

import java.util.function.Consumer;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.survival.world.item.SurvivalItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

public class SurvivalRecipeProvider extends RecipeProvider {

  public SurvivalRecipeProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
    ShapelessRecipeBuilder.shapeless(Items.STRING, 3)
        .requires(Ingredient.of(SurvivalItems.CLEAN_RAG.get(), SurvivalItems.DIRTY_RAG.get()))
        .unlockedBy("has_clean_rag", has(SurvivalItems.CLEAN_RAG.get()))
        .unlockedBy("has_dirty_rag", has(SurvivalItems.DIRTY_RAG.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(SurvivalItems.PIPE_BOMB.get())
        .pattern("bib")
        .pattern("igi")
        .pattern("bib")
        .define('i', Tags.Items.INGOTS_IRON)
        .define('b', SurvivalItems.BLOODY_RAG.get())
        .define('g', Tags.Items.GUNPOWDER)
        .unlockedBy("has_bloody_rag", has(SurvivalItems.BLOODY_RAG.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(SurvivalItems.CLEAN_RAG.get())
        .pattern("sss")
        .pattern("sss")
        .pattern("sss")
        .define('s', Items.STRING)
        .unlockedBy("has_string", has(Items.STRING))
        .save(consumer);
    ShapedRecipeBuilder.shaped(SurvivalItems.SPLINT.get())
        .pattern("rs ")
        .pattern("srs")
        .pattern(" sr")
        .define('s', Items.STICK)
        .define('r', Ingredient.of(SurvivalItems.CLEAN_RAG.get(), SurvivalItems.DIRTY_RAG.get()))
        .unlockedBy("has_stick", has(Items.STICK))
        .save(consumer);

    // ================================================================================
    // Supply Drop Radios
    // ================================================================================

    ShapedRecipeBuilder.shaped(SurvivalItems.MILITARY_DROP_RADIO.get())
        .pattern("iti")
        .pattern("igi")
        .pattern("iii")
        .define('t', Items.REDSTONE_TORCH)
        .define('i', Items.IRON_BLOCK)
        .define('g', Items.DIAMOND)
        .unlockedBy("has_redstone_torch", has(Items.REDSTONE_TORCH))
        .save(consumer);
    ShapedRecipeBuilder.shaped(SurvivalItems.MEDICAL_DROP_RADIO.get())
        .pattern("iti")
        .pattern("ifi")
        .pattern("iii")
        .define('t', Items.REDSTONE_TORCH)
        .define('i', Items.EMERALD)
        .define('f', ModItems.FIRST_AID_KIT.get())
        .unlockedBy("has_redstone_torch", has(Items.REDSTONE_TORCH))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(SurvivalItems.OPEN_CANNED_SWEETCORN.get())
        .requires(SurvivalItems.CANNED_SWEETCORN.get())
        .requires(Ingredient.of(
            SurvivalItems.CAN_OPENER.get(),
            SurvivalItems.SCREWDRIVER.get(),
            SurvivalItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_corn", has(SurvivalItems.CANNED_SWEETCORN.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(SurvivalItems.OPEN_CANNED_BEANS.get())
        .requires(SurvivalItems.CANNED_BEANS.get())
        .requires(Ingredient.of(
            SurvivalItems.CAN_OPENER.get(),
            SurvivalItems.SCREWDRIVER.get(),
            SurvivalItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_beans", has(SurvivalItems.CANNED_BEANS.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(SurvivalItems.OPEN_CANNED_TUNA.get())
        .requires(SurvivalItems.CANNED_TUNA.get())
        .requires(Ingredient.of(
            SurvivalItems.CAN_OPENER.get(),
            SurvivalItems.SCREWDRIVER.get(),
            SurvivalItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_tuna", has(SurvivalItems.CANNED_TUNA.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(SurvivalItems.OPEN_CANNED_PEACHES.get())
        .requires(SurvivalItems.CANNED_PEACHES.get())
        .requires(Ingredient.of(
            SurvivalItems.CAN_OPENER.get(),
            SurvivalItems.SCREWDRIVER.get(),
            SurvivalItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_peaches", has(SurvivalItems.CANNED_PEACHES.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(SurvivalItems.OPEN_CANNED_PASTA.get())
        .requires(SurvivalItems.CANNED_PASTA.get())
        .requires(Ingredient.of(
            SurvivalItems.CAN_OPENER.get(),
            SurvivalItems.SCREWDRIVER.get(),
            SurvivalItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_pasta", has(SurvivalItems.CANNED_PASTA.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(SurvivalItems.OPEN_CANNED_CORNED_BEEF.get())
        .requires(SurvivalItems.CANNED_CORNED_BEEF.get())
        .requires(Ingredient.of(
            SurvivalItems.CAN_OPENER.get(),
            SurvivalItems.SCREWDRIVER.get(),
            SurvivalItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_corned_beef", has(SurvivalItems.CANNED_CORNED_BEEF.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(SurvivalItems.OPEN_CANNED_CUSTARD.get())
        .requires(SurvivalItems.CANNED_CUSTARD.get())
        .requires(Ingredient.of(
            SurvivalItems.CAN_OPENER.get(),
            SurvivalItems.SCREWDRIVER.get(),
            SurvivalItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_custard", has(SurvivalItems.CANNED_CUSTARD.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(SurvivalItems.OPEN_CANNED_PICKLES.get())
        .requires(SurvivalItems.CANNED_PICKLES.get())
        .requires(Ingredient.of(
            SurvivalItems.CAN_OPENER.get(),
            SurvivalItems.SCREWDRIVER.get(),
            SurvivalItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_pickles", has(SurvivalItems.CANNED_PICKLES.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(SurvivalItems.OPEN_CANNED_DOG_FOOD.get())
        .requires(SurvivalItems.CANNED_DOG_FOOD.get())
        .requires(Ingredient.of(
            SurvivalItems.CAN_OPENER.get(),
            SurvivalItems.SCREWDRIVER.get(),
            SurvivalItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_dog_food", has(SurvivalItems.CANNED_DOG_FOOD.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(SurvivalItems.OPEN_CANNED_TOMATO_SOUP.get())
        .requires(SurvivalItems.CANNED_TOMATO_SOUP.get())
        .requires(Ingredient.of(
            SurvivalItems.CAN_OPENER.get(),
            SurvivalItems.SCREWDRIVER.get(),
            SurvivalItems.MULTI_TOOL.get()))
        .unlockedBy("has_canned_tomato_soup", has(SurvivalItems.CANNED_TOMATO_SOUP.get()))
        .save(consumer);
  }

  @Override
  public String getName() {
    return "Crafting Dead Survival Recipes";
  }
}
