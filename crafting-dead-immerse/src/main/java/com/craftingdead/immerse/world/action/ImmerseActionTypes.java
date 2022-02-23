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

package com.craftingdead.immerse.world.action;

import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.world.level.block.ImmerseBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ImmerseActionTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<ActionType<?>> actionTypes =
      DeferredRegister.create((Class<ActionType<?>>) (Object) ActionType.class,
          CraftingDeadImmerse.ID);

  public static final RegistryObject<ItemActionType<?>> BUILD_BASE_CENTER =
      actionTypes.register("build_base_center",
          () -> BuildCuboidActionType.block()
              .duration(100)
              .block(ImmerseBlocks.BASE_CENTER)
              .notClaimed()
              .dontOwnBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_CAMPFIRE =
      actionTypes.register("build_campfire",
          () -> BuildCuboidActionType.block()
              .duration(100)
              .block(Blocks.CAMPFIRE)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_CHEST =
      actionTypes.register("build_chest",
          () -> BuildCuboidActionType.block()
              .duration(100)
              .block(Blocks.CHEST)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_OAK_PLANK_WALL =
      actionTypes.register("build_oak_plank_wall",
          () -> BuildCuboidActionType.wall()
              .duration(100)
              .block(Blocks.OAK_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_SPRUCE_PLANK_WALL =
      actionTypes.register("build_spruce_plank_wall",
          () -> BuildCuboidActionType.wall()
              .duration(100)
              .block(Blocks.SPRUCE_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_BIRCH_PLANK_WALL =
      actionTypes.register("build_birch_plank_wall",
          () -> BuildCuboidActionType.wall()
              .duration(100)
              .block(Blocks.BIRCH_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_JUNGLE_PLANK_WALL =
      actionTypes.register("build_jungle_plank_wall",
          () -> BuildCuboidActionType.wall()
              .duration(100)
              .block(Blocks.JUNGLE_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_ACACIA_PLANK_WALL =
      actionTypes.register("build_acacia_plank_wall",
          () -> BuildCuboidActionType.wall()
              .duration(100)
              .block(Blocks.ACACIA_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_DARK_OAK_PLANK_WALL =
      actionTypes.register("build_dark_oak_plank_wall",
          () -> BuildCuboidActionType.wall()
              .duration(100)
              .block(Blocks.DARK_OAK_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_OAK_DOOR =
      actionTypes.register("build_oak_door",
          () -> BuildDoorWallActionType.builder()
              .duration(100)
              .wallBlock(Blocks.OAK_PLANKS)
              .doorBlock(Blocks.OAK_DOOR)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_SPRUCE_DOOR =
      actionTypes.register("build_spruce_door",
          () -> BuildDoorWallActionType.builder()
              .duration(100)
              .wallBlock(Blocks.SPRUCE_PLANKS)
              .doorBlock(Blocks.SPRUCE_DOOR)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_BIRCH_DOOR =
      actionTypes.register("build_birch_door",
          () -> BuildDoorWallActionType.builder()
              .duration(100)
              .wallBlock(Blocks.BIRCH_PLANKS)
              .doorBlock(Blocks.BIRCH_DOOR)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_JUNGLE_DOOR =
      actionTypes.register("build_jungle_door",
          () -> BuildDoorWallActionType.builder()
              .duration(100)
              .wallBlock(Blocks.JUNGLE_PLANKS)
              .doorBlock(Blocks.JUNGLE_DOOR)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_ACACIA_DOOR =
      actionTypes.register("build_acacia_door",
          () -> BuildDoorWallActionType.builder()
              .duration(100)
              .wallBlock(Blocks.ACACIA_PLANKS)
              .doorBlock(Blocks.ACACIA_DOOR)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_DARK_OAK_DOOR =
      actionTypes.register("build_dark_oak_door",
          () -> BuildDoorWallActionType.builder()
              .duration(100)
              .wallBlock(Blocks.DARK_OAK_PLANKS)
              .doorBlock(Blocks.DARK_OAK_DOOR)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_OAK_PLANK_PLATFORM =
      actionTypes.register("build_oak_plank_platform",
          () -> BuildCuboidActionType.platform()
              .duration(100)
              .block(Blocks.OAK_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_SPRUCE_PLANK_PLATFORM =
      actionTypes.register("build_spruce_plank_platform",
          () -> BuildCuboidActionType.platform()
              .duration(100)
              .block(Blocks.SPRUCE_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_BIRCH_PLANK_PLATFORM =
      actionTypes.register("build_birch_plank_platform",
          () -> BuildCuboidActionType.platform()
              .duration(100)
              .block(Blocks.BIRCH_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_JUNGLE_PLANK_PLATFORM =
      actionTypes.register("build_jungle_plank_platform",
          () -> BuildCuboidActionType.platform()
              .duration(100)
              .block(Blocks.JUNGLE_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_ACACIA_PLANK_PLATFORM =
      actionTypes.register("build_acacia_plank_platform",
          () -> BuildCuboidActionType.platform()
              .duration(100)
              .block(Blocks.ACACIA_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_DARK_OAK_PLANK_PLATFORM =
      actionTypes.register("build_dark_oak_plank_platform",
          () -> BuildCuboidActionType.platform()
              .duration(100)
              .block(Blocks.DARK_OAK_PLANKS)
              .withinBase()
              .build());
}
