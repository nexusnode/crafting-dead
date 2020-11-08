/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.data;

import java.util.function.Consumer;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.item.ModItems;
import com.google.gson.JsonObject;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;

public class ModRecipeProvider extends RecipeProvider {

  public ModRecipeProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
    consumer.accept(new IFinishedRecipe() {

      @Override
      public JsonObject getRecipeJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", getID().toString());
        return jsonObject;
      }

      @Override
      public void serialize(JsonObject json) {

      }

      @Override
      public ResourceLocation getID() {
        return new ResourceLocation(CraftingDead.ID, "upgrade_magazine");
      }

      @Override
      public IRecipeSerializer<?> getSerializer() {
        return null;
      }

      @Nullable
      @Override
      public JsonObject getAdvancementJson() {
        return null;
      }

      @Nullable
      @Override
      public ResourceLocation getAdvancementID() {
        return null;
      }
    });

    // ================================================================================
    // Attachments
    // ================================================================================

    ShapedRecipeBuilder.shapedRecipe(ModItems.ACOG_SIGHT.get())
        .patternLine("gig")
        .patternLine("iii")
        .key('g', Items.GLASS)
        .key('i', Items.IRON_ORE)
        .addCriterion("has_glass", this.hasItem(Items.GLASS))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.RED_DOT_SIGHT.get())
        .patternLine("g  ")
        .patternLine("iii")
        .key('g', Items.GLASS)
        .key('i', Items.IRON_ORE)
        .addCriterion("has_glass", this.hasItem(Items.GLASS))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.EOTECH_SIGHT.get())
        .patternLine("gr ")
        .patternLine("iri")
        .key('g', Items.GLASS)
        .key('i', Items.IRON_ORE)
        .key('r', Items.REDSTONE)
        .addCriterion("has_redstone", this.hasItem(Items.REDSTONE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.LP_SCOPE.get())
        .patternLine("iii")
        .patternLine("g g")
        .patternLine("iii")
        .key('g', Items.GLASS)
        .key('i', Items.IRON_ORE)
        .addCriterion("has_glass", this.hasItem(Items.GLASS))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.HP_SCOPE.get())
        .patternLine("iii")
        .patternLine("grg")
        .patternLine("iii")
        .key('g', Items.GLASS)
        .key('i', Items.IRON_ORE)
        .key('r', Items.REDSTONE)
        .addCriterion("has_redstone", this.hasItem(Items.REDSTONE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.SUPPRESSOR.get())
        .patternLine("isi")
        .patternLine("isi")
        .patternLine("isi")
        .key('i', Items.IRON_ORE)
        .key('s', Items.STRING)
        .addCriterion("has_iron_ore", this.hasItem(Items.IRON_ORE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.TACTICAL_GRIP.get())
        .patternLine(" i ")
        .patternLine(" i ")
        .patternLine(" i ")
        .key('i', Items.IRON_ORE)
        .addCriterion("has_iron_ore", this.hasItem(Items.IRON_ORE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.BIPOD.get())
        .patternLine(" i ")
        .patternLine("i i")
        .patternLine("i i")
        .key('i', Items.IRON_ORE)
        .addCriterion("has_iron_ore", this.hasItem(Items.IRON_ORE))
        .build(consumer);

    // ================================================================================
    // Assault Rifles
    // ================================================================================

    ShapedRecipeBuilder.shapedRecipe(ModItems.M4A1.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.LIGHT_GRAY_DYE)
        .key('e', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_light_gray_dye", this.hasItem(Items.LIGHT_GRAY_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.SCARH.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.ORANGE_DYE)
        .key('e', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_orange_dye", this.hasItem(Items.ORANGE_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.AK47.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.BROWN_DYE)
        .key('e', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_brown_dye", this.hasItem(Items.BROWN_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.ACR.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.RED_DYE)
        .key('e', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_red_dye", this.hasItem(Items.RED_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.FNFAL.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.INK_SAC)
        .key('e', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_ink_sac", this.hasItem(Items.INK_SAC))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.HK417.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.MAGENTA_DYE)
        .key('e', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_magenta_dye", this.hasItem(Items.MAGENTA_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.MPT55.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.YELLOW_DYE)
        .key('e', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_yellow_dye", this.hasItem(Items.YELLOW_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.M1GARAND.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.LIME_DYE)
        .key('e', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_lime_dye", this.hasItem(Items.LIME_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.SPORTER22.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.BLUE_DYE)
        .key('e', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_blue_dye", this.hasItem(Items.BLUE_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.G36C.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.BLUE_DYE)
        .key('e', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_blue_dye", this.hasItem(Items.BLUE_DYE))
        .build(consumer);

    // ================================================================================
    // Machine Guns
    // ================================================================================

    ShapedRecipeBuilder.shapedRecipe(ModItems.M240B.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.MEDIUM_STOCK.get())
        .key('b', ModItems.HEAVY_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.RED_DYE)
        .key('e', ModItems.HEAVY_HANDLE.get())
        .addCriterion("has_red_dye", this.hasItem(Items.RED_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.RPK.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.MEDIUM_STOCK.get())
        .key('b', ModItems.HEAVY_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.ORANGE_DYE)
        .key('e', ModItems.HEAVY_HANDLE.get())
        .addCriterion("has_orange_dye", this.hasItem(Items.ORANGE_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.MINIGUN.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.MEDIUM_STOCK.get())
        .key('b', ModItems.HEAVY_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.LIME_DYE)
        .key('e', ModItems.HEAVY_HANDLE.get())
        .addCriterion("has_lime_dye", this.hasItem(Items.LIME_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.MK48MOD.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.MEDIUM_STOCK.get())
        .key('b', ModItems.HEAVY_BODY.get())
        .key('c', ModItems.MEDIUM_BARREL.get())
        .key('d', Items.BLUE_DYE)
        .key('e', ModItems.HEAVY_HANDLE.get())
        .addCriterion("has_blue_dye", this.hasItem(Items.BLUE_DYE))
        .build(consumer);

    // ================================================================================
    // Pistols
    // ================================================================================

    ShapedRecipeBuilder.shapedRecipe(ModItems.TASER.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.SMALL_BODY.get())
        .key('c', ModItems.SMALL_BARREL.get())
        .key('d', Items.RED_DYE)
        .key('e', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_red_dye", this.hasItem(Items.RED_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.M1911.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.SMALL_BODY.get())
        .key('c', ModItems.SMALL_BARREL.get())
        .key('d', Items.BLUE_DYE)
        .key('e', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_blue_dye", this.hasItem(Items.BLUE_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.G18.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.SMALL_BODY.get())
        .key('c', ModItems.SMALL_BARREL.get())
        .key('d', Items.LIME_DYE)
        .key('e', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_lime_dye", this.hasItem(Items.LIME_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.M9.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.SMALL_BODY.get())
        .key('c', ModItems.SMALL_BARREL.get())
        .key('d', Items.ORANGE_DYE)
        .key('e', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_orange_dye", this.hasItem(Items.ORANGE_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.DESERT_EAGLE.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.SMALL_BODY.get())
        .key('c', ModItems.SMALL_BARREL.get())
        .key('d', Items.GRAY_DYE)
        .key('e', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_gray_dye", this.hasItem(Items.GRAY_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.P250.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.SMALL_BODY.get())
        .key('c', ModItems.SMALL_BARREL.get())
        .key('d', Items.BROWN_DYE)
        .key('e', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_brown_dye", this.hasItem(Items.BROWN_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.MAGNUM.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.SMALL_BODY.get())
        .key('c', ModItems.SMALL_BARREL.get())
        .key('d', Items.LIGHT_GRAY_DYE)
        .key('e', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_light_gray_dye", this.hasItem(Items.LIGHT_GRAY_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.FN57.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.SMALL_BODY.get())
        .key('c', ModItems.SMALL_BARREL.get())
        .key('d', Items.YELLOW_DYE)
        .key('e', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_yellow_dye", this.hasItem(Items.YELLOW_DYE))
        .build(consumer);

    // ================================================================================
    // Submachine Guns
    // ================================================================================

    ShapedRecipeBuilder.shapedRecipe(ModItems.MAC10.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.SMALL_BARREL.get())
        .key('d', Items.GRAY_DYE)
        .key('e', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_gray_dye", this.hasItem(Items.GRAY_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.P90.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.SMALL_BARREL.get())
        .key('d', Items.LIGHT_GRAY_DYE)
        .key('e', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_light_gray_dye", this.hasItem(Items.LIGHT_GRAY_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.VECTOR.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.SMALL_BARREL.get())
        .key('d', Items.BLUE_DYE)
        .key('e', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_blue_dye", this.hasItem(Items.BLUE_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.MP5A5.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.SMALL_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.SMALL_BARREL.get())
        .key('d', Items.LIME_DYE)
        .key('e', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_lime_dye", this.hasItem(Items.LIME_DYE))
        .build(consumer);

    // ================================================================================
    // Sniper Rifles
    // ================================================================================

    ShapedRecipeBuilder.shapedRecipe(ModItems.M107.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.MEDIUM_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.HEAVY_BARREL.get())
        .key('d', Items.RED_DYE)
        .key('e', ModItems.HEAVY_HANDLE.get())
        .addCriterion("has_red_dye", this.hasItem(Items.RED_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.AS50.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.MEDIUM_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.HEAVY_BARREL.get())
        .key('d', Items.LIME_DYE)
        .key('e', ModItems.HEAVY_HANDLE.get())
        .addCriterion("has_lime_dye", this.hasItem(Items.LIME_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.AWP.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.MEDIUM_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.HEAVY_BARREL.get())
        .key('d', Items.GREEN_DYE)
        .key('e', ModItems.HEAVY_HANDLE.get())
        .addCriterion("has_green_dye", this.hasItem(Items.GREEN_DYE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.DMR.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.MEDIUM_STOCK.get())
        .key('b', ModItems.MEDIUM_BODY.get())
        .key('c', ModItems.HEAVY_BARREL.get())
        .key('d', Items.LIGHT_GRAY_DYE)
        .key('e', ModItems.HEAVY_HANDLE.get())
        .addCriterion("has_light_gray_dye", this.hasItem(Items.LIGHT_GRAY_DYE))
        .build(consumer);

    // ================================================================================
    // Shotguns
    // ================================================================================

    ShapedRecipeBuilder.shapedRecipe(ModItems.TRENCHGUN.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.MEDIUM_STOCK.get())
        .key('b', ModItems.HEAVY_BODY.get())
        .key('c', ModItems.HEAVY_BARREL.get())
        .key('d', Items.OAK_PLANKS)
        .key('e', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_oak_planks", this.hasItem(Items.OAK_PLANKS))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.MOSSBERG.get())
        .patternLine(" d ")
        .patternLine("abc")
        .patternLine(" e ")
        .key('a', ModItems.MEDIUM_STOCK.get())
        .key('b', ModItems.HEAVY_BODY.get())
        .key('c', ModItems.HEAVY_BARREL.get())
        .key('d', Items.BLACK_DYE)
        .key('e', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_black_dye", this.hasItem(Items.BLACK_DYE))
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

    // ================================================================================
    // Medical
    // ================================================================================

    ShapedRecipeBuilder.shapedRecipe(ModItems.FIRST_AID_KIT.get())
        .patternLine("sss")
        .patternLine("sas")
        .patternLine("sss")
        .key('s', Items.STRING)
        .key('a', Items.APPLE)
        .addCriterion("has_apple", this.hasItem(Items.APPLE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.SYRINGE.get())
        .patternLine("gag")
        .patternLine("g g")
        .patternLine("ggg")
        .key('g', Items.GLASS)
        .key('a', Items.ARROW)
        .addCriterion("has_glass", this.hasItem(Items.GLASS))
        .build(consumer);
    ShapelessRecipeBuilder.shapelessRecipe(ModItems.BANDAGE.get())
        .addIngredient(ModItems.CLEAN_RAG.get())
        .addIngredient(ModItems.CLEAN_RAG.get())
        .addCriterion("has_clean_rag", this.hasItem(Items.APPLE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.CLEAN_RAG.get())
        .patternLine("sss")
        .patternLine("sss")
        .patternLine("sss")
        .key('s', Items.STRING)
        .addCriterion("has_string", this.hasItem(Items.STRING))
        .build(consumer);
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

    ShapedRecipeBuilder.shapedRecipe(ModItems.BOWIE_KNIFE.get())
        .patternLine(" s ")
        .patternLine("k  ")
        .key('s', Items.STICK)
        .key('k', ModItems.COMBAT_KNIFE.get())
        .addCriterion("has_combat_knife", this.hasItem(ModItems.COMBAT_KNIFE.get()))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.COMBAT_KNIFE.get())
        .patternLine("  i")
        .patternLine(" i ")
        .patternLine("s  ")
        .key('s', Items.STICK)
        .key('i', Tags.Items.INGOTS_IRON)
        .addCriterion("has_stick", this.hasItem(Items.STICK))
        .build(consumer);

    // ================================================================================
    // Miscellaneous
    // ================================================================================

    ShapedRecipeBuilder.shapedRecipe(ModItems.SMALL_BARREL.get())
        .patternLine("  i")
        .patternLine(" i ")
        .patternLine("i  ")
        .key('i', Items.IRON_INGOT)
        .addCriterion("has_iron_ingot", this.hasItem(Items.IRON_INGOT))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.SMALL_BODY.get())
        .patternLine("iii")
        .patternLine("iri")
        .patternLine("ii ")
        .key('i', Items.IRON_INGOT)
        .key('r', Items.REDSTONE)
        .addCriterion("has_redstone", this.hasItem(Items.REDSTONE))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.SMALL_HANDLE.get())
        .patternLine("iii")
        .patternLine("il ")
        .key('i', Items.IRON_INGOT)
        .key('l', Items.LEVER)
        .addCriterion("has_lever", this.hasItem(Items.LEVER))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.SMALL_STOCK.get())
        .patternLine("iii")
        .patternLine("iii")
        .patternLine("ii ")
        .key('i', Items.IRON_INGOT)
        .addCriterion("has_iron_ingot", this.hasItem(Items.IRON_INGOT))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.MEDIUM_BARREL.get())
        .patternLine("iii")
        .patternLine("ibi")
        .patternLine("iii")
        .key('i', Items.IRON_INGOT)
        .key('b', ModItems.SMALL_BARREL.get())
        .addCriterion("has_small_barrel", this.hasItem(ModItems.SMALL_BARREL.get()))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.MEDIUM_BODY.get())
        .patternLine("iii")
        .patternLine("ibi")
        .patternLine("iii")
        .key('i', Items.IRON_INGOT)
        .key('b', ModItems.SMALL_BODY.get())
        .addCriterion("has_small_body", this.hasItem(ModItems.SMALL_BODY.get()))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.MEDIUM_HANDLE.get())
        .patternLine("iii")
        .patternLine("ihi")
        .patternLine("iii")
        .key('i', Items.IRON_INGOT)
        .key('h', ModItems.SMALL_HANDLE.get())
        .addCriterion("has_small_handle", this.hasItem(ModItems.SMALL_HANDLE.get()))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.MEDIUM_STOCK.get())
        .patternLine("iii")
        .patternLine("isi")
        .patternLine("iii")
        .key('i', Items.IRON_INGOT)
        .key('s', ModItems.SMALL_STOCK.get())
        .addCriterion("has_small_stock", this.hasItem(ModItems.SMALL_STOCK.get()))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.HEAVY_BARREL.get())
        .patternLine("iii")
        .patternLine("ibi")
        .patternLine("iii")
        .key('i', Items.IRON_INGOT)
        .key('b', ModItems.MEDIUM_BARREL.get())
        .addCriterion("has_medium_barrel", this.hasItem(ModItems.MEDIUM_BARREL.get()))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.HEAVY_BODY.get())
        .patternLine("iii")
        .patternLine("ibi")
        .patternLine("iii")
        .key('i', Items.IRON_INGOT)
        .key('b', ModItems.MEDIUM_BODY.get())
        .addCriterion("has_medium_body", this.hasItem(ModItems.MEDIUM_BODY.get()))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.HEAVY_HANDLE.get())
        .patternLine("iii")
        .patternLine("ihi")
        .patternLine("iii")
        .key('i', Items.IRON_INGOT)
        .key('h', ModItems.MEDIUM_HANDLE.get())
        .addCriterion("has_medium_handle", this.hasItem(ModItems.MEDIUM_HANDLE.get()))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.MEDIUM_BOLT.get())
        .patternLine("iii")
        .patternLine("ii ")
        .key('i', Items.IRON_INGOT)
        .addCriterion("has_iron_ingot", this.hasItem(Items.IRON_INGOT))
        .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(ModItems.HEAVY_BOLT.get())
        .patternLine("iii")
        .patternLine("ibi")
        .patternLine("iii")
        .key('i', Items.IRON_INGOT)
        .key('b', ModItems.MEDIUM_BOLT.get())
        .addCriterion("has_medium_bolt", this.hasItem(ModItems.MEDIUM_BOLT.get()))
        .build(consumer);
  }

  @Override
  public String getName() {
    return "Crafting Dead Recipes";
  }
}
