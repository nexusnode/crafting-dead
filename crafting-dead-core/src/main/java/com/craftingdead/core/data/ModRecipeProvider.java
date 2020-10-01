/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core.data;

import java.util.function.Consumer;
import com.craftingdead.core.item.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

public class ModRecipeProvider extends RecipeProvider {

  public ModRecipeProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
    // ================================================================================
    // Medical
    // ================================================================================

    ShapedRecipeBuilder.shapedRecipe(ModItems.SPLINT.get())
        .patternLine("rs ")
        .patternLine("srs")
        .patternLine(" sr")
        .key('s', Items.STICK)
        .key('r', Ingredient.fromItems(ModItems.CLEAN_RAG.get(), ModItems.DIRTY_RAG.get()))
        .addCriterion("has_stick", this.hasItem(Items.STICK))
        .build(consumer);

    // ================================================================================
    // Melee Weapons
    // ================================================================================

    ShapedRecipeBuilder.shapedRecipe(ModItems.COMBAT_KNIFE.get())
        .patternLine("  i")
        .patternLine(" i ")
        .patternLine("s  ")
        .key('s', Items.STICK)
        .key('i', Tags.Items.INGOTS_IRON)
        .addCriterion("has_stick", this.hasItem(Items.STICK))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.BOWIE_KNIFE.get())
        .patternLine(" s ")
        .patternLine("k  ")
        .key('s', Items.STICK)
        .key('k', ModItems.COMBAT_KNIFE.get())
        .addCriterion("has_combat_knife", this.hasItem(ModItems.COMBAT_KNIFE.get()))
        .build(consumer);

    // ================================================================================
    // Grenades
    // ================================================================================

    ShapedRecipeBuilder.shapedRecipe(ModItems.FIRE_GRENADE.get())
        .patternLine(" i ")
        .patternLine("ifi")
        .patternLine(" i ")
        .key('i', Tags.Items.INGOTS_IRON)
        .key('f', Items.FIRE_CHARGE)
        .addCriterion("has_fire_charge", this.hasItem(Items.FIRE_CHARGE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.SMOKE_GRENADE.get())
        .patternLine("wiw")
        .patternLine("igi")
        .patternLine("wiw")
        .key('i', Tags.Items.INGOTS_IRON)
        .key('w', ItemTags.WOOL)
        .key('g', Tags.Items.GUNPOWDER)
        .addCriterion("has_gunpowder", this.hasItem(Tags.Items.GUNPOWDER))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.FLASH_GRENADE.get())
        .patternLine("eie")
        .patternLine("igi")
        .patternLine("eie")
        .key('i', Tags.Items.INGOTS_IRON)
        .key('e', Items.FERMENTED_SPIDER_EYE)
        .key('g', Tags.Items.GUNPOWDER)
        .addCriterion("has_fermented_spider_eye", this.hasItem(Items.FERMENTED_SPIDER_EYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.DECOY_GRENADE.get())
        .patternLine("nin")
        .patternLine("igi")
        .patternLine("nin")
        .key('i', Tags.Items.INGOTS_IRON)
        .key('n', Items.NOTE_BLOCK)
        .key('g', Tags.Items.GUNPOWDER)
        .addCriterion("has_note_block", this.hasItem(Items.NOTE_BLOCK))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.FRAG_GRENADE.get())
        .patternLine(" i ")
        .patternLine("igi")
        .patternLine(" i ")
        .key('i', Tags.Items.INGOTS_IRON)
        .key('g', Tags.Items.GUNPOWDER)
        .addCriterion("has_gunpowder", this.hasItem(Tags.Items.GUNPOWDER))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.PIPE_GRENADE.get())
        .patternLine("bib")
        .patternLine("igi")
        .patternLine("bib")
        .key('i', Tags.Items.INGOTS_IRON)
        .key('b', ModItems.BLOODY_RAG.get())
        .key('g', Tags.Items.GUNPOWDER)
        .addCriterion("has_bloody_rag", this.hasItem(ModItems.BLOODY_RAG.get()))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.C4.get())
        .patternLine(" i ")
        .patternLine("iti")
        .patternLine(" i ")
        .key('i', Tags.Items.INGOTS_IRON)
        .key('t', Items.TNT)
        .addCriterion("has_tnt", this.hasItem(Items.TNT))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.REMOTE_DETONATOR.get())
        .patternLine(" i ")
        .patternLine("iri")
        .patternLine("iii")
        .key('i', Tags.Items.INGOTS_IRON)
        .key('r', Tags.Items.DUSTS_REDSTONE)
        .addCriterion("has_redstone", this.hasItem(Tags.Items.DUSTS_REDSTONE))
        .build(consumer);
  }

  @Override
  public String getName() {
    return "Crafting Dead Recipes";
  }
}
