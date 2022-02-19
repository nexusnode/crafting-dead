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
          () -> BuildBlockActionType.builder()
              .duration(100)
              .block(ImmerseBlocks.BASE_CENTER)
              .notClaimed()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_CAMPFIRE =
      actionTypes.register("build_campfire",
          () -> BuildBlockActionType.builder()
              .duration(100)
              .block(Blocks.CAMPFIRE)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_OAK_PLANK_WALL =
      actionTypes.register("build_oak_plank_wall",
          () -> BuildWallActionType.builder()
              .duration(100)
              .block(Blocks.OAK_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_SPRUCE_PLANK_WALL =
      actionTypes.register("build_spruce_plank_wall",
          () -> BuildWallActionType.builder()
              .duration(100)
              .block(Blocks.SPRUCE_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_BIRCH_PLANK_WALL =
      actionTypes.register("build_birch_plank_wall",
          () -> BuildWallActionType.builder()
              .duration(100)
              .block(Blocks.BIRCH_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_JUNGLE_PLANK_WALL =
      actionTypes.register("build_jungle_plank_wall",
          () -> BuildWallActionType.builder()
              .duration(100)
              .block(Blocks.JUNGLE_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_ACACIA_PLANK_WALL =
      actionTypes.register("build_acacia_plank_wall",
          () -> BuildWallActionType.builder()
              .duration(100)
              .block(Blocks.ACACIA_PLANKS)
              .withinBase()
              .build());

  public static final RegistryObject<ItemActionType<?>> BUILD_DARK_OAK_PLANK_WALL =
      actionTypes.register("build_dark_oak_plank_wall",
          () -> BuildWallActionType.builder()
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
}
