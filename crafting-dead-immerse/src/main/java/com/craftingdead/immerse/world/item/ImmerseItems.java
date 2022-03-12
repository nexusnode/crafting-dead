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

package com.craftingdead.immerse.world.item;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.world.action.ImmerseActionTypes;
import com.craftingdead.immerse.world.food.ImmerseFoods;
import com.craftingdead.immerse.world.level.block.ImmerseBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmerseItems {

  public static final DeferredRegister<Item> items =
      DeferredRegister.create(ForgeRegistries.ITEMS, CraftingDeadImmerse.ID);

  public static final CreativeModeTab TAB =
      new CreativeModeTab(CraftingDeadImmerse.ID) {

        @Override
        public ItemStack makeIcon() {
          return new ItemStack(BASE_CENTER.get());
        }
      };

  public static final RegistryObject<BlockItem> BASE_CENTER =
      items.register("base_center",
          () -> new BlockItem(ImmerseBlocks.BASE_CENTER.get(), new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> BASE_CENTER_BLUEPRINT =
      items.register("base_center_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_BASE_CENTER,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> CHEST_BLUEPRINT =
      items.register("chest_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_CHEST, new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> CAMPFIRE_BLUEPRINT =
      items.register("campfire_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_CAMPFIRE,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> OAK_PLANK_WALL_BLUEPRINT =
      items.register("oak_plank_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_OAK_PLANK_WALL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> SPRUCE_PLANK_WALL_BLUEPRINT =
      items.register("spruce_plank_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_SPRUCE_PLANK_WALL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> BIRCH_PLANK_WALL_BLUEPRINT =
      items.register("birch_plank_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_BIRCH_PLANK_WALL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> JUNGLE_PLANK_WALL_BLUEPRINT =
      items.register("jungle_plank_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_JUNGLE_PLANK_WALL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> ACACIA_PLANK_WALL_BLUEPRINT =
      items.register("acacia_plank_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_ACACIA_PLANK_WALL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> DARK_OAK_PLANK_WALL_BLUEPRINT =
      items.register("dark_oak_plank_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_DARK_OAK_PLANK_WALL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> OAK_DOOR_BLUEPRINT =
      items.register("oak_door_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_OAK_DOOR,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> SPRUCE_DOOR_BLUEPRINT =
      items.register("spruce_door_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_SPRUCE_DOOR,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> BIRCH_DOOR_BLUEPRINT =
      items.register("birch_door_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_BIRCH_DOOR,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> JUNGLE_DOOR_BLUEPRINT =
      items.register("jungle_door_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_JUNGLE_DOOR,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> ACACIA_DOOR_BLUEPRINT =
      items.register("acacia_door_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_ACACIA_DOOR,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> DARK_OAK_DOOR_BLUEPRINT =
      items.register("dark_oak_door_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_DARK_OAK_DOOR,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> OAK_PLANK_PLATFORM_BLUEPRINT =
      items.register("oak_plank_platform_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_OAK_PLANK_PLATFORM,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> SPRUCE_PLANK_PLATFORM_BLUEPRINT =
      items.register("spruce_plank_platform_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_SPRUCE_PLANK_PLATFORM,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> BIRCH_PLANK_PLATFORM_BLUEPRINT =
      items.register("birch_plank_platform_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_BIRCH_PLANK_PLATFORM,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> JUNGLE_PLANK_PLATFORM_BLUEPRINT =
      items.register("jungle_plank_platform_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_JUNGLE_PLANK_PLATFORM,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> ACACIA_PLANK_PLATFORM_BLUEPRINT =
      items.register("acacia_plank_platform_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_ACACIA_PLANK_PLATFORM,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> DARK_OAK_PLANK_PLATFORM_BLUEPRINT =
      items.register("dark_oak_plank_platform_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_DARK_OAK_PLANK_PLATFORM,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> POWER_BAR =
      items.register("power_bar",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.POWER_BAR)));

  public static final RegistryObject<Item> CANDY_BAR =
      items.register("candy_bar",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANDY_BAR)));

  public static final RegistryObject<Item> CEREAL =
      items.register("cereal",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CEREAL)));

  public static final RegistryObject<Item> CANNED_CORN =
      items.register("canned_corn",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_CORN)));

  public static final RegistryObject<Item> CANNED_BEANS =
      items.register("canned_beans",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_BEANS)));

  public static final RegistryObject<Item> CANNED_TUNA =
      items.register("canned_tuna",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_TUNA)));

  public static final RegistryObject<Item> CANNED_PEACHES =
      items.register("canned_peaches",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_PEACHES)));

  public static final RegistryObject<Item> CANNED_PASTA =
      items.register("canned_pasta",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_PASTA)));

  public static final RegistryObject<Item> CANNED_BACON =
      items.register("canned_bacon",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_BACON)));

  public static final RegistryObject<Item> CANNED_CUSTARD =
      items.register("canned_custard",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_CUSTARD)));

  public static final RegistryObject<Item> CANNED_PICKLES =
      items.register("canned_pickles",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_PICKLES)));

  public static final RegistryObject<Item> CANNED_DOG_FOOD =
      items.register("canned_dog_food",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_DOG_FOOD)));

  public static final RegistryObject<Item> CANNED_TOMATO_SOUP =
      items.register("canned_tomato_soup",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_TOMATO_SOUP)));

  public static final RegistryObject<Item> MRE =
      items.register("mre",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.MRE)));

  public static final RegistryObject<Item> ORANGE =
      items.register("orange",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ORANGE)));

  public static final RegistryObject<Item> ROTTEN_ORANGE =
      items.register("rotten_orange",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ROTTEN_ORANGE)));

  public static final RegistryObject<Item> PEAR =
      items.register("pear",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.PEAR)));

  public static final RegistryObject<Item> ROTTEN_PEAR =
      items.register("rotten_pear",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ROTTEN_PEAR)));

  public static final RegistryObject<Item> RICE_BAG =
      items.register("rice_bag",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.RICE_BAG)));

  public static final RegistryObject<Item> APPLE =
      items.register("apple",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.APPLE)));

  public static final RegistryObject<Item> ROTTEN_APPLE =
      items.register("rotten_apple",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ROTTEN_APPLE)));

  public static final RegistryObject<Item> NOODLES =
      items.register("noodles",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.NOODLES)));

  public static final RegistryObject<Item> WATERMELON =
      items.register("watermelon",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.WATERMELON)));

  public static final RegistryObject<Item> ROTTEN_WATERMELON =
      items.register("rotten_watermelon",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ROTTEN_WATERMELON)));

  public static final RegistryObject<Item> BLUEBERRY =
      items.register("blueberry",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.BLUEBERRY)));

  public static final RegistryObject<Item> ROTTEN_BLUEBERRY =
      items.register("rotten_blueberry",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ROTTEN_BLUEBERRY)));

  public static final RegistryObject<Item> RASPBERRY =
      items.register("raspberry",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.RASPBERRY)));

  public static final RegistryObject<Item> ROTTEN_RASPBERRY =
      items.register("rotten_raspberry",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ROTTEN_RASPBERRY)));

  public static final RegistryObject<Item> CHIPS =
      items.register("chips",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CHIPS)));

  public static final RegistryObject<Item> RANCH_CHIPS =
      items.register("ranch_chips",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.RANCH_CHIPS)));

  public static final RegistryObject<Item> CHEESY_CHIPS =
      items.register("cheesy_chips",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CHEESY_CHIPS)));

  public static final RegistryObject<Item> SALTED_CHIPS =
      items.register("salted_chips",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.SALTED_CHIPS)));

  public static final RegistryObject<Item> POPCORN =
      items.register("popcorn",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.POPCORN)));

  public static final RegistryObject<Item> NUTTY_CEREAL =
      items.register("nutty_cereal",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.NUTTY_CEREAL)));

  public static final RegistryObject<Item> EMERALD_CEREAL =
      items.register("emerald_cereal",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.EMERALD_CEREAL)));

  public static final RegistryObject<Item> FLAKE_CEREAL =
      items.register("flake_cereal",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.FLAKE_CEREAL)));
}
