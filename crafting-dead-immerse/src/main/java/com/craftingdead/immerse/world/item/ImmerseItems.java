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

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.world.action.ImmerseActionTypes;
import com.craftingdead.immerse.world.level.block.ImmerseBlocks;
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
}
