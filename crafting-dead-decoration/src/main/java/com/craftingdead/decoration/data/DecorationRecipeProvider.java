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

package com.craftingdead.decoration.data;

import java.util.Objects;
import java.util.function.Consumer;
import com.craftingdead.decoration.world.item.DecorationItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

public class DecorationRecipeProvider extends RecipeProvider {

  public DecorationRecipeProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(DecorationItems.CONCRETE_BARRIER_ITEM.get(), 5)
        .pattern(" # ")
        .pattern(" # ")
        .pattern("###")
        .define('#', Items.GRAY_CONCRETE)
        .unlockedBy("has_item", has(Items.GRAY_CONCRETE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(DecorationItems.ROAD_BARRIER_ITEM.get(), 3)
        .pattern(" # ")
        .pattern("i i")
        .define('#', Items.RED_WOOL)
        .define('i', Items.IRON_INGOT)
        .unlockedBy("has_item", has(Items.IRON_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(DecorationItems.ROAD_BLANK_SLAB_ITEM.get(), 6)
        .pattern("###")
        .define('#', DecorationItems.ROAD_BLANK_ITEM.get())
        .unlockedBy("has_item", has(DecorationItems.ROAD_BLANK_ITEM.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(DecorationItems.ROAD_BROKEN_ITEM.get(), 8)
        .pattern("###")
        .pattern("#i#")
        .pattern("###")
        .define('#', DecorationItems.ROAD_BLANK_ITEM.get())
        .define('i', Tags.Items.DYES_WHITE)
        .unlockedBy("has_item", has(DecorationItems.ROAD_BLANK_ITEM.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(DecorationItems.ROAD_BROKEN_SLAB_ITEM.get(), 6)
        .pattern("###")
        .define('#', DecorationItems.ROAD_BROKEN_ITEM.get())
        .unlockedBy("has_item", has(DecorationItems.ROAD_BLANK_ITEM.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(DecorationItems.ROAD_SOLID_ITEM.get(), 7)
        .pattern("###")
        .pattern("i#i")
        .pattern("###")
        .define('#', DecorationItems.ROAD_BLANK_ITEM.get())
        .define('i', Tags.Items.DYES_WHITE)
        .unlockedBy("has_item", has(DecorationItems.ROAD_BLANK_ITEM.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(DecorationItems.ROAD_SOLID_SLAB_ITEM.get(), 6)
        .pattern("###")
        .define('#', DecorationItems.ROAD_SOLID_ITEM.get())
        .unlockedBy("has_item", has(DecorationItems.ROAD_BLANK_ITEM.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(DecorationItems.SANDBAG_ITEM.get(), 5)
        .pattern("#i#")
        .pattern(" # ").define('#', Items.STRING)
        .define('i', Items.SAND)
        .unlockedBy("has_item", has(Items.SAND))
        .save(consumer);
    ShapedRecipeBuilder.shaped(DecorationItems.SANDBAG_SLAB_ITEM.get(), 6)
        .pattern("###")
        .define('#', DecorationItems.SANDBAG_ITEM.get())
        .unlockedBy("has_item", has(DecorationItems.SANDBAG_ITEM.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(DecorationItems.STEEL_POLE_BARRIER_ITEM.get(), 3)
        .pattern("#i#")
        .pattern("# #").define('#', Items.IRON_INGOT)
        .define('i', Items.IRON_BARS)
        .unlockedBy("has_item", has(Items.IRON_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(DecorationItems.STOP_SIGN_ITEM.get()).pattern("#")
        .pattern("i")
        .pattern("i").define('#', Items.RED_CONCRETE)
        .define('i', Items.IRON_BARS)
        .unlockedBy("has_item", has(Items.IRON_BARS))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(DecorationItems.CONCRETE_BARRIER_SLAB_ITEM.get())
        .requires(DecorationItems.CONCRETE_BARRIER_ITEM.get())
        .requires(DecorationItems.ROAD_BLANK_SLAB_ITEM.get())
        .unlockedBy("has_item", has(DecorationItems.CONCRETE_BARRIER_ITEM.get()))
        .save(consumer);
    ShapelessRecipeBuilder.shapeless(DecorationItems.POLE_BARRIER_ITEM.get())
        .requires(DecorationItems.POLE_BARRIER_UNLIT_ITEM.get())
        .requires(Items.REDSTONE_TORCH)
        .unlockedBy("has_item", has(DecorationItems.POLE_BARRIER_UNLIT_ITEM.get()))
        .save(consumer);
    ShapelessRecipeBuilder.shapeless(DecorationItems.POLE_BARRIER_UNLIT_ITEM.get())
        .requires(DecorationItems.STEEL_POLE_BARRIER_ITEM.get())
        .requires(Items.RED_WOOL)
        .unlockedBy("has_item", has(DecorationItems.STEEL_POLE_BARRIER_ITEM.get()))
        .save(consumer);
    ShapelessRecipeBuilder.shapeless(DecorationItems.ROAD_BLANK_ITEM.get(), 4)
        .requires(Items.GRAY_CONCRETE).requires(Items.GRAY_CONCRETE)
        .unlockedBy("has_item", has(Items.GRAY_CONCRETE))
        .save(consumer);
    ShapelessRecipeBuilder.shapeless(DecorationItems.STEEL_POLE_BARRIER_SLAB_ITEM.get())
        .requires(DecorationItems.STEEL_POLE_BARRIER_ITEM.get())
        .requires(DecorationItems.ROAD_BLANK_SLAB_ITEM.get())
        .unlockedBy("has_item", has(DecorationItems.ROAD_BLANK_ITEM.get()))
        .save(consumer);
    ShapelessRecipeBuilder.shapeless(DecorationItems.STRIPED_CONCRETE_BARRIER_ITEM.get())
        .requires(DecorationItems.CONCRETE_BARRIER_ITEM.get())
        .requires(Items.RED_WOOL)
        .unlockedBy("has_item", has(DecorationItems.ROAD_BLANK_ITEM.get()))
        .save(consumer);
    ShapelessRecipeBuilder.shapeless(DecorationItems.STRIPED_CONCRETE_BARRIER_SLAB_ITEM.get())
        .requires(DecorationItems.STRIPED_CONCRETE_BARRIER_ITEM.get())
        .requires(DecorationItems.ROAD_BLANK_SLAB_ITEM.get())
        .unlockedBy("has_item", has(DecorationItems.ROAD_BLANK_ITEM.get()))
        .save(consumer);

    SingleItemRecipeBuilder
        .stonecutting(Ingredient.of(DecorationItems.ROAD_BLANK_ITEM.get()),
            DecorationItems.ROAD_BLANK_SLAB_ITEM.get(), 2)
        .unlockedBy("has_item", has(DecorationItems.ROAD_BLANK_ITEM.get()))
        .save(consumer, shapeSuffix(DecorationItems.ROAD_BLANK_SLAB_ITEM.get(), "stonecutting"));
    SingleItemRecipeBuilder
        .stonecutting(Ingredient.of(DecorationItems.ROAD_BROKEN_ITEM.get()),
            DecorationItems.ROAD_BROKEN_SLAB_ITEM.get(), 2)
        .unlockedBy("has_item", has(DecorationItems.ROAD_BLANK_ITEM.get()))
        .save(consumer, shapeSuffix(DecorationItems.ROAD_BROKEN_SLAB_ITEM.get(), "stonecutting"));
    SingleItemRecipeBuilder
        .stonecutting(Ingredient.of(DecorationItems.ROAD_SOLID_ITEM.get()),
            DecorationItems.ROAD_SOLID_SLAB_ITEM.get(), 2)
        .unlockedBy("has_item", has(DecorationItems.ROAD_BLANK_ITEM.get()))
        .save(consumer, shapeSuffix(DecorationItems.ROAD_SOLID_SLAB_ITEM.get(), "stonecutting"));
  }

  private static String shapeSuffix(ItemLike path, String suffix) {
    return Objects.requireNonNull(path.asItem().getRegistryName()).toString() + '_' + suffix;
  }
}
