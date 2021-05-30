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

package com.craftingdead.survival.data;

import java.util.function.Consumer;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.survival.world.item.SurvivalItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

public class SurvivalRecipeProvider extends RecipeProvider {

  public SurvivalRecipeProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
    ShapelessRecipeBuilder.shapeless(Items.STRING, 3)
        .requires(Ingredient.of(SurvivalItems.CLEAN_RAG.get(), SurvivalItems.DIRTY_RAG.get()))
        .unlockedBy("has_clean_rag", has(SurvivalItems.CLEAN_RAG.get()))
        .unlockedBy("has_dirty_rag", has(SurvivalItems.DIRTY_RAG.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(SurvivalItems.PIPE_GRENADE.get())
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
        .define('i', Items.IRON_INGOT)
        .define('g', Items.GOLD_INGOT)
        .unlockedBy("has_redstone_torch", has(Items.REDSTONE_TORCH))
        .save(consumer);
    ShapedRecipeBuilder.shaped(SurvivalItems.MEDICAL_DROP_RADIO.get())
        .pattern("iti")
        .pattern("ifi")
        .pattern("iii")
        .define('t', Items.REDSTONE_TORCH)
        .define('i', Items.IRON_INGOT)
        .define('f', ModItems.FIRST_AID_KIT.get())
        .unlockedBy("has_redstone_torch", has(Items.REDSTONE_TORCH))
        .save(consumer);
  }

  @Override
  public String getName() {
    return "Crafting Dead Survival Recipes";
  }
}
