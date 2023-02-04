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

package com.craftingdead.decoration.world.level.block;

import com.craftingdead.decoration.CraftingDeadDecoration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DecorationBlocks {

  public static final DeferredRegister<Block> deferredRegister =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CraftingDeadDecoration.ID);

  public static final RegistryObject<Block> BLUE_GAS_CAN_1 =
      deferredRegister.register("blue_gas_can_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.GAS_CAN));

  public static final RegistryObject<Block> BLUE_GAS_CAN_2 =
      deferredRegister.register("blue_gas_can_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.GAS_CAN));

  public static final RegistryObject<Block> BLUE_GAS_CAN_3 =
      deferredRegister.register("blue_gas_can_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.DOUBLE_GAS_CAN));

  public static final RegistryObject<Block> GREEN_GAS_CAN_1 =
      deferredRegister.register("green_gas_can_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.GAS_CAN));

  public static final RegistryObject<Block> GREEN_GAS_CAN_2 =
      deferredRegister.register("green_gas_can_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.GAS_CAN));

  public static final RegistryObject<Block> GREEN_GAS_CAN_3 =
      deferredRegister.register("green_gas_can_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.DOUBLE_GAS_CAN));

  public static final RegistryObject<Block> RED_GAS_CAN_1 =
      deferredRegister.register("red_gas_can_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.GAS_CAN));

  public static final RegistryObject<Block> RED_GAS_CAN_2 =
      deferredRegister.register("red_gas_can_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.GAS_CAN));

  public static final RegistryObject<Block> RED_GAS_CAN_3 =
      deferredRegister.register("red_gas_can_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.DOUBLE_GAS_CAN));

  public static final RegistryObject<Block> BOX_STORE_SHELF_1 =
      deferredRegister.register("box_store_shelf_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOX_STORE_SHELF_2 =
      deferredRegister.register("box_store_shelf_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOX_STORE_SHELF_3 =
      deferredRegister.register("box_store_shelf_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOX_STORE_SHELF_4 =
      deferredRegister.register("box_store_shelf_4",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOTTOM_BOX_STORE_SHELF_1 =
      deferredRegister.register("bottom_box_store_shelf_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOTTOM_BOX_STORE_SHELF_2 =
      deferredRegister.register("bottom_box_store_shelf_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOTTOM_BOX_STORE_SHELF_3 =
      deferredRegister.register("bottom_box_store_shelf_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOTTOM_BOX_STORE_SHELF_4 =
      deferredRegister.register("bottom_box_store_shelf_4",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> CAN_STORE_SHELF_1 =
      deferredRegister.register("can_store_shelf_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> CAN_STORE_SHELF_2 =
      deferredRegister.register("can_store_shelf_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> CAN_STORE_SHELF_3 =
      deferredRegister.register("can_store_shelf_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));


  public static final RegistryObject<Block> CAN_STORE_SHELF_4 =
      deferredRegister.register("can_store_shelf_4",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOTTOM_CAN_STORE_SHELF_1 =
      deferredRegister.register("bottom_can_store_shelf_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOTTOM_CAN_STORE_SHELF_2 =
      deferredRegister.register("bottom_can_store_shelf_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOTTOM_CAN_STORE_SHELF_3 =
      deferredRegister.register("bottom_can_store_shelf_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));


  public static final RegistryObject<Block> BOTTOM_CAN_STORE_SHELF_4 =
      deferredRegister.register("bottom_can_store_shelf_4",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> STORE_SHELF_1 =
      deferredRegister.register("store_shelf_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> STORE_SHELF_2 =
      deferredRegister.register("store_shelf_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> STORE_SHELF_3 =
      deferredRegister.register("store_shelf_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));


  public static final RegistryObject<Block> STORE_SHELF_4 =
      deferredRegister.register("store_shelf_4",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOTTOM_STORE_SHELF_1 =
      deferredRegister.register("bottom_store_shelf_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOTTOM_STORE_SHELF_2 =
      deferredRegister.register("bottom_store_shelf_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> BOTTOM_STORE_SHELF_3 =
      deferredRegister.register("bottom_store_shelf_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));


  public static final RegistryObject<Block> BOTTOM_STORE_SHELF_4 =
      deferredRegister.register("bottom_store_shelf_4",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.STORE_SHELF));

  public static final RegistryObject<Block> TOOL_1 =
      deferredRegister.register("tool_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.TOOL_1));

  public static final RegistryObject<Block> TOOL_2 =
      deferredRegister.register("tool_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.TOOL_2));

  public static final RegistryObject<Block> TOOL_3 =
      deferredRegister.register("tool_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.TOOL_3));

  public static final RegistryObject<Block> TOOL_4 =
      deferredRegister.register("tool_4",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.TOOL_4));

  public static final RegistryObject<Block> TOILET =
      deferredRegister.register("toilet",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.TOILET));

  public static final RegistryObject<Block> FULL_TOILET =
      deferredRegister.register("full_toilet",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.TOILET));

  public static final RegistryObject<Block> DIRTY_TOILET =
      deferredRegister.register("dirty_toilet",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.TOILET));

  public static final RegistryObject<Block> FULL_DIRTY_TOILET =
      deferredRegister.register("full_dirty_toilet",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.TOILET));

  public static final RegistryObject<Block> OLD_TELEVISION =
      deferredRegister.register("old_television",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.OLD_TELEVISION));

  public static final RegistryObject<Block> CRACKED_OLD_TELEVISION =
      deferredRegister.register("cracked_old_television",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.OLD_TELEVISION));

  public static final RegistryObject<Block> CRACKED_TELEVISION =
      deferredRegister.register("cracked_television",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.TELEVISION));

  public static final RegistryObject<Block> TELEVISION =
      deferredRegister.register("television",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.TELEVISION));

  public static final RegistryObject<Block> OFFICE_CHAIR_1 =
      deferredRegister.register("office_chair_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noCollission(),
              __ -> Shapes.block()));

  public static final RegistryObject<Block> OFFICE_CHAIR_2 =
      deferredRegister.register("office_chair_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noCollission(),
              __ -> Shapes.block()));

  public static final RegistryObject<Block> OFFICE_CHAIR_3 =
      deferredRegister.register("office_chair_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noCollission(),
              __ -> Shapes.block()));

  public static final RegistryObject<Block> RIPPED_OFFICE_CHAIR_1 =
      deferredRegister.register("ripped_office_chair_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noCollission(),
              __ -> Shapes.block()));

  public static final RegistryObject<Block> RIPPED_OFFICE_CHAIR_2 =
      deferredRegister.register("ripped_office_chair_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noCollission(),
              __ -> Shapes.block()));

  public static final RegistryObject<Block> RIPPED_OFFICE_CHAIR_3 =
      deferredRegister.register("ripped_office_chair_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noCollission(),
              __ -> Shapes.block()));

  public static final RegistryObject<Block> LAPTOP_1 =
      deferredRegister.register("laptop_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noCollission(),
              BlockShapes.LAPTOP));

  public static final RegistryObject<Block> LAPTOP_2 =
      deferredRegister.register("laptop_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noCollission(),
              BlockShapes.LAPTOP));

  public static final RegistryObject<Block> LAPTOP_3 =
      deferredRegister.register("laptop_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noCollission(),
              BlockShapes.LAPTOP));

  public static final RegistryObject<Block> BROKEN_LAPTOP =
      deferredRegister.register("broken_laptop",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noCollission(),
              BlockShapes.LAPTOP));

  public static final RegistryObject<Block> YELLOW_GAS_TANK =
      deferredRegister.register("yellow_gas_tank",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.GAS_TANK));

  public static final RegistryObject<Block> BLUE_GAS_TANK =
      deferredRegister.register("blue_gas_tank",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.GAS_TANK));

  public static final RegistryObject<Block> GRAY_GAS_TANK =
      deferredRegister.register("gray_gas_tank",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.GAS_TANK));

  public static final RegistryObject<Block> COMPUTER_1 =
      deferredRegister.register("computer_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.COMPUTER));

  public static final RegistryObject<Block> COMPUTER_2 =
      deferredRegister.register("computer_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.COMPUTER));

  public static final RegistryObject<Block> COMPUTER_3 =
      deferredRegister.register("computer_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.COMPUTER));

  public static final RegistryObject<Block> BROKEN_COMPUTER =
      deferredRegister.register("broken_computer",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              BlockShapes.COMPUTER));

  public static final RegistryObject<Block> BLUE_BARREL_1 =
      deferredRegister.register("blue_barrel_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> WORN_BLUE_BARREL_1 =
      deferredRegister.register("worn_blue_barrel_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> BLUE_BARREL_2 =
      deferredRegister.register("blue_barrel_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> WORN_BLUE_BARREL_2 =
      deferredRegister.register("worn_blue_barrel_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> GRAY_BARREL_1 =
      deferredRegister.register("gray_barrel_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> WORN_GRAY_BARREL_1 =
      deferredRegister.register("worn_gray_barrel_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> GRAY_BARREL_2 =
      deferredRegister.register("gray_barrel_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> WORN_GRAY_BARREL_2 =
      deferredRegister.register("worn_gray_barrel_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> GREEN_BARREL_1 =
      deferredRegister.register("green_barrel_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> WORN_GREEN_BARREL_1 =
      deferredRegister.register("worn_green_barrel_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> GREEN_BARREL_2 =
      deferredRegister.register("green_barrel_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> WORN_GREEN_BARREL_2 =
      deferredRegister.register("worn_green_barrel_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> RED_BARREL_1 =
      deferredRegister.register("red_barrel_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> WORN_RED_BARREL_1 =
      deferredRegister.register("worn_red_barrel_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> RED_BARREL_2 =
      deferredRegister.register("red_barrel_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> WORN_RED_BARREL_2 =
      deferredRegister.register("worn_red_barrel_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL),
              BlockShapes.BARREL));

  public static final RegistryObject<Block> OAK_PLANK_BARRICADE_1 =
      deferredRegister.register("oak_plank_barricade_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(0.3F)
                  .sound(SoundType.WOOD),
              BlockShapes.PLANK_BARRICADE_1));

  public static final RegistryObject<Block> OAK_PLANK_BARRICADE_2 =
      deferredRegister.register("oak_plank_barricade_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(0.3F)
                  .sound(SoundType.WOOD),
              BlockShapes.PLANK_BARRICADE_2));

  public static final RegistryObject<Block> OAK_PLANK_BARRICADE_3 =
      deferredRegister.register("oak_plank_barricade_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(0.3F)
                  .sound(SoundType.WOOD),
              BlockShapes.PLANK_BARRICADE_3));

  public static final RegistryObject<Block> SPRUCE_PLANK_BARRICADE_1 =
      deferredRegister.register("spruce_plank_barricade_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(0.3F)
                  .sound(SoundType.WOOD),
              BlockShapes.PLANK_BARRICADE_1));

  public static final RegistryObject<Block> SPRUCE_PLANK_BARRICADE_2 =
      deferredRegister.register("spruce_plank_barricade_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(0.3F)
                  .sound(SoundType.WOOD),
              BlockShapes.PLANK_BARRICADE_2));

  public static final RegistryObject<Block> SPRUCE_PLANK_BARRICADE_3 =
      deferredRegister.register("spruce_plank_barricade_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(0.3F)
                  .sound(SoundType.WOOD),
              BlockShapes.PLANK_BARRICADE_3));

  public static final RegistryObject<Block> BIRCH_PLANK_BARRICADE_1 =
      deferredRegister.register("birch_plank_barricade_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(0.3F)
                  .sound(SoundType.WOOD),
              BlockShapes.PLANK_BARRICADE_1));

  public static final RegistryObject<Block> BIRCH_PLANK_BARRICADE_2 =
      deferredRegister.register("birch_plank_barricade_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(0.3F)
                  .sound(SoundType.WOOD),
              BlockShapes.PLANK_BARRICADE_2));

  public static final RegistryObject<Block> BIRCH_PLANK_BARRICADE_3 =
      deferredRegister.register("birch_plank_barricade_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(0.3F)
                  .sound(SoundType.WOOD),
              BlockShapes.PLANK_BARRICADE_3));

  public static final RegistryObject<Block> DARK_OAK_PLANK_BARRICADE_1 =
      deferredRegister.register("dark_oak_plank_barricade_1",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(0.3F)
                  .sound(SoundType.WOOD),
              BlockShapes.PLANK_BARRICADE_1));

  public static final RegistryObject<Block> DARK_OAK_PLANK_BARRICADE_2 =
      deferredRegister.register("dark_oak_plank_barricade_2",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(0.3F)
                  .sound(SoundType.WOOD),
              BlockShapes.PLANK_BARRICADE_2));

  public static final RegistryObject<Block> DARK_OAK_PLANK_BARRICADE_3 =
      deferredRegister.register("dark_oak_plank_barricade_3",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(0.3F)
                  .sound(SoundType.WOOD),
              BlockShapes.PLANK_BARRICADE_3));

  public static final RegistryObject<Block> BATTEN_LIGHT =
      deferredRegister.register("batten_light",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(0.3F)
                  .sound(SoundType.METAL)
                  .noCollission(),
              BlockShapes.BATTEN_LIGHT));

  public static final RegistryObject<Block> LIT_BATTEN_LIGHT =
      deferredRegister.register("lit_batten_light",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(0.3F)
                  .sound(SoundType.METAL)
                  .noCollission()
                  .lightLevel(__ -> 15),
              BlockShapes.BATTEN_LIGHT));

  public static final RegistryObject<Block> WOODEN_PALLET =
      deferredRegister.register("wooden_pallet",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(2.0F, 3.0F)
                  .sound(SoundType.WOOD)
                  .noOcclusion(),
              __ -> BlockShapes.WOODEN_PALLET));

  public static final RegistryObject<Block> STACKED_WOODEN_PALLETS =
      deferredRegister.register("stacked_wooden_pallets",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(2.0F, 3.0F)
                  .sound(SoundType.WOOD)
                  .noOcclusion(),
              __ -> BlockShapes.STACKED_WOODEN_PALLETS));

  public static final RegistryObject<Block> CRATE_ON_WOODEN_PALLET =
      deferredRegister.register("crate_on_wooden_pallet",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.WOOD)
                  .strength(2.0F, 3.0F)
                  .sound(SoundType.WOOD)
                  .noOcclusion(),
              __ -> Shapes.block()));

  public static final RegistryObject<Block> SECURITY_CAMERA =
      deferredRegister.register("security_camera",
          () -> new WaterloggedOrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.WOOD)
                  .noOcclusion(),
              BlockShapes.SECURITY_CAMERA));

  public static final RegistryObject<Block> WASHING_MACHINE =
      deferredRegister.register("washing_machine",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              __ -> Shapes.block()));

  public static final RegistryObject<Block> BROKEN_WASHING_MACHINE =
      deferredRegister.register("broken_washing_machine",
          () -> new OrientableBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noOcclusion(),
              __ -> Shapes.block()));

  public static final RegistryObject<Block> CLOTHING_RACK =
      deferredRegister.register("clothing_rack",
          () -> new ClothingRackBlock(
              BlockBehaviour.Properties.of(Material.METAL)
                  .strength(5.0F)
                  .sound(SoundType.METAL)
                  .noCollission()));

  public static final RegistryObject<Block> CHERRY_LEAVES =
      deferredRegister.register("cherry_leaves",
          () -> new LeavesBlock(Block.Properties.of(Material.LEAVES).noOcclusion()));

  public static final RegistryObject<Block> BASALT_BRICKS =
      deferredRegister.register("basalt_bricks",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> CHISELED_BASALT =
      deferredRegister.register("chiseled_basalt",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> CHERRY_PLANKS =
      deferredRegister.register("cherry_planks",
          () -> new Block(Block.Properties.of(Material.WOOD)));
  public static final RegistryObject<Block> MOSSY_BASALT_BRICKS =
      deferredRegister.register("mossy_basalt_bricks",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> POLISHED_BASALT_BRICKS =
      deferredRegister.register("polished_basalt_bricks",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> CRACKED_POLISHED_BASALT_BRICKS =
      deferredRegister.register("cracked_polished_basalt_bricks",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> CRACKED_BASALT_BRICKS =
      deferredRegister.register("cracked_basalt_bricks",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> CHERRY_LOG =
      deferredRegister.register("cherry_log",
          () -> new RotatedPillarBlock(
              BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                  .strength(2.0F)
                  .sound(SoundType.WOOD)));

  public static final RegistryObject<Block> STRIPPED_CHERRY_LOG =
      deferredRegister.register("stripped_cherry_log",
          () -> new RotatedPillarBlock(
              BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_PINK)
                  .strength(2.0F)
                  .sound(SoundType.WOOD)));

  public static final RegistryObject<Block> COUNTER_SOLID =
      deferredRegister.register("counter_solid",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> KITCHEN_TILE =
      deferredRegister.register("kitchen_tile",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> ANDESITE_KITCHEN_TILE =
      deferredRegister.register("andesite_kitchen_tile",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> GRANITE_KITCHEN_TILE =
      deferredRegister.register("granite_kitchen_tile",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> DIORITE_KITCHEN_TILE =
      deferredRegister.register("diorite_kitchen_tile",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> OBSIDIAN_KITCHEN_TILE =
      deferredRegister.register("obsidian_kitchen_tile",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> POLISHED_BASALT_KITCHEN_BRICK =
      deferredRegister.register("polished_basalt_kitchen_tile",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> CRYING_OBSIDIAN_KITCHEN_TILE =
      deferredRegister.register("crying_obsidian_kitchen_tile",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> POLISHED_OBSIDIAN =
      deferredRegister.register("polished_obsidian",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> POLISHED_CRYING_OBSIDIAN =
      deferredRegister.register("polished_crying_obsidian",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> CHERRY_BOOKSHELF =
      deferredRegister.register("cherry_bookshelf",
          () -> new Block(Block.Properties.of(Material.WOOD)));

  public static final RegistryObject<Block> ROAD_BLANK =
      deferredRegister.register("road_blank",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> ROAD_SOLID =
      deferredRegister.register("road_solid",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f),
              __ -> Shapes.block()));

  public static final RegistryObject<Block> ROAD_DOUBLE =
      deferredRegister.register("road_double",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f),
              __ -> Shapes.block()));

  public static final RegistryObject<Block> ROAD_BROKEN =
      deferredRegister.register("road_broken",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f),
              __ -> Shapes.block()));

  public static final RegistryObject<Block> SANDBAG =
      deferredRegister.register("sandbag",
          () -> new Block(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> SANDBAG_SLAB =
      deferredRegister.register("sandbag_slab",
          () -> new SlabBlock(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> ROAD_BLANK_SLAB =
      deferredRegister.register("road_blank_slab",
          () -> new SlabBlock(Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> ROAD_BROKEN_SLAB =
      deferredRegister.register("road_broken_slab",
          () -> new OrientableSlabBlockBase(
              Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> ROAD_SOLID_SLAB =
      deferredRegister.register("road_solid_slab",
          () -> new OrientableSlabBlockBase(
              Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> ROAD_DOUBLE_SLAB =
      deferredRegister.register("road_double_slab",
          () -> new OrientableSlabBlockBase(
              Block.Properties.of(Material.STONE).strength(5.0f, 5.0f)));

  public static final RegistryObject<Block> STOP_SIGN =
      deferredRegister.register("stop_sign",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE).strength(5.0f, 5.0f).noOcclusion(),
              BlockShapes.STOP_SIGN));

  public static final RegistryObject<Block> TRAFFIC_LIGHT =
      deferredRegister.register("traffic_light",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE).strength(5.0f, 5.0f).noOcclusion(),
              BlockShapes.TRAFFIC_LIGHT));

  public static final RegistryObject<Block> ROAD_BARRIER =
      deferredRegister.register("road_barrier",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE).strength(5.0f, 5.0f).noOcclusion(),
              BlockShapes.ROAD_BARRIER));

  public static final RegistryObject<Block> POLE_BARRIER =
      deferredRegister.register("pole_barrier",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE).strength(5.0f, 5.0f).noOcclusion(),
              BlockShapes.POLE_BARRIER));

  public static final RegistryObject<Block> STEEL_POLE_BARRIER =
      deferredRegister.register("steel_pole_barrier",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE).strength(5.0f, 5.0f).noOcclusion(),
              BlockShapes.STEEL_POLE_BARRIER));

  public static final RegistryObject<Block> STEEL_POLE_BARRIER_SLAB =
      deferredRegister.register("steel_pole_barrier_slab",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE).strength(5.0f, 5.0f).noOcclusion(),
              BlockShapes.SLAB_STEEL_POLE_BARRIER));

  public static final RegistryObject<Block> POLE_BARRIER_UNLIT =
      deferredRegister.register("pole_barrier_unlit",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE).strength(5.0f, 5.0f).noOcclusion(),
              BlockShapes.UNLIT_POLE_BARRIER));

  public static final RegistryObject<Block> CONCRETE_BARRIER =
      deferredRegister.register("concrete_barrier",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE).strength(5.0f, 5.0f).noOcclusion(),
              BlockShapes.CONCRETE_BARRIER));

  public static final RegistryObject<Block> STRIPED_CONCRETE_BARRIER =
      deferredRegister.register("striped_concrete_barrier",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f)
                  .noOcclusion(),
              BlockShapes.STRIPED_CONCRETE_BARRIER));

  public static final RegistryObject<Block> CONCRETE_BARRIER_SLAB =
      deferredRegister.register("concrete_barrier_slab",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f)
                  .noOcclusion(),
              BlockShapes.SLAB_CONCRETE_BARRIER));

  public static final RegistryObject<Block> STRIPED_CONCRETE_BARRIER_SLAB =
      deferredRegister.register("striped_concrete_barrier_slab",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f)
                  .noOcclusion(),
              BlockShapes.SLAB_STRIPED_CONCRETE_BARRIER));

  public static final RegistryObject<Block> VENDING_MACHINE =
      deferredRegister.register("vending_machine",
          () -> new OrientableBlock(
              Block.Properties.of(Material.METAL)
                  .noOcclusion(),
              BlockShapes.DOUBLE_TALL_BLOCK));

  public static final RegistryObject<Block> STREET_LIGHT_BASE =
      deferredRegister.register("street_light_base",
          () -> new ShapedBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f)
                  .noOcclusion(),
              BlockShapes.STREET_LIGHT));

  public static final RegistryObject<Block> STREET_LIGHT_CURVE =
      deferredRegister.register("street_light_curve",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f)
                  .noOcclusion(),
              BlockShapes.STREET_LIGHT_CURVE));

  public static final RegistryObject<Block> STREET_LIGHT_DOUBLE_CURVE =
      deferredRegister.register("street_light_double_curve",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f)
                  .noOcclusion(),
              BlockShapes.STREET_LIGHT_DOUBLE_CURVE));

  public static final RegistryObject<Block> STREET_LIGHT_FLAT_CROSS =
      deferredRegister.register("street_light_flat_cross",
          () -> new ShapedBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f)
                  .noOcclusion(),
              BlockShapes.STREET_LIGHT_CROSS));

  public static final RegistryObject<Block> STREET_LIGHT_HEAD =
      deferredRegister.register("street_light_head",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f)
                  .noOcclusion()
                  .lightLevel(__ -> 15),
              BlockShapes.STREET_LIGHT_HORIZONTAL));

  public static final RegistryObject<Block> STREET_LIGHT_HORIZONTAL_POLE =
      deferredRegister.register("street_light_horizontal_pole",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f)
                  .noOcclusion(),
              BlockShapes.STREET_LIGHT_HORIZONTAL));

  public static final RegistryObject<Block> STREET_LIGHT_VERTICAL_CROSS =
      deferredRegister.register("street_light_vertical_cross",
          () -> new OrientableBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f)
                  .noOcclusion(),
              BlockShapes.STREET_LIGHT_VERTICAL_CROSS));

  public static final RegistryObject<Block> STREET_LIGHT_VERTICAL_POLE =
      deferredRegister.register("street_light_vertical_pole",
          () -> new ShapedBlock(
              Block.Properties.of(Material.STONE)
                  .strength(5.0f, 5.0f)
                  .noOcclusion(),
              BlockShapes.STREET_LIGHT_POLE));

  public static final RegistryObject<Block> GOLD_CHAIN =
      deferredRegister.register("gold_chain",
          () -> new ChainBlock(Block.Properties.of(Material.METAL)));

  public static final RegistryObject<Block> BASALT_LANTERN =
      deferredRegister.register("basalt_lantern",
          () -> new LanternBlock(BlockBehaviour.Properties.of(Material.METAL)
              .requiresCorrectToolForDrops()
              .strength(3.5F)
              .sound(SoundType.LANTERN)
              .lightLevel(__ -> 15)
              .noOcclusion()));

  public static final RegistryObject<Block> FRIDGE =
      deferredRegister.register("fridge",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.FRIDGE));

  public static final RegistryObject<Block> COUNTER =
      deferredRegister.register("counter",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.COUNTER));

  public static final RegistryObject<Block> COUNTER_CORNER =
      deferredRegister.register("counter_corner",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.COUNTER_CORNER));

  public static final RegistryObject<Block> COUNTER_SINK =
      deferredRegister.register("counter_sink",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.COUNTER_SINK));

  public static final RegistryObject<Block> SINK =
      deferredRegister.register("sink",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.SINK));

  public static final RegistryObject<Block> PLATE =
      deferredRegister.register("plate",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.PLATE));

  public static final RegistryObject<Block> MILITARY_RADIO =
      deferredRegister.register("military_radio",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.MILITARY_RADIO));

  public static final RegistryObject<Block> RADIO =
      deferredRegister.register("radio",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.RADIO));

  public static final RegistryObject<Block> FLOWERPOT =
      deferredRegister.register("flowerpot",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.FLOWER_POT));

  public static final RegistryObject<Block> CERAMIC_POT =
      deferredRegister.register("ceramic_pot",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.FLOWER_POT));

  public static final RegistryObject<Block> STONE_POT =
      deferredRegister.register("stone_pot",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.FLOWER_POT));

  public static final RegistryObject<Block> WHITE_STOOL =
      deferredRegister.register("white_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> BLACK_STOOL =
      deferredRegister.register("black_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> RED_STOOL =
      deferredRegister.register("red_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> BLUE_STOOL =
      deferredRegister.register("blue_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> GREEN_STOOL =
      deferredRegister.register("green_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> ORANGE_STOOL =
      deferredRegister.register("orange_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> MAGENTA_STOOL =
      deferredRegister.register("magenta_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> LIGHT_BLUE_STOOL =
      deferredRegister.register("light_blue_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> YELLOW_STOOL =
      deferredRegister.register("yellow_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> LIME_STOOL =
      deferredRegister.register("lime_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> PINK_STOOL =
      deferredRegister.register("pink_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> GRAY_STOOL =
      deferredRegister.register("gray_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> LIGHT_GRAY_STOOL =
      deferredRegister.register("light_gray_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> CYAN_STOOL =
      deferredRegister.register("cyan_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> PURPLE_STOOL =
      deferredRegister.register("purple_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> BROWN_STOOL =
      deferredRegister.register("brown_stool",
          () -> new ShapedBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.STOOL));

  public static final RegistryObject<Block> WHITE_CHAIR =
      deferredRegister.register("white_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> BLACK_CHAIR =
      deferredRegister.register("black_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> RED_CHAIR =
      deferredRegister.register("red_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> BLUE_CHAIR =
      deferredRegister.register("blue_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> GREEN_CHAIR =
      deferredRegister.register("green_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> ORANGE_CHAIR =
      deferredRegister.register("orange_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> MAGENTA_CHAIR =
      deferredRegister.register("magenta_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> LIGHT_BLUE_CHAIR =
      deferredRegister.register("light_blue_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> YELLOW_CHAIR =
      deferredRegister.register("yellow_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> LIME_CHAIR =
      deferredRegister.register("lime_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> PINK_CHAIR =
      deferredRegister.register("pink_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> GRAY_CHAIR =
      deferredRegister.register("gray_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> LIGHT_GRAY_CHAIR =
      deferredRegister.register("light_gray_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> CYAN_CHAIR =
      deferredRegister.register("cyan_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> PURPLE_CHAIR =
      deferredRegister.register("purple_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> BROWN_CHAIR =
      deferredRegister.register("brown_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.STONE).noOcclusion(),
              BlockShapes.CHAIR));

  public static final RegistryObject<Block> OAK_TABLE =
      deferredRegister.register("oak_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> SPRUCE_TABLE =
      deferredRegister.register("spruce_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> JUNGLE_TABLE =
      deferredRegister.register("jungle_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> DARK_OAK_TABLE =
      deferredRegister.register("dark_oak_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> BIRCH_TABLE =
      deferredRegister.register("birch_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> ACACIA_TABLE =
      deferredRegister.register("acacia_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> CRIMSON_STEM_TABLE =
      deferredRegister.register("crimson_stem_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> WARPED_STEM_TABLE =
      deferredRegister.register("warped_stem_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> STRIPPED_OAK_TABLE =
      deferredRegister.register("stripped_oak_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> STRIPPED_SPRUCE_TABLE =
      deferredRegister.register("stripped_spruce_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> STRIPPED_JUNGLE_TABLE =
      deferredRegister.register("stripped_jungle_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> STRIPPED_DARK_OAK_TABLE =
      deferredRegister.register("stripped_dark_oak_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> STRIPPED_BIRCH_TABLE =
      deferredRegister.register("stripped_birch_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> STRIPPED_ACACIA_TABLE =
      deferredRegister.register("stripped_acacia_table",
          () -> new ShapedBlock(
              Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> STRIPPED_CRIMSON_STEM_TABLE =
      deferredRegister.register("stripped_crimson_stem_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> STRIPPED_WARPED_STEM_TABLE =
      deferredRegister.register("stripped_warped_stem_table",
          () -> new ShapedBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TABLE));

  public static final RegistryObject<Block> OAK_TALL_CHAIR =
      deferredRegister.register("oak_tall_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TALL_CHAIR));

  public static final RegistryObject<Block> DARK_OAK_TALL_CHAIR =
      deferredRegister.register("dark_oak_tall_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TALL_CHAIR));

  public static final RegistryObject<Block> ACACIA_TALL_CHAIR =
      deferredRegister.register("acacia_tall_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TALL_CHAIR));

  public static final RegistryObject<Block> JUNGLE_TALL_CHAIR =
      deferredRegister.register("jungle_tall_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TALL_CHAIR));

  public static final RegistryObject<Block> SPRUCE_TALL_CHAIR =
      deferredRegister.register("spruce_tall_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TALL_CHAIR));

  public static final RegistryObject<Block> BIRCH_TALL_CHAIR =
      deferredRegister.register("birch_tall_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TALL_CHAIR));

  public static final RegistryObject<Block> CRIMSON_STEM_TALL_CHAIR =
      deferredRegister.register("crimson_stem_tall_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TALL_CHAIR));

  public static final RegistryObject<Block> WARPED_STEM_TALL_CHAIR =
      deferredRegister.register("warped_stem_tall_chair",
          () -> new OrientableBlock(Block.Properties.of(Material.WOOD).noOcclusion(),
              BlockShapes.TALL_CHAIR));

  public static final RegistryObject<Block> LIGHT_SWITCH =
      deferredRegister.register("light_switch",
          () -> new OrientableBlock(Block.Properties.of(Material.METAL).noOcclusion(),
              BlockShapes.LIGHT_SWITCH, true));

  public static final RegistryObject<Block> ELECTRICAL_SOCKET =
      deferredRegister.register("electrical_socket",
          () -> new OrientableBlock(Block.Properties.of(Material.METAL).noOcclusion(),
              BlockShapes.ELECTRICAL_SOCKET, true));

  public static final RegistryObject<Block> ABANDONED_CAMPFIRE =
      deferredRegister.register("abandoned_campfire",
          () -> new OrientableBlock(
              Block.Properties.of(Material.WOOD, MaterialColor.PODZOL)
                  .strength(2.0F)
                  .sound(SoundType.WOOD)
                  .noOcclusion(),
              BlockShapes.ABANDONED_CAMPFIRE));
}
