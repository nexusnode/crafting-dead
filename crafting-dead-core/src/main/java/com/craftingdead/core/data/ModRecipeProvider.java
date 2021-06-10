/*
 * Crafting Dead Copyright (C) 2021 NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.data;

import java.util.function.Consumer;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.tags.ModItemTags;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.crafting.ModRecipeSerializers;
import net.minecraft.data.CustomRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

public class ModRecipeProvider extends RecipeProvider {

  public ModRecipeProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
    UpgradeMagazineRecipeBuilder
        .create(Ingredient.of(ModItems.STANAG_DRUM_MAGAZINE.get()),
            ModItems.STANAG_BOX_MAGAZINE.get())
        .unlockedBy("has_stanag_drum_magazine", has(ModItems.STANAG_DRUM_MAGAZINE.get()))
        .save(consumer);

    UpgradeMagazineRecipeBuilder
        .create(Ingredient.of(ModItems.STANAG_30_ROUND_MAGAZINE.get()),
            ModItems.STANAG_DRUM_MAGAZINE.get())
        .unlockedBy("has_stanag_30_round_magazine", has(ModItems.STANAG_30_ROUND_MAGAZINE.get()))
        .save(consumer);

    UpgradeMagazineRecipeBuilder
        .create(Ingredient.of(ModItems.STANAG_20_ROUND_MAGAZINE.get()),
            ModItems.STANAG_30_ROUND_MAGAZINE.get())
        .unlockedBy("has_stanag_20_round_magazine", has(ModItems.STANAG_20_ROUND_MAGAZINE.get()))
        .save(consumer);

    UpgradeMagazineRecipeBuilder
        .create(Ingredient.of(ModItems.MP5A5_21_ROUND_MAGAZINE.get()),
            ModItems.MP5A5_35_ROUND_MAGAZINE.get())
        .unlockedBy("has_mp5a5_21_round_magazine", has(ModItems.MP5A5_21_ROUND_MAGAZINE.get()))
        .save(consumer);

    UpgradeMagazineRecipeBuilder
        .create(Ingredient.of(ModItems.MAC10_MAGAZINE.get()),
            ModItems.MAC10_EXTENDED_MAGAZINE.get())
        .unlockedBy("has_mac10_magazine", has(ModItems.MAC10_MAGAZINE.get()))
        .save(consumer);

    UpgradeMagazineRecipeBuilder
        .create(Ingredient.of(ModItems.RPK_MAGAZINE.get()),
            ModItems.RPK_DRUM_MAGAZINE.get())
        .unlockedBy("has_rpk_magazine", has(ModItems.RPK_MAGAZINE.get()))
        .save(consumer);

    CustomRecipeBuilder.special(ModRecipeSerializers.DUPLICATE_MAGAZINE.get())
        .save(consumer, CraftingDead.ID + ":duplicate_magazine");

    // ================================================================================
    // Attachments
    // ================================================================================

    ShapedRecipeBuilder.shaped(ModItems.ACOG_SIGHT.get())
        .pattern("gig")
        .pattern("iii")
        .define('g', Items.GLASS)
        .define('i', Items.IRON_ORE)
        .unlockedBy("has_glass", has(Items.GLASS))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.RED_DOT_SIGHT.get())
        .pattern("g  ")
        .pattern("iii")
        .define('g', Items.GLASS)
        .define('i', Items.IRON_ORE)
        .unlockedBy("has_glass", has(Items.GLASS))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.EOTECH_SIGHT.get())
        .pattern("gr ")
        .pattern("iri")
        .define('g', Items.GLASS)
        .define('i', Items.IRON_ORE)
        .define('r', Items.REDSTONE)
        .unlockedBy("has_redstone", has(Items.REDSTONE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.LP_SCOPE.get())
        .pattern("iii")
        .pattern("g g")
        .pattern("iii")
        .define('g', Items.GLASS)
        .define('i', Items.IRON_ORE)
        .unlockedBy("has_glass", has(Items.GLASS))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.HP_SCOPE.get())
        .pattern("iii")
        .pattern("grg")
        .pattern("iii")
        .define('g', Items.GLASS)
        .define('i', Items.IRON_ORE)
        .define('r', Items.REDSTONE)
        .unlockedBy("has_redstone", has(Items.REDSTONE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.SUPPRESSOR.get())
        .pattern("isi")
        .pattern("isi")
        .pattern("isi")
        .define('i', Items.IRON_ORE)
        .define('s', Items.STRING)
        .unlockedBy("has_iron_ore", has(Items.IRON_ORE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.TACTICAL_GRIP.get())
        .pattern(" i ")
        .pattern(" i ")
        .pattern(" i ")
        .define('i', Items.IRON_ORE)
        .unlockedBy("has_iron_ore", has(Items.IRON_ORE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.BIPOD.get())
        .pattern(" i ")
        .pattern("i i")
        .pattern("i i")
        .define('i', Items.IRON_ORE)
        .unlockedBy("has_iron_ore", has(Items.IRON_ORE))
        .save(consumer);

    // ================================================================================
    // Assault Rifles
    // ================================================================================

    ShapedRecipeBuilder.shaped(ModItems.M4A1.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.LIGHT_GRAY_DYE)
        .define('e', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_light_gray_dye", has(Items.LIGHT_GRAY_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.SCARH.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.ORANGE_DYE)
        .define('e', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_orange_dye", has(Items.ORANGE_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.AK47.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.BROWN_DYE)
        .define('e', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_brown_dye", has(Items.BROWN_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.ACR.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.RED_DYE)
        .define('e', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_red_dye", has(Items.RED_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.FNFAL.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.INK_SAC)
        .define('e', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_ink_sac", has(Items.INK_SAC))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.HK417.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.MAGENTA_DYE)
        .define('e', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_magenta_dye", has(Items.MAGENTA_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.MPT55.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.YELLOW_DYE)
        .define('e', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_yellow_dye", has(Items.YELLOW_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.M1GARAND.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.LIME_DYE)
        .define('e', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_lime_dye", has(Items.LIME_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.SPORTER22.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.BLUE_DYE)
        .define('e', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_blue_dye", has(Items.BLUE_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.G36C.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.BLUE_DYE)
        .define('e', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_blue_dye", has(Items.BLUE_DYE))
        .save(consumer);

    // ================================================================================
    // Machine Guns
    // ================================================================================

    ShapedRecipeBuilder.shaped(ModItems.M240B.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.MEDIUM_STOCK.get())
        .define('b', ModItems.HEAVY_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.RED_DYE)
        .define('e', ModItems.HEAVY_HANDLE.get())
        .unlockedBy("has_red_dye", has(Items.RED_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.RPK.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.MEDIUM_STOCK.get())
        .define('b', ModItems.HEAVY_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.ORANGE_DYE)
        .define('e', ModItems.HEAVY_HANDLE.get())
        .unlockedBy("has_orange_dye", has(Items.ORANGE_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.MINIGUN.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.MEDIUM_STOCK.get())
        .define('b', ModItems.HEAVY_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.LIME_DYE)
        .define('e', ModItems.HEAVY_HANDLE.get())
        .unlockedBy("has_lime_dye", has(Items.LIME_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.MK48MOD.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.MEDIUM_STOCK.get())
        .define('b', ModItems.HEAVY_BODY.get())
        .define('c', ModItems.MEDIUM_BARREL.get())
        .define('d', Items.BLUE_DYE)
        .define('e', ModItems.HEAVY_HANDLE.get())
        .unlockedBy("has_blue_dye", has(Items.BLUE_DYE))
        .save(consumer);

    // ================================================================================
    // Pistols
    // ================================================================================

    ShapedRecipeBuilder.shaped(ModItems.TASER.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.SMALL_BODY.get())
        .define('c', ModItems.SMALL_BARREL.get())
        .define('d', Items.RED_DYE)
        .define('e', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_red_dye", has(Items.RED_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.M1911.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.SMALL_BODY.get())
        .define('c', ModItems.SMALL_BARREL.get())
        .define('d', Items.BLUE_DYE)
        .define('e', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_blue_dye", has(Items.BLUE_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.G18.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.SMALL_BODY.get())
        .define('c', ModItems.SMALL_BARREL.get())
        .define('d', Items.LIME_DYE)
        .define('e', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_lime_dye", has(Items.LIME_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.M9.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.SMALL_BODY.get())
        .define('c', ModItems.SMALL_BARREL.get())
        .define('d', Items.ORANGE_DYE)
        .define('e', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_orange_dye", has(Items.ORANGE_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.DESERT_EAGLE.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.SMALL_BODY.get())
        .define('c', ModItems.SMALL_BARREL.get())
        .define('d', Items.GRAY_DYE)
        .define('e', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_gray_dye", has(Items.GRAY_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.P250.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.SMALL_BODY.get())
        .define('c', ModItems.SMALL_BARREL.get())
        .define('d', Items.BROWN_DYE)
        .define('e', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_brown_dye", has(Items.BROWN_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.MAGNUM.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.SMALL_BODY.get())
        .define('c', ModItems.SMALL_BARREL.get())
        .define('d', Items.LIGHT_GRAY_DYE)
        .define('e', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_light_gray_dye", has(Items.LIGHT_GRAY_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.FN57.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.SMALL_BODY.get())
        .define('c', ModItems.SMALL_BARREL.get())
        .define('d', Items.YELLOW_DYE)
        .define('e', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_yellow_dye", has(Items.YELLOW_DYE))
        .save(consumer);

    // ================================================================================
    // Submachine Guns
    // ================================================================================

    ShapedRecipeBuilder.shaped(ModItems.MAC10.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.SMALL_BARREL.get())
        .define('d', Items.GRAY_DYE)
        .define('e', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_gray_dye", has(Items.GRAY_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.P90.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.SMALL_BARREL.get())
        .define('d', Items.LIGHT_GRAY_DYE)
        .define('e', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_light_gray_dye", has(Items.LIGHT_GRAY_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.VECTOR.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.SMALL_BARREL.get())
        .define('d', Items.BLUE_DYE)
        .define('e', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_blue_dye", has(Items.BLUE_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.MP5A5.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.SMALL_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.SMALL_BARREL.get())
        .define('d', Items.LIME_DYE)
        .define('e', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_lime_dye", has(Items.LIME_DYE))
        .save(consumer);

    // ================================================================================
    // Sniper Rifles
    // ================================================================================

    ShapedRecipeBuilder.shaped(ModItems.M107.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.MEDIUM_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.HEAVY_BARREL.get())
        .define('d', Items.RED_DYE)
        .define('e', ModItems.HEAVY_HANDLE.get())
        .unlockedBy("has_red_dye", has(Items.RED_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.AS50.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.MEDIUM_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.HEAVY_BARREL.get())
        .define('d', Items.LIME_DYE)
        .define('e', ModItems.HEAVY_HANDLE.get())
        .unlockedBy("has_lime_dye", has(Items.LIME_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.AWP.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.MEDIUM_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.HEAVY_BARREL.get())
        .define('d', Items.GREEN_DYE)
        .define('e', ModItems.HEAVY_HANDLE.get())
        .unlockedBy("has_green_dye", has(Items.GREEN_DYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.DMR.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.MEDIUM_STOCK.get())
        .define('b', ModItems.MEDIUM_BODY.get())
        .define('c', ModItems.HEAVY_BARREL.get())
        .define('d', Items.LIGHT_GRAY_DYE)
        .define('e', ModItems.HEAVY_HANDLE.get())
        .unlockedBy("has_light_gray_dye", has(Items.LIGHT_GRAY_DYE))
        .save(consumer);

    // ================================================================================
    // Shotguns
    // ================================================================================

    ShapedRecipeBuilder.shaped(ModItems.TRENCHGUN.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.MEDIUM_STOCK.get())
        .define('b', ModItems.HEAVY_BODY.get())
        .define('c', ModItems.HEAVY_BARREL.get())
        .define('d', Items.OAK_PLANKS)
        .define('e', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_oak_planks", has(Items.OAK_PLANKS))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.MOSSBERG.get())
        .pattern(" d ")
        .pattern("abc")
        .pattern(" e ")
        .define('a', ModItems.MEDIUM_STOCK.get())
        .define('b', ModItems.HEAVY_BODY.get())
        .define('c', ModItems.HEAVY_BARREL.get())
        .define('d', Items.BLACK_DYE)
        .define('e', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_black_dye", has(Items.BLACK_DYE))
        .save(consumer);

    // ================================================================================
    // Grenades
    // ================================================================================

    ShapedRecipeBuilder.shaped(ModItems.FIRE_GRENADE.get())
        .pattern(" i ")
        .pattern("ifi")
        .pattern(" i ")
        .define('i', Tags.Items.INGOTS_IRON)
        .define('f', Items.FIRE_CHARGE)
        .unlockedBy("has_fire_charge", has(Items.FIRE_CHARGE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.SMOKE_GRENADE.get())
        .pattern("wiw")
        .pattern("igi")
        .pattern("wiw")
        .define('i', Tags.Items.INGOTS_IRON)
        .define('w', ItemTags.WOOL)
        .define('g', Tags.Items.GUNPOWDER)
        .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.FLASH_GRENADE.get())
        .pattern("eie")
        .pattern("igi")
        .pattern("eie")
        .define('i', Tags.Items.INGOTS_IRON)
        .define('e', Items.FERMENTED_SPIDER_EYE)
        .define('g', Tags.Items.GUNPOWDER)
        .unlockedBy("has_fermented_spider_eye", has(Items.FERMENTED_SPIDER_EYE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.DECOY_GRENADE.get())
        .pattern("nin")
        .pattern("igi")
        .pattern("nin")
        .define('i', Tags.Items.INGOTS_IRON)
        .define('n', Items.NOTE_BLOCK)
        .define('g', Tags.Items.GUNPOWDER)
        .unlockedBy("has_note_block", has(Items.NOTE_BLOCK))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.FRAG_GRENADE.get())
        .pattern(" i ")
        .pattern("igi")
        .pattern(" i ")
        .define('i', Tags.Items.INGOTS_IRON)
        .define('g', Tags.Items.GUNPOWDER)
        .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.C4.get())
        .pattern(" i ")
        .pattern("iti")
        .pattern(" i ")
        .define('i', Tags.Items.INGOTS_IRON)
        .define('t', Items.TNT)
        .unlockedBy("has_tnt", has(Items.TNT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.REMOTE_DETONATOR.get())
        .pattern(" i ")
        .pattern("iri")
        .pattern("iii")
        .define('i', Tags.Items.INGOTS_IRON)
        .define('r', Tags.Items.DUSTS_REDSTONE)
        .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
        .save(consumer);

    // ================================================================================
    // Medical
    // ================================================================================

    ShapedRecipeBuilder.shaped(ModItems.FIRST_AID_KIT.get())
        .pattern("sss")
        .pattern("sas")
        .pattern("sss")
        .define('s', Items.STRING)
        .define('a', Items.APPLE)
        .unlockedBy("has_string", has(Items.STRING))
        .unlockedBy("has_apple", has(Items.APPLE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.SYRINGE.get())
        .pattern("gag")
        .pattern("g g")
        .pattern("ggg")
        .define('g', Items.GLASS)
        .define('a', Items.ARROW)
        .unlockedBy("has_glass", has(Items.GLASS))
        .save(consumer);
    ShapelessRecipeBuilder.shapeless(ModItems.BANDAGE.get())
        .requires(Items.STRING)
        .requires(Items.STRING)
        .unlockedBy("has_string", has(Items.STRING))
        .save(consumer);

    // ================================================================================
    // Melee Weapons
    // ================================================================================

    ShapedRecipeBuilder.shaped(ModItems.BOWIE_KNIFE.get())
        .pattern(" s ")
        .pattern("k  ")
        .define('s', Items.STICK)
        .define('k', ModItems.COMBAT_KNIFE.get())
        .unlockedBy("has_combat_knife", has(ModItems.COMBAT_KNIFE.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.COMBAT_KNIFE.get())
        .pattern("  i")
        .pattern(" i ")
        .pattern("s  ")
        .define('s', Items.STICK)
        .define('i', Tags.Items.INGOTS_IRON)
        .unlockedBy("has_stick", has(Items.STICK))
        .save(consumer);

    // ================================================================================
    // Gun Parts
    // ================================================================================

    ShapedRecipeBuilder.shaped(ModItems.SMALL_BARREL.get())
        .pattern("  i")
        .pattern(" i ")
        .pattern("i  ")
        .define('i', Items.IRON_INGOT)
        .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.SMALL_BODY.get())
        .pattern("iii")
        .pattern("iri")
        .pattern("ii ")
        .define('i', Items.IRON_INGOT)
        .define('r', Items.REDSTONE)
        .unlockedBy("has_redstone", has(Items.REDSTONE))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.SMALL_HANDLE.get())
        .pattern("iii")
        .pattern("il ")
        .define('i', Items.IRON_INGOT)
        .define('l', Items.LEVER)
        .unlockedBy("has_lever", has(Items.LEVER))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.SMALL_STOCK.get())
        .pattern("iii")
        .pattern("iii")
        .pattern("ii ")
        .define('i', Items.IRON_INGOT)
        .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.MEDIUM_BARREL.get())
        .pattern("iii")
        .pattern("ibi")
        .pattern("iii")
        .define('i', Items.IRON_INGOT)
        .define('b', ModItems.SMALL_BARREL.get())
        .unlockedBy("has_small_barrel", has(ModItems.SMALL_BARREL.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.MEDIUM_BODY.get())
        .pattern("iii")
        .pattern("ibi")
        .pattern("iii")
        .define('i', Items.IRON_INGOT)
        .define('b', ModItems.SMALL_BODY.get())
        .unlockedBy("has_small_body", has(ModItems.SMALL_BODY.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.MEDIUM_HANDLE.get())
        .pattern("iii")
        .pattern("ihi")
        .pattern("iii")
        .define('i', Items.IRON_INGOT)
        .define('h', ModItems.SMALL_HANDLE.get())
        .unlockedBy("has_small_handle", has(ModItems.SMALL_HANDLE.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.MEDIUM_STOCK.get())
        .pattern("iii")
        .pattern("isi")
        .pattern("iii")
        .define('i', Items.IRON_INGOT)
        .define('s', ModItems.SMALL_STOCK.get())
        .unlockedBy("has_small_stock", has(ModItems.SMALL_STOCK.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.HEAVY_BARREL.get())
        .pattern("iii")
        .pattern("ibi")
        .pattern("iii")
        .define('i', Items.IRON_INGOT)
        .define('b', ModItems.MEDIUM_BARREL.get())
        .unlockedBy("has_medium_barrel", has(ModItems.MEDIUM_BARREL.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.HEAVY_BODY.get())
        .pattern("iii")
        .pattern("ibi")
        .pattern("iii")
        .define('i', Items.IRON_INGOT)
        .define('b', ModItems.MEDIUM_BODY.get())
        .unlockedBy("has_medium_body", has(ModItems.MEDIUM_BODY.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.HEAVY_HANDLE.get())
        .pattern("iii")
        .pattern("ihi")
        .pattern("iii")
        .define('i', Items.IRON_INGOT)
        .define('h', ModItems.MEDIUM_HANDLE.get())
        .unlockedBy("has_medium_handle", has(ModItems.MEDIUM_HANDLE.get()))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.MEDIUM_BOLT.get())
        .pattern("iii")
        .pattern("ii ")
        .define('i', Items.IRON_INGOT)
        .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
        .save(consumer);
    ShapedRecipeBuilder.shaped(ModItems.HEAVY_BOLT.get())
        .pattern("iii")
        .pattern("ibi")
        .pattern("iii")
        .define('i', Items.IRON_INGOT)
        .define('b', ModItems.MEDIUM_BOLT.get())
        .unlockedBy("has_medium_bolt", has(ModItems.MEDIUM_BOLT.get()))
        .save(consumer);

    ShapelessRecipeBuilder.shapeless(Items.IRON_INGOT, 4)
        .requires(ModItemTags.MAGAZINES)
        .unlockedBy("has_magazine", has(ModItemTags.MAGAZINES))
        .save(consumer);
  }

  @Override
  public String getName() {
    return "Crafting Dead Recipes";
  }
}
