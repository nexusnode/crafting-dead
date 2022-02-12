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
  public static final DeferredRegister<ActionType<?>> ACTION_TYPES =
      DeferredRegister.create((Class<ActionType<?>>) (Object) ActionType.class,
          CraftingDeadImmerse.ID);

  public static final RegistryObject<ItemActionType<?>> BUILD_BASE_CENTER =
      ACTION_TYPES.register("build_base_center",
          () -> BuildBlockActionType.builder()
              .duration(100)
              .block(ImmerseBlocks.BASE_CENTER)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_CAMPFIRE =
      ACTION_TYPES.register("build_campfire",
          () -> BuildBlockActionType.builder()
              .duration(100)
              .block(Blocks.CAMPFIRE)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_OAK_PLANKS_WALL =
      ACTION_TYPES.register("build_oak_planks_wall",
          () -> BuildWallActionType.builder()
              .duration(100)
              .block(Blocks.OAK_PLANKS)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_SPRUCE_PLANKS_WALL =
      ACTION_TYPES.register("build_spruce_planks_wall",
          () -> BuildWallActionType.builder()
              .duration(100)
              .block(Blocks.SPRUCE_PLANKS)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_BIRCH_PLANKS_WALL =
      ACTION_TYPES.register("build_birch_planks_wall",
          () -> BuildWallActionType.builder()
              .duration(100)
              .block(Blocks.BIRCH_PLANKS)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_JUNGLE_PLANKS_WALL =
      ACTION_TYPES.register("build_jungle_planks_wall",
          () -> BuildWallActionType.builder()
              .duration(100)
              .block(Blocks.JUNGLE_PLANKS)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_ACACIA_PLANKS_WALL =
      ACTION_TYPES.register("build_acacia_planks_wall",
          () -> BuildWallActionType.builder()
              .duration(100)
              .block(Blocks.ACACIA_PLANKS)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_DARK_OAK_PLANKS_WALL =
      ACTION_TYPES.register("build_dark_oak_planks_wall",
          () -> BuildWallActionType.builder()
              .duration(100)
              .block(Blocks.DARK_OAK_PLANKS)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_OAK_DOOR =
      ACTION_TYPES.register("build_oak_door",
          () -> BuildDoorWallActionType.builder()
              .duration(100)
              .wallBlock(Blocks.OAK_PLANKS)
              .doorBlock(Blocks.OAK_DOOR)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_SPRUCE_DOOR =
      ACTION_TYPES.register("build_spruce_door",
          () -> BuildDoorWallActionType.builder()
              .duration(100)
              .wallBlock(Blocks.SPRUCE_PLANKS)
              .doorBlock(Blocks.SPRUCE_DOOR)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_BIRCH_DOOR =
      ACTION_TYPES.register("build_birch_door",
          () -> BuildDoorWallActionType.builder()
              .duration(100)
              .wallBlock(Blocks.BIRCH_PLANKS)
              .doorBlock(Blocks.BIRCH_DOOR)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_JUNGLE_DOOR =
      ACTION_TYPES.register("build_jungle_door",
          () -> BuildDoorWallActionType.builder()
              .duration(100)
              .wallBlock(Blocks.JUNGLE_PLANKS)
              .doorBlock(Blocks.JUNGLE_DOOR)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_ACACIA_DOOR =
      ACTION_TYPES.register("build_acacia_door",
          () -> BuildDoorWallActionType.builder()
              .duration(100)
              .wallBlock(Blocks.ACACIA_PLANKS)
              .doorBlock(Blocks.ACACIA_DOOR)
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_DARK_OAK_DOOR =
      ACTION_TYPES.register("build_dark_oak_door",
          () -> BuildDoorWallActionType.builder()
              .duration(100)
              .wallBlock(Blocks.DARK_OAK_PLANKS)
              .doorBlock(Blocks.DARK_OAK_DOOR)
              .build());
}
