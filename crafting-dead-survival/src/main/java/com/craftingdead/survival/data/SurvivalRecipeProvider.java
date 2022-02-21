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
  }

  @Override
  public String getName() {
    return "Crafting Dead Survival Recipes";
  }
}
