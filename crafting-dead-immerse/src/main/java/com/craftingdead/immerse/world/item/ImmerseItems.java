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

package com.craftingdead.immerse.world.item;

import com.craftingdead.core.world.item.ActionItem;
import com.craftingdead.core.world.item.ArbitraryTooltips;
import com.craftingdead.core.world.item.MeleeWeaponItem;
import com.craftingdead.core.world.item.ToolItem;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.world.action.ImmerseActionTypes;
import com.craftingdead.immerse.world.food.ImmerseFoods;
import com.craftingdead.immerse.world.level.block.ImmerseBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmerseItems {

  public static final DeferredRegister<Item> deferredRegister =
      DeferredRegister.create(ForgeRegistries.ITEMS, CraftingDeadImmerse.ID);

  public static final CreativeModeTab TAB =
      new CreativeModeTab(CraftingDeadImmerse.ID) {

        @Override
        public ItemStack makeIcon() {
          return new ItemStack(BASE_CENTER.get());
        }
      };

  public static final RegistryObject<BlockItem> BASE_CENTER =
      deferredRegister.register("base_center",
          () -> new BlockItem(ImmerseBlocks.BASE_CENTER.get(), new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> BASE_CENTER_BLUEPRINT =
      deferredRegister.register("base_center_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_BASE_CENTER,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> CHEST_BLUEPRINT =
      deferredRegister.register("chest_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_CHEST, new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> CAMPFIRE_BLUEPRINT =
      deferredRegister.register("campfire_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_CAMPFIRE,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> OAK_PLANK_WALL_BLUEPRINT =
      deferredRegister.register("oak_plank_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_OAK_PLANK_WALL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> SPRUCE_PLANK_WALL_BLUEPRINT =
      deferredRegister.register("spruce_plank_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_SPRUCE_PLANK_WALL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> BIRCH_PLANK_WALL_BLUEPRINT =
      deferredRegister.register("birch_plank_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_BIRCH_PLANK_WALL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> JUNGLE_PLANK_WALL_BLUEPRINT =
      deferredRegister.register("jungle_plank_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_JUNGLE_PLANK_WALL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> ACACIA_PLANK_WALL_BLUEPRINT =
      deferredRegister.register("acacia_plank_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_ACACIA_PLANK_WALL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> DARK_OAK_PLANK_WALL_BLUEPRINT =
      deferredRegister.register("dark_oak_plank_wall_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_DARK_OAK_PLANK_WALL,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> OAK_DOOR_BLUEPRINT =
      deferredRegister.register("oak_door_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_OAK_DOOR,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> SPRUCE_DOOR_BLUEPRINT =
      deferredRegister.register("spruce_door_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_SPRUCE_DOOR,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> BIRCH_DOOR_BLUEPRINT =
      deferredRegister.register("birch_door_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_BIRCH_DOOR,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> JUNGLE_DOOR_BLUEPRINT =
      deferredRegister.register("jungle_door_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_JUNGLE_DOOR,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> ACACIA_DOOR_BLUEPRINT =
      deferredRegister.register("acacia_door_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_ACACIA_DOOR,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> DARK_OAK_DOOR_BLUEPRINT =
      deferredRegister.register("dark_oak_door_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_DARK_OAK_DOOR,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> OAK_PLANK_PLATFORM_BLUEPRINT =
      deferredRegister.register("oak_plank_platform_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_OAK_PLANK_PLATFORM,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> SPRUCE_PLANK_PLATFORM_BLUEPRINT =
      deferredRegister.register("spruce_plank_platform_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_SPRUCE_PLANK_PLATFORM,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> BIRCH_PLANK_PLATFORM_BLUEPRINT =
      deferredRegister.register("birch_plank_platform_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_BIRCH_PLANK_PLATFORM,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> JUNGLE_PLANK_PLATFORM_BLUEPRINT =
      deferredRegister.register("jungle_plank_platform_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_JUNGLE_PLANK_PLATFORM,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> ACACIA_PLANK_PLATFORM_BLUEPRINT =
      deferredRegister.register("acacia_plank_platform_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_ACACIA_PLANK_PLATFORM,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlueprintItem> DARK_OAK_PLANK_PLATFORM_BLUEPRINT =
      deferredRegister.register("dark_oak_plank_platform_blueprint",
          () -> new BlueprintItem(ImmerseActionTypes.BUILD_DARK_OAK_PLANK_PLATFORM,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> EMPTY_WATER_BOTTLE =
      deferredRegister.register("empty_water_bottle",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> WATER_BOTTLE =
      deferredRegister.register("water_bottle",
          () -> new DrinkItem(new Item.Properties().tab(TAB), 8, EMPTY_WATER_BOTTLE));

  public static final RegistryObject<Item> EMPTY_WATER_CANTEEN =
      deferredRegister.register("empty_water_canteen",
          () -> new ActionItem(ImmerseActionTypes.FILL_WATER_CANTEEN,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> WATER_CANTEEN =
      deferredRegister.register("water_canteen",
          () -> new DrinkItem(new Item.Properties().tab(TAB), 9, EMPTY_WATER_CANTEEN));

  public static final RegistryObject<Item> POWER_BAR =
      deferredRegister.register("power_bar",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.POWER_BAR)));

  public static final RegistryObject<Item> CANDY_BAR =
      deferredRegister.register("candy_bar",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANDY_BAR)));

  public static final RegistryObject<Item> CEREAL =
      deferredRegister.register("cereal",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CEREAL)));

  public static final RegistryObject<Item> CANNED_SWEETCORN =
      deferredRegister.register("canned_sweetcorn",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> OPEN_CANNED_SWEETCORN =
      deferredRegister.register("open_canned_sweetcorn",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_CORN)));

  public static final RegistryObject<Item> CANNED_BEANS =
      deferredRegister.register("canned_beans",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> OPEN_CANNED_BEANS =
      deferredRegister.register("open_canned_beans",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_BEANS)));

  public static final RegistryObject<Item> CANNED_TUNA =
      deferredRegister.register("canned_tuna",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> OPEN_CANNED_TUNA =
      deferredRegister.register("open_canned_tuna",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_TUNA)));

  public static final RegistryObject<Item> CANNED_PEACHES =
      deferredRegister.register("canned_peaches",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> OPEN_CANNED_PEACHES =
      deferredRegister.register("open_canned_peaches",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_PEACHES)));

  public static final RegistryObject<Item> CANNED_PASTA =
      deferredRegister.register("canned_pasta",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> OPEN_CANNED_PASTA =
      deferredRegister.register("open_canned_pasta",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_PASTA)));

  public static final RegistryObject<Item> CANNED_CORNED_BEEF =
      deferredRegister.register("canned_corned_beef",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> OPEN_CANNED_CORNED_BEEF =
      deferredRegister.register("open_canned_corned_beef",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_BACON)));

  public static final RegistryObject<Item> CANNED_CUSTARD =
      deferredRegister.register("canned_custard",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> OPEN_CANNED_CUSTARD =
      deferredRegister.register("open_canned_custard",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_CUSTARD)));

  public static final RegistryObject<Item> CANNED_PICKLES =
      deferredRegister.register("canned_pickles",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> OPEN_CANNED_PICKLES =
      deferredRegister.register("open_canned_pickles",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_PICKLES)));

  public static final RegistryObject<Item> CANNED_DOG_FOOD =
      deferredRegister.register("canned_dog_food",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> OPEN_CANNED_DOG_FOOD =
      deferredRegister.register("open_canned_dog_food",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_DOG_FOOD)));

  public static final RegistryObject<Item> CANNED_TOMATO_SOUP =
      deferredRegister.register("canned_tomato_soup",
          () -> new Item(new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> OPEN_CANNED_TOMATO_SOUP =
      deferredRegister.register("open_canned_tomato_soup",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CANNED_TOMATO_SOUP)));

  public static final RegistryObject<Item> MRE =
      deferredRegister.register("mre",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.MRE)));

  public static final RegistryObject<Item> ORANGE =
      deferredRegister.register("orange",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ORANGE)));

  public static final RegistryObject<Item> ROTTEN_ORANGE =
      deferredRegister.register("rotten_orange",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ROTTEN_ORANGE)));

  public static final RegistryObject<Item> PEAR =
      deferredRegister.register("pear",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.PEAR)));

  public static final RegistryObject<Item> ROTTEN_PEAR =
      deferredRegister.register("rotten_pear",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ROTTEN_PEAR)));

  public static final RegistryObject<Item> RICE_BAG =
      deferredRegister.register("rice_bag",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.RICE_BAG)));

  public static final RegistryObject<Item> ROTTEN_APPLE =
      deferredRegister.register("rotten_apple",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ROTTEN_APPLE)));

  public static final RegistryObject<Item> NOODLES =
      deferredRegister.register("noodles",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.NOODLES)));

  public static final RegistryObject<Item> ROTTEN_MELON_SLICE =
      deferredRegister.register("rotten_melon_slice",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ROTTEN_MELON_SLICE)));

  public static final RegistryObject<Item> BLUEBERRY =
      deferredRegister.register("blueberry",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.BLUEBERRY)));

  public static final RegistryObject<Item> ROTTEN_BLUEBERRY =
      deferredRegister.register("rotten_blueberry",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ROTTEN_BLUEBERRY)));

  public static final RegistryObject<Item> RASPBERRY =
      deferredRegister.register("raspberry",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.RASPBERRY)));

  public static final RegistryObject<Item> ROTTEN_RASPBERRY =
      deferredRegister.register("rotten_raspberry",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.ROTTEN_RASPBERRY)));

  public static final RegistryObject<Item> CHIPS =
      deferredRegister.register("chips",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CHIPS)));

  public static final RegistryObject<Item> RANCH_CHIPS =
      deferredRegister.register("ranch_chips",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.RANCH_CHIPS)));

  public static final RegistryObject<Item> CHEESY_CHIPS =
      deferredRegister.register("cheesy_chips",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.CHEESY_CHIPS)));

  public static final RegistryObject<Item> SALTED_CHIPS =
      deferredRegister.register("salted_chips",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.SALTED_CHIPS)));

  public static final RegistryObject<Item> POPCORN =
      deferredRegister.register("popcorn",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.POPCORN)));

  public static final RegistryObject<Item> NUTTY_CEREAL =
      deferredRegister.register("nutty_cereal",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.NUTTY_CEREAL)));

  public static final RegistryObject<Item> EMERALD_CEREAL =
      deferredRegister.register("emerald_cereal",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.EMERALD_CEREAL)));

  public static final RegistryObject<Item> FLAKE_CEREAL =
      deferredRegister.register("flake_cereal",
          () -> new Item(new Item.Properties().tab(TAB).food(ImmerseFoods.FLAKE_CEREAL)));

  // ================================================================================
  // Tools
  // ================================================================================

  public static final RegistryObject<Item> CAN_OPENER =
      deferredRegister.register("can_opener", () -> new ToolItem(
          new Item.Properties().durability(8).tab(TAB)) {

        @Override
        public boolean hasContainerItem(ItemStack stack) {
          return true;
        }

        @Override
        public ItemStack getContainerItem(ItemStack itemStack) {
          return itemStack.copy();
        }
      });

  public static final RegistryObject<Item> SCREWDRIVER =
      deferredRegister.register("screwdriver", () -> new ToolItem(
          new Item.Properties().durability(4).tab(TAB)) {

        @Override
        public boolean hasContainerItem(ItemStack stack) {
          return true;
        }

        @Override
        public ItemStack getContainerItem(ItemStack itemStack) {
          return itemStack.copy();
        }
      });

  public static final RegistryObject<Item> MULTI_TOOL =
      deferredRegister.register("multi_tool", () -> new MeleeWeaponItem(8, -2.4F,
          new Item.Properties().durability(20).tab(TAB)) {

        @Override
        public boolean hasContainerItem(ItemStack stack) {
          return true;
        }

        @Override
        public ItemStack getContainerItem(ItemStack itemStack) {
          return itemStack.copy();
        }
      });

  static {
    var canOpenerTooltip = new TranslatableComponent("can_opener.information")
        .withStyle(ChatFormatting.GRAY);
    ArbitraryTooltips.registerTooltip(CAN_OPENER, canOpenerTooltip);
    ArbitraryTooltips.registerTooltip(SCREWDRIVER, canOpenerTooltip);
    ArbitraryTooltips.registerTooltip(MULTI_TOOL, canOpenerTooltip);
  }
}
