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

package com.craftingdead.decoration.world.item;

import com.craftingdead.decoration.CraftingDeadDecoration;
import com.craftingdead.decoration.world.level.block.DecorationBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DecorationItems {

  public static final DeferredRegister<Item> deferredRegister =
      DeferredRegister.create(ForgeRegistries.ITEMS, CraftingDeadDecoration.ID);

  public static final CreativeModeTab TAB =
      new CreativeModeTab(CraftingDeadDecoration.ID) {

        @Override
        public ItemStack makeIcon() {
          return new ItemStack(STACKED_WOODEN_PALLETS.get());
        }
      };

  public static final RegistryObject<BlockItem> FUSE_BOX =
      deferredRegister.register("fuse_box",
          () -> new BlockItem(DecorationBlocks.FUSE_BOX.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> AA_POSTER =
      deferredRegister.register("aa_poster",
          () -> new BlockItem(DecorationBlocks.AA_POSTER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> BLUE_GAS_CAN_1 =
      deferredRegister.register("blue_gas_can_1",
          () -> new BlockItem(DecorationBlocks.BLUE_GAS_CAN_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BLUE_GAS_CAN_2 =
      deferredRegister.register("blue_gas_can_2",
          () -> new BlockItem(DecorationBlocks.BLUE_GAS_CAN_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BLUE_GAS_CAN_3 =
      deferredRegister.register("blue_gas_can_3",
          () -> new BlockItem(DecorationBlocks.BLUE_GAS_CAN_3.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> GREEN_GAS_CAN_1 =
      deferredRegister.register("green_gas_can_1",
          () -> new BlockItem(DecorationBlocks.GREEN_GAS_CAN_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> GREEN_GAS_CAN_2 =
      deferredRegister.register("green_gas_can_2",
          () -> new BlockItem(DecorationBlocks.GREEN_GAS_CAN_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> GREEN_GAS_CAN_3 =
      deferredRegister.register("green_gas_can_3",
          () -> new BlockItem(DecorationBlocks.GREEN_GAS_CAN_3.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> RED_GAS_CAN_1 =
      deferredRegister.register("red_gas_can_1",
          () -> new BlockItem(DecorationBlocks.RED_GAS_CAN_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> RED_GAS_CAN_2 =
      deferredRegister.register("red_gas_can_2",
          () -> new BlockItem(DecorationBlocks.RED_GAS_CAN_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> RED_GAS_CAN_3 =
      deferredRegister.register("red_gas_can_3",
          () -> new BlockItem(DecorationBlocks.RED_GAS_CAN_3.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> BOX_STORE_SHELF_1 =
      deferredRegister.register("box_store_shelf_1",
          () -> new BlockItem(DecorationBlocks.BOX_STORE_SHELF_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BOX_STORE_SHELF_2 =
      deferredRegister.register("box_store_shelf_2",
          () -> new BlockItem(DecorationBlocks.BOX_STORE_SHELF_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BOX_STORE_SHELF_3 =
      deferredRegister.register("box_store_shelf_3",
          () -> new BlockItem(DecorationBlocks.BOX_STORE_SHELF_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BOX_STORE_SHELF_4 =
      deferredRegister.register("box_store_shelf_4",
          () -> new BlockItem(DecorationBlocks.BOX_STORE_SHELF_4.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> BOTTOM_BOX_STORE_SHELF_1 =
      deferredRegister.register("bottom_box_store_shelf_1",
          () -> new BlockItem(DecorationBlocks.BOTTOM_BOX_STORE_SHELF_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BOTTOM_BOX_STORE_SHELF_2 =
      deferredRegister.register("bottom_box_store_shelf_2",
          () -> new BlockItem(DecorationBlocks.BOTTOM_BOX_STORE_SHELF_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BOTTOM_BOX_STORE_SHELF_3 =
      deferredRegister.register("bottom_box_store_shelf_3",
          () -> new BlockItem(DecorationBlocks.BOTTOM_BOX_STORE_SHELF_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BOTTOM_BOX_STORE_SHELF_4 =
      deferredRegister.register("bottom_box_store_shelf_4",
          () -> new BlockItem(DecorationBlocks.BOTTOM_BOX_STORE_SHELF_4.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> CAN_STORE_SHELF_1 =
      deferredRegister.register("can_store_shelf_1",
          () -> new BlockItem(DecorationBlocks.CAN_STORE_SHELF_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> CAN_STORE_SHELF_2 =
      deferredRegister.register("can_store_shelf_2",
          () -> new BlockItem(DecorationBlocks.CAN_STORE_SHELF_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> CAN_STORE_SHELF_3 =
      deferredRegister.register("can_store_shelf_3",
          () -> new BlockItem(DecorationBlocks.CAN_STORE_SHELF_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> CAN_STORE_SHELF_4 =
      deferredRegister.register("can_store_shelf_4",
          () -> new BlockItem(DecorationBlocks.CAN_STORE_SHELF_4.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> BOTTOM_CAN_STORE_SHELF_1 =
      deferredRegister.register("bottom_can_store_shelf_1",
          () -> new BlockItem(DecorationBlocks.BOTTOM_CAN_STORE_SHELF_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BOTTOM_CAN_STORE_SHELF_2 =
      deferredRegister.register("bottom_can_store_shelf_2",
          () -> new BlockItem(DecorationBlocks.BOTTOM_CAN_STORE_SHELF_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BOTTOM_CAN_STORE_SHELF_3 =
      deferredRegister.register("bottom_can_store_shelf_3",
          () -> new BlockItem(DecorationBlocks.BOTTOM_CAN_STORE_SHELF_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BOTTOM_CAN_STORE_SHELF_4 =
      deferredRegister.register("bottom_can_store_shelf_4",
          () -> new BlockItem(DecorationBlocks.BOTTOM_CAN_STORE_SHELF_4.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> STORE_SHELF_1 =
      deferredRegister.register("store_shelf_1",
          () -> new BlockItem(DecorationBlocks.STORE_SHELF_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> STORE_SHELF_2 =
      deferredRegister.register("store_shelf_2",
          () -> new BlockItem(DecorationBlocks.STORE_SHELF_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> STORE_SHELF_3 =
      deferredRegister.register("store_shelf_3",
          () -> new BlockItem(DecorationBlocks.STORE_SHELF_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> STORE_SHELF_4 =
      deferredRegister.register("store_shelf_4",
          () -> new BlockItem(DecorationBlocks.STORE_SHELF_4.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> BOTTOM_STORE_SHELF_1 =
      deferredRegister.register("bottom_store_shelf_1",
          () -> new BlockItem(DecorationBlocks.BOTTOM_STORE_SHELF_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BOTTOM_STORE_SHELF_2 =
      deferredRegister.register("bottom_store_shelf_2",
          () -> new BlockItem(DecorationBlocks.BOTTOM_STORE_SHELF_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BOTTOM_STORE_SHELF_3 =
      deferredRegister.register("bottom_store_shelf_3",
          () -> new BlockItem(DecorationBlocks.BOTTOM_STORE_SHELF_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BOTTOM_STORE_SHELF_4 =
      deferredRegister.register("bottom_store_shelf_4",
          () -> new BlockItem(DecorationBlocks.BOTTOM_STORE_SHELF_4.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> TOOL_1 =
      deferredRegister.register("tool_1",
          () -> new BlockItem(DecorationBlocks.TOOL_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> TOOL_2 =
      deferredRegister.register("tool_2",
          () -> new BlockItem(DecorationBlocks.TOOL_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> TOOL_3 =
      deferredRegister.register("tool_3",
          () -> new BlockItem(DecorationBlocks.TOOL_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> TOOL_4 =
      deferredRegister.register("tool_4",
          () -> new BlockItem(DecorationBlocks.TOOL_4.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> TOILET =
      deferredRegister.register("toilet",
          () -> new BlockItem(DecorationBlocks.TOILET.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> FULL_TOILET =
      deferredRegister.register("full_toilet",
          () -> new BlockItem(DecorationBlocks.FULL_TOILET.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> DIRTY_TOILET =
      deferredRegister.register("dirty_toilet",
          () -> new BlockItem(DecorationBlocks.DIRTY_TOILET.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> FULL_DIRTY_TOILET =
      deferredRegister.register("full_dirty_toilet",
          () -> new BlockItem(DecorationBlocks.FULL_DIRTY_TOILET.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> OLD_TELEVISION =
      deferredRegister.register("old_television",
          () -> new BlockItem(DecorationBlocks.OLD_TELEVISION.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> CRACKED_OLD_TELEVISION =
      deferredRegister.register("cracked_old_television",
          () -> new BlockItem(DecorationBlocks.CRACKED_OLD_TELEVISION.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> TELEVISION =
      deferredRegister.register("television",
          () -> new BlockItem(DecorationBlocks.TELEVISION.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> CRACKED_TELEVISION =
      deferredRegister.register("cracked_television",
          () -> new BlockItem(DecorationBlocks.CRACKED_TELEVISION.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> OFFICE_CHAIR_1 =
      deferredRegister.register("office_chair_1",
          () -> new BlockItem(DecorationBlocks.OFFICE_CHAIR_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> OFFICE_CHAIR_2 =
      deferredRegister.register("office_chair_2",
          () -> new BlockItem(DecorationBlocks.OFFICE_CHAIR_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> OFFICE_CHAIR_3 =
      deferredRegister.register("office_chair_3",
          () -> new BlockItem(DecorationBlocks.OFFICE_CHAIR_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> RIPPED_OFFICE_CHAIR_1 =
      deferredRegister.register("ripped_office_chair_1",
          () -> new BlockItem(DecorationBlocks.RIPPED_OFFICE_CHAIR_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> RIPPED_OFFICE_CHAIR_2 =
      deferredRegister.register("ripped_office_chair_2",
          () -> new BlockItem(DecorationBlocks.RIPPED_OFFICE_CHAIR_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> RIPPED_OFFICE_CHAIR_3 =
      deferredRegister.register("ripped_office_chair_3",
          () -> new BlockItem(DecorationBlocks.RIPPED_OFFICE_CHAIR_3.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> LAPTOP_1 =
      deferredRegister.register("laptop_1",
          () -> new BlockItem(DecorationBlocks.LAPTOP_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> LAPTOP_2 =
      deferredRegister.register("laptop_2",
          () -> new BlockItem(DecorationBlocks.LAPTOP_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> LAPTOP_3 =
      deferredRegister.register("laptop_3",
          () -> new BlockItem(DecorationBlocks.LAPTOP_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BROKEN_LAPTOP =
      deferredRegister.register("broken_laptop",
          () -> new BlockItem(DecorationBlocks.BROKEN_LAPTOP.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> YELLOW_GAS_TANK =
      deferredRegister.register("yellow_gas_tank",
          () -> new BlockItem(DecorationBlocks.YELLOW_GAS_TANK.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BLUE_GAS_TANK =
      deferredRegister.register("blue_gas_tank",
          () -> new BlockItem(DecorationBlocks.BLUE_GAS_TANK.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> GRAY_GAS_TANK =
      deferredRegister.register("gray_gas_tank",
          () -> new BlockItem(DecorationBlocks.GRAY_GAS_TANK.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> COMPUTER_1 =
      deferredRegister.register("computer_1",
          () -> new BlockItem(DecorationBlocks.COMPUTER_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> COMPUTER_2 =
      deferredRegister.register("computer_2",
          () -> new BlockItem(DecorationBlocks.COMPUTER_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> COMPUTER_3 =
      deferredRegister.register("computer_3",
          () -> new BlockItem(DecorationBlocks.COMPUTER_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BROKEN_COMPUTER =
      deferredRegister.register("broken_computer",
          () -> new BlockItem(DecorationBlocks.BROKEN_COMPUTER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> BLUE_BARREL_1 =
      deferredRegister.register("blue_barrel_1",
          () -> new BlockItem(DecorationBlocks.BLUE_BARREL_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> WORN_BLUE_BARREL_1 =
      deferredRegister.register("worn_blue_barrel_1",
          () -> new BlockItem(DecorationBlocks.WORN_BLUE_BARREL_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BLUE_BARREL_2 =
      deferredRegister.register("blue_barrel_2",
          () -> new BlockItem(DecorationBlocks.BLUE_BARREL_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> WORN_BLUE_BARREL_2 =
      deferredRegister.register("worn_blue_barrel_2",
          () -> new BlockItem(DecorationBlocks.WORN_BLUE_BARREL_2.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> GRAY_BARREL_1 =
      deferredRegister.register("gray_barrel_1",
          () -> new BlockItem(DecorationBlocks.GRAY_BARREL_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> WORN_GRAY_BARREL_1 =
      deferredRegister.register("worn_gray_barrel_1",
          () -> new BlockItem(DecorationBlocks.WORN_GRAY_BARREL_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> GRAY_BARREL_2 =
      deferredRegister.register("gray_barrel_2",
          () -> new BlockItem(DecorationBlocks.GRAY_BARREL_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> WORN_GRAY_BARREL_2 =
      deferredRegister.register("worn_gray_barrel_2",
          () -> new BlockItem(DecorationBlocks.WORN_GRAY_BARREL_2.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> GREEN_BARREL_1 =
      deferredRegister.register("green_barrel_1",
          () -> new BlockItem(DecorationBlocks.GREEN_BARREL_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> WORN_GREEN_BARREL_1 =
      deferredRegister.register("worn_green_barrel_1",
          () -> new BlockItem(DecorationBlocks.WORN_GREEN_BARREL_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> GREEN_BARREL_2 =
      deferredRegister.register("green_barrel_2",
          () -> new BlockItem(DecorationBlocks.GREEN_BARREL_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> WORN_GREEN_BARREL_2 =
      deferredRegister.register("worn_green_barrel_2",
          () -> new BlockItem(DecorationBlocks.WORN_GREEN_BARREL_2.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> RED_BARREL_1 =
      deferredRegister.register("red_barrel_1",
          () -> new BlockItem(DecorationBlocks.RED_BARREL_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> WORN_RED_BARREL_1 =
      deferredRegister.register("worn_red_barrel_1",
          () -> new BlockItem(DecorationBlocks.WORN_RED_BARREL_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> RED_BARREL_2 =
      deferredRegister.register("red_barrel_2",
          () -> new BlockItem(DecorationBlocks.RED_BARREL_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> WORN_RED_BARREL_2 =
      deferredRegister.register("worn_red_barrel_2",
          () -> new BlockItem(DecorationBlocks.WORN_RED_BARREL_2.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> OAK_PLANK_BARRICADE_1 =
      deferredRegister.register("oak_plank_barricade_1",
          () -> new BlockItem(DecorationBlocks.OAK_PLANK_BARRICADE_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> OAK_PLANK_BARRICADE_2 =
      deferredRegister.register("oak_plank_barricade_2",
          () -> new BlockItem(DecorationBlocks.OAK_PLANK_BARRICADE_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> OAK_PLANK_BARRICADE_3 =
      deferredRegister.register("oak_plank_barricade_3",
          () -> new BlockItem(DecorationBlocks.OAK_PLANK_BARRICADE_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> SPRUCE_PLANK_BARRICADE_1 =
      deferredRegister.register("spruce_plank_barricade_1",
          () -> new BlockItem(DecorationBlocks.SPRUCE_PLANK_BARRICADE_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> SPRUCE_PLANK_BARRICADE_2 =
      deferredRegister.register("spruce_plank_barricade_2",
          () -> new BlockItem(DecorationBlocks.SPRUCE_PLANK_BARRICADE_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> SPRUCE_PLANK_BARRICADE_3 =
      deferredRegister.register("spruce_plank_barricade_3",
          () -> new BlockItem(DecorationBlocks.SPRUCE_PLANK_BARRICADE_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BIRCH_PLANK_BARRICADE_1 =
      deferredRegister.register("birch_plank_barricade_1",
          () -> new BlockItem(DecorationBlocks.BIRCH_PLANK_BARRICADE_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BIRCH_PLANK_BARRICADE_2 =
      deferredRegister.register("birch_plank_barricade_2",
          () -> new BlockItem(DecorationBlocks.BIRCH_PLANK_BARRICADE_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> BIRCH_PLANK_BARRICADE_3 =
      deferredRegister.register("birch_plank_barricade_3",
          () -> new BlockItem(DecorationBlocks.BIRCH_PLANK_BARRICADE_3.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> DARK_OAK_PLANK_BARRICADE_1 =
      deferredRegister.register("dark_oak_plank_barricade_1",
          () -> new BlockItem(DecorationBlocks.DARK_OAK_PLANK_BARRICADE_1.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> DARK_OAK_PLANK_BARRICADE_2 =
      deferredRegister.register("dark_oak_plank_barricade_2",
          () -> new BlockItem(DecorationBlocks.DARK_OAK_PLANK_BARRICADE_2.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<BlockItem> DARK_OAK_PLANK_BARRICADE_3 =
      deferredRegister.register("dark_oak_plank_barricade_3",
          () -> new BlockItem(DecorationBlocks.DARK_OAK_PLANK_BARRICADE_3.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> BATTEN_LIGHT =
      deferredRegister.register("batten_light",
          () -> new BlockItem(DecorationBlocks.BATTEN_LIGHT.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> LIT_BATTEN_LIGHT =
      deferredRegister.register("lit_batten_light",
          () -> new BlockItem(DecorationBlocks.LIT_BATTEN_LIGHT.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> WOODEN_PALLET =
      deferredRegister.register("wooden_pallet",
          () -> new BlockItem(DecorationBlocks.WOODEN_PALLET.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> STACKED_WOODEN_PALLETS =
      deferredRegister.register("stacked_wooden_pallets",
          () -> new BlockItem(DecorationBlocks.STACKED_WOODEN_PALLETS.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> CRATE_ON_WOODEN_PALLET =
      deferredRegister.register("crate_on_wooden_pallet",
          () -> new BlockItem(DecorationBlocks.CRATE_ON_WOODEN_PALLET.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> SECURITY_CAMERA =
      deferredRegister.register("security_camera",
          () -> new BlockItem(DecorationBlocks.SECURITY_CAMERA.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> WASHING_MACHINE =
      deferredRegister.register("washing_machine",
          () -> new BlockItem(DecorationBlocks.WASHING_MACHINE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> BROKEN_WASHING_MACHINE =
      deferredRegister.register("broken_washing_machine",
          () -> new BlockItem(DecorationBlocks.BROKEN_WASHING_MACHINE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<BlockItem> CLOTHING_RACK =
      deferredRegister.register("clothing_rack",
          () -> new BlockItem(DecorationBlocks.CLOTHING_RACK.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CHERRY_LEAVES_ITEM =
      deferredRegister.register("cherry_leaves",
          () -> new BlockItem(DecorationBlocks.CHERRY_LEAVES.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> BASALT_BRICKS_ITEM =
      deferredRegister.register("basalt_bricks",
          () -> new BlockItem(DecorationBlocks.BASALT_BRICKS.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CHISELED_BASALT_ITEM =
      deferredRegister.register("chiseled_basalt",
          () -> new BlockItem(DecorationBlocks.CHISELED_BASALT.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CHERRY_PLANKS_ITEM =
      deferredRegister.register("cherry_planks",
          () -> new BlockItem(DecorationBlocks.CHERRY_PLANKS.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> MOSSY_BASALT_BRICKS_ITEM =
      deferredRegister.register("mossy_basalt_bricks",
          () -> new BlockItem(DecorationBlocks.MOSSY_BASALT_BRICKS.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> POLISHED_BASALT_BRICKS_ITEM =
      deferredRegister.register("polished_basalt_bricks",
          () -> new BlockItem(DecorationBlocks.POLISHED_BASALT_BRICKS.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CRACKED_POLISHED_BASALT_BRICKS_ =
      deferredRegister.register("cracked_polished_basalt_bricks",
          () -> new BlockItem(DecorationBlocks.CRACKED_POLISHED_BASALT_BRICKS.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CRACKED_BASALT_BRICKS_ITEM =
      deferredRegister.register("cracked_basalt_bricks",
          () -> new BlockItem(DecorationBlocks.CRACKED_BASALT_BRICKS.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CHERRY_LOG_ITEM =
      deferredRegister.register("cherry_log",
          () -> new BlockItem(DecorationBlocks.CHERRY_LOG.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STRIPPED_CHERRY_LOG_ITEM =
      deferredRegister.register("stripped_cherry_log",
          () -> new BlockItem(DecorationBlocks.STRIPPED_CHERRY_LOG.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> COUNTER_SOLID_ITEM =
      deferredRegister.register("counter_solid",
          () -> new BlockItem(DecorationBlocks.COUNTER_SOLID.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> KITCHEN_TILE_ITEM =
      deferredRegister.register("kitchen_tile",
          () -> new BlockItem(DecorationBlocks.KITCHEN_TILE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ANDESITE_KITCHEN_TILE_ITEM =
      deferredRegister.register("andesite_kitchen_tile",
          () -> new BlockItem(DecorationBlocks.ANDESITE_KITCHEN_TILE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> GRANITE_KITCHEN_TILE_ITEM =
      deferredRegister.register("granite_kitchen_tile",
          () -> new BlockItem(DecorationBlocks.GRANITE_KITCHEN_TILE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> DIORITE_KITCHEN_TILE_ITEM =
      deferredRegister.register("diorite_kitchen_tile",
          () -> new BlockItem(DecorationBlocks.DIORITE_KITCHEN_TILE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> OBSIDIAN_KITCHEN_TILE_ITEM =
      deferredRegister.register("obsidian_kitchen_tile",
          () -> new BlockItem(DecorationBlocks.OBSIDIAN_KITCHEN_TILE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> POLISHED_BASALT_KITCHEN_BRICK_ITEM =
      deferredRegister.register("polished_basalt_kitchen_tile",
          () -> new BlockItem(DecorationBlocks.POLISHED_BASALT_KITCHEN_BRICK.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CRYING_OBSIDIAN_KITCHEN_TILE_ITEM =
      deferredRegister.register("crying_obsidian_kitchen_tile",
          () -> new BlockItem(DecorationBlocks.CRYING_OBSIDIAN_KITCHEN_TILE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> POLISHED_OBSIDIAN_ITEM =
      deferredRegister.register("polished_obsidian",
          () -> new BlockItem(DecorationBlocks.POLISHED_OBSIDIAN.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> POLISHED_CRYING_OBSIDIAN_ITEM =
      deferredRegister.register("polished_crying_obsidian",
          () -> new BlockItem(DecorationBlocks.POLISHED_CRYING_OBSIDIAN.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CHERRY_BOOKSHELF_ITEM =
      deferredRegister.register("cherry_bookshelf",
          () -> new BlockItem(DecorationBlocks.CHERRY_BOOKSHELF.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ROAD_BLANK_ITEM =
      deferredRegister.register("road_blank",
          () -> new BlockItem(DecorationBlocks.ROAD_BLANK.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ROAD_SOLID_ITEM =
      deferredRegister.register("road_solid",
          () -> new BlockItem(DecorationBlocks.ROAD_SOLID.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ROAD_DOUBLE_ITEM =
      deferredRegister.register("road_double",
          () -> new BlockItem(DecorationBlocks.ROAD_DOUBLE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ROAD_BROKEN_ITEM =
      deferredRegister.register("road_broken",
          () -> new BlockItem(DecorationBlocks.ROAD_BROKEN.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SANDBAG_ITEM =
      deferredRegister.register("sandbag",
          () -> new BlockItem(DecorationBlocks.SANDBAG.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> SANDBAG_SLAB_ITEM =
      deferredRegister.register("sandbag_slab",
          () -> new BlockItem(DecorationBlocks.SANDBAG_SLAB.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ROAD_BLANK_SLAB_ITEM =
      deferredRegister.register("road_blank_slab",
          () -> new BlockItem(DecorationBlocks.ROAD_BLANK_SLAB.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ROAD_BROKEN_SLAB_ITEM =
      deferredRegister.register("road_broken_slab",
          () -> new BlockItem(DecorationBlocks.ROAD_BROKEN_SLAB.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ROAD_SOLID_SLAB_ITEM =
      deferredRegister.register("road_solid_slab",
          () -> new BlockItem(DecorationBlocks.ROAD_SOLID_SLAB.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ROAD_DOUBLE_SLAB_ITEM =
      deferredRegister.register("road_double_slab",
          () -> new BlockItem(DecorationBlocks.ROAD_DOUBLE_SLAB.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STOP_SIGN_ITEM =
      deferredRegister.register("stop_sign",
          () -> new BlockItem(DecorationBlocks.STOP_SIGN.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRAFFIC_LIGHT_ITEM =
      deferredRegister.register("traffic_light",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_LIGHT.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ROAD_BARRIER_ITEM =
      deferredRegister.register("road_barrier",
          () -> new BlockItem(DecorationBlocks.ROAD_BARRIER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> POLE_BARRIER_ITEM =
      deferredRegister.register("pole_barrier",
          () -> new BlockItem(DecorationBlocks.POLE_BARRIER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_POLE_BARRIER_ITEM =
      deferredRegister.register("steel_pole_barrier",
          () -> new BlockItem(DecorationBlocks.STEEL_POLE_BARRIER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STEEL_POLE_BARRIER_SLAB_ITEM =
      deferredRegister.register("steel_pole_barrier_slab",
          () -> new BlockItem(DecorationBlocks.STEEL_POLE_BARRIER_SLAB.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> POLE_BARRIER_UNLIT_ITEM =
      deferredRegister.register("pole_barrier_unlit",
          () -> new BlockItem(DecorationBlocks.POLE_BARRIER_UNLIT.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CONCRETE_BARRIER_ITEM =
      deferredRegister.register("concrete_barrier",
          () -> new BlockItem(DecorationBlocks.CONCRETE_BARRIER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STRIPED_CONCRETE_BARRIER_ITEM =
      deferredRegister.register("striped_concrete_barrier",
          () -> new BlockItem(DecorationBlocks.STRIPED_CONCRETE_BARRIER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> CONCRETE_BARRIER_SLAB_ITEM =
      deferredRegister.register("concrete_barrier_slab",
          () -> new BlockItem(DecorationBlocks.CONCRETE_BARRIER_SLAB.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STRIPED_CONCRETE_BARRIER_SLAB_ITEM =
      deferredRegister.register("striped_concrete_barrier_slab",
          () -> new BlockItem(DecorationBlocks.STRIPED_CONCRETE_BARRIER_SLAB.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> VENDING_MACHINE_ITEM =
      deferredRegister.register("vending_machine",
          () -> new BlockItem(DecorationBlocks.VENDING_MACHINE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STREET_LIGHT_BASE_ITEM =
      deferredRegister.register("street_light_base",
          () -> new BlockItem(DecorationBlocks.STREET_LIGHT_BASE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STREET_LIGHT_CURVE_ITEM =
      deferredRegister.register("street_light_curve",
          () -> new BlockItem(DecorationBlocks.STREET_LIGHT_CURVE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STREET_LIGHT_DOUBLE_CURVE_ITEM =
      deferredRegister.register("street_light_double_curve",
          () -> new BlockItem(DecorationBlocks.STREET_LIGHT_DOUBLE_CURVE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STREET_LIGHT_FLAT_CROSS_ITEM =
      deferredRegister.register("street_light_flat_cross",
          () -> new BlockItem(DecorationBlocks.STREET_LIGHT_FLAT_CROSS.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STREET_LIGHT_HEAD_ITEM =
      deferredRegister.register("street_light_head",
          () -> new BlockItem(DecorationBlocks.STREET_LIGHT_HEAD.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STREET_LIGHT_HORIZONTAL_POLE_ITEM =
      deferredRegister.register("street_light_horizontal_pole",
          () -> new BlockItem(DecorationBlocks.STREET_LIGHT_HORIZONTAL_POLE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STREET_LIGHT_VERTICAL_CROSS_ITEM =
      deferredRegister.register("street_light_vertical_cross",
          () -> new BlockItem(DecorationBlocks.STREET_LIGHT_VERTICAL_CROSS.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> STREET_LIGHT_VERTICAL_POLE_ITEM =
      deferredRegister.register("street_light_vertical_pole",
          () -> new BlockItem(DecorationBlocks.STREET_LIGHT_VERTICAL_POLE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> GOLD_CHAIN_ITEM =
      deferredRegister.register("gold_chain",
          () -> new BlockItem(DecorationBlocks.GOLD_CHAIN.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> BASALT_LANTERN_ITEM =
      deferredRegister.register("basalt_lantern",
          () -> new BlockItem(DecorationBlocks.BASALT_LANTERN.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> FRIDGE_ITEM =
      deferredRegister.register("fridge",
          () -> new BlockItem(DecorationBlocks.FRIDGE.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> COUNTER_ITEM =
      deferredRegister.register("counter",
          () -> new BlockItem(DecorationBlocks.COUNTER.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> COUNTER_CORNER_ITEM =
      deferredRegister.register("counter_corner",
          () -> new BlockItem(DecorationBlocks.COUNTER_CORNER.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> COUNTER_SINK_ITEM =
      deferredRegister.register("counter_sink",
          () -> new BlockItem(DecorationBlocks.COUNTER_SINK.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SINK_ITEM =
      deferredRegister.register("sink",
          () -> new BlockItem(DecorationBlocks.SINK.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> PLATE_ITEM =
      deferredRegister.register("plate",
          () -> new BlockItem(DecorationBlocks.PLATE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> MILITARY_RADIO_ITEM =
      deferredRegister.register("military_radio",
          () -> new BlockItem(DecorationBlocks.MILITARY_RADIO.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> RADIO_ITEM =
      deferredRegister.register("radio",
          () -> new BlockItem(DecorationBlocks.RADIO.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> FLOWERPOT_ITEM =
      deferredRegister.register("flowerpot",
          () -> new BlockItem(DecorationBlocks.FLOWERPOT.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> CERAMIC_POT_ITEM =
      deferredRegister.register("ceramic_pot",
          () -> new BlockItem(DecorationBlocks.CERAMIC_POT.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> STONE_POT_ITEM =
      deferredRegister.register("stone_pot",
          () -> new BlockItem(DecorationBlocks.STONE_POT.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> WHITE_STOOL_ITEM =
      deferredRegister.register("white_stool",
          () -> new BlockItem(DecorationBlocks.WHITE_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> BLACK_STOOL_ITEM =
      deferredRegister.register("black_stool",
          () -> new BlockItem(DecorationBlocks.BLACK_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> RED_STOOL_ITEM =
      deferredRegister.register("red_stool",
          () -> new BlockItem(DecorationBlocks.RED_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> BLUE_STOOL_ITEM =
      deferredRegister.register("blue_stool",
          () -> new BlockItem(DecorationBlocks.BLUE_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> GREEN_STOOL_ITEM =
      deferredRegister.register("green_stool",
          () -> new BlockItem(DecorationBlocks.GREEN_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> ORANGE_STOOL_ITEM =
      deferredRegister.register("orange_stool",
          () -> new BlockItem(DecorationBlocks.ORANGE_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> MAGENTA_STOOL_ITEM =
      deferredRegister.register("magenta_stool",
          () -> new BlockItem(DecorationBlocks.MAGENTA_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> LIGHT_BLUE_STOOL_ITEM =
      deferredRegister.register("light_blue_stool",
          () -> new BlockItem(DecorationBlocks.LIGHT_BLUE_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> YELLOW_STOOL_ITEM =
      deferredRegister.register("yellow_stool",
          () -> new BlockItem(DecorationBlocks.YELLOW_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> LIME_STOOL_ITEM =
      deferredRegister.register("lime_stool",
          () -> new BlockItem(DecorationBlocks.LIME_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> PINK_STOOL_ITEM =
      deferredRegister.register("pink_stool",
          () -> new BlockItem(DecorationBlocks.PINK_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> GRAY_STOOL_ITEM =
      deferredRegister.register("gray_stool",
          () -> new BlockItem(DecorationBlocks.GRAY_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> LIGHT_GRAY_STOOL_ITEM =
      deferredRegister.register("light_gray_stool",
          () -> new BlockItem(DecorationBlocks.LIGHT_GRAY_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> CYAN_STOOL_ITEM =
      deferredRegister.register("cyan_stool",
          () -> new BlockItem(DecorationBlocks.CYAN_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> PURPLE_STOOL_ITEM =
      deferredRegister.register("purple_stool",
          () -> new BlockItem(DecorationBlocks.PURPLE_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> BROWN_STOOL_ITEM =
      deferredRegister.register("brown_stool",
          () -> new BlockItem(DecorationBlocks.BROWN_STOOL.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> WHITE_CHAIR_ITEM =
      deferredRegister.register("white_chair",
          () -> new BlockItem(DecorationBlocks.WHITE_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> BLACK_CHAIR_ITEM =
      deferredRegister.register("black_chair",
          () -> new BlockItem(DecorationBlocks.BLACK_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> RED_CHAIR_ITEM =
      deferredRegister.register("red_chair",
          () -> new BlockItem(DecorationBlocks.RED_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> BLUE_CHAIR_ITEM =
      deferredRegister.register("blue_chair",
          () -> new BlockItem(DecorationBlocks.BLUE_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> GREEN_CHAIR_ITEM =
      deferredRegister.register("green_chair",
          () -> new BlockItem(DecorationBlocks.GREEN_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> ORANGE_CHAIR_ITEM =
      deferredRegister.register("orange_chair",
          () -> new BlockItem(DecorationBlocks.ORANGE_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> MAGENTA_CHAIR_ITEM =
      deferredRegister.register("magenta_chair",
          () -> new BlockItem(DecorationBlocks.MAGENTA_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> LIGHT_BLUE_CHAIR_ITEM =
      deferredRegister.register("light_blue_chair",
          () -> new BlockItem(DecorationBlocks.LIGHT_BLUE_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> YELLOW_CHAIR_ITEM =
      deferredRegister.register("yellow_chair",
          () -> new BlockItem(DecorationBlocks.YELLOW_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> LIME_CHAIR_ITEM =
      deferredRegister.register("lime_chair",
          () -> new BlockItem(DecorationBlocks.LIME_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> PINK_CHAIR_ITEM =
      deferredRegister.register("pink_chair",
          () -> new BlockItem(DecorationBlocks.PINK_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> GRAY_CHAIR_ITEM =
      deferredRegister.register("gray_chair",
          () -> new BlockItem(DecorationBlocks.GRAY_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> LIGHT_GRAY_CHAIR_ITEM =
      deferredRegister.register("light_gray_chair",
          () -> new BlockItem(DecorationBlocks.LIGHT_GRAY_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> CYAN_CHAIR_ITEM =
      deferredRegister.register("cyan_chair",
          () -> new BlockItem(DecorationBlocks.CYAN_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> PURPLE_CHAIR_ITEM =
      deferredRegister.register("purple_chair",
          () -> new BlockItem(DecorationBlocks.PURPLE_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> BROWN_CHAIR_ITEM =
      deferredRegister.register("brown_chair",
          () -> new BlockItem(DecorationBlocks.BROWN_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> OAK_TABLE_ITEM =
      deferredRegister.register("oak_table",
          () -> new BlockItem(DecorationBlocks.OAK_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SPRUCE_TABLE_ITEM =
      deferredRegister.register("spruce_table",
          () -> new BlockItem(DecorationBlocks.SPRUCE_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> JUNGLE_TABLE_ITEM =
      deferredRegister.register("jungle_table",
          () -> new BlockItem(DecorationBlocks.JUNGLE_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> DARK_OAK_TABLE_ITEM =
      deferredRegister.register("dark_oak_table",
          () -> new BlockItem(DecorationBlocks.DARK_OAK_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> BIRCH_TABLE_ITEM =
      deferredRegister.register("birch_table",
          () -> new BlockItem(DecorationBlocks.BIRCH_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> ACACIA_TABLE_ITEM =
      deferredRegister.register("acacia_table",
          () -> new BlockItem(DecorationBlocks.ACACIA_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> CRIMSON_STEM_TABLE_ITEM =
      deferredRegister.register("crimson_stem_table",
          () -> new BlockItem(DecorationBlocks.CRIMSON_STEM_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> WARPED_STEM_TABLE_ITEM =
      deferredRegister.register("warped_stem_table",
          () -> new BlockItem(DecorationBlocks.WARPED_STEM_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> STRIPPED_OAK_TABLE_ITEM =
      deferredRegister.register("stripped_oak_table",
          () -> new BlockItem(DecorationBlocks.STRIPPED_OAK_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> STRIPPED_SPRUCE_TABLE_ITEM =
      deferredRegister.register("stripped_spruce_table",
          () -> new BlockItem(DecorationBlocks.STRIPPED_SPRUCE_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> STRIPPED_JUNGLE_TABLE_ITEM =
      deferredRegister.register("stripped_jungle_table",
          () -> new BlockItem(DecorationBlocks.STRIPPED_JUNGLE_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> STRIPPED_DARK_OAK_TABLE_ITEM =
      deferredRegister.register("stripped_dark_oak_table",
          () -> new BlockItem(DecorationBlocks.STRIPPED_DARK_OAK_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> STRIPPED_BIRCH_TABLE_ITEM =
      deferredRegister.register("stripped_birch_table",
          () -> new BlockItem(DecorationBlocks.STRIPPED_BIRCH_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> STRIPPED_ACACIA_TABLE_ITEM =
      deferredRegister.register("stripped_acacia_table",
          () -> new BlockItem(DecorationBlocks.STRIPPED_ACACIA_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> STRIPPED_CRIMSON_STEM_TABLE_ITEM =
      deferredRegister.register("stripped_crimson_stem_table",
          () -> new BlockItem(DecorationBlocks.STRIPPED_CRIMSON_STEM_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> STRIPPED_WARPPED_STEM_TABLE_ITEM =
      deferredRegister.register("stripped_warped_stem_table",
          () -> new BlockItem(DecorationBlocks.STRIPPED_WARPED_STEM_TABLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> OAK_TALL_CHAIR_ITEM =
      deferredRegister.register("oak_tall_chair",
          () -> new BlockItem(DecorationBlocks.OAK_TALL_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> DARK_OAK_TALL_CHAIR_ITEM =
      deferredRegister.register("dark_oak_tall_chair",
          () -> new BlockItem(DecorationBlocks.DARK_OAK_TALL_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> ACACIA_TALL_CHAIR_ITEM =
      deferredRegister.register("acacia_tall_chair",
          () -> new BlockItem(DecorationBlocks.ACACIA_TALL_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> JUNGLE_TALL_CHAIR_ITEM =
      deferredRegister.register("jungle_tall_chair",
          () -> new BlockItem(DecorationBlocks.JUNGLE_TALL_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SPRUCE_TALL_CHAIR_ITEM =
      deferredRegister.register("spruce_tall_chair",
          () -> new BlockItem(DecorationBlocks.SPRUCE_TALL_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> BIRCH_TALL_CHAIR_ITEM =
      deferredRegister.register("birch_tall_chair",
          () -> new BlockItem(DecorationBlocks.BIRCH_TALL_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> CRIMSON_STEM_TALL_CHAIR_ITEM =
      deferredRegister.register("crimson_stem_tall_chair",
          () -> new BlockItem(DecorationBlocks.CRIMSON_STEM_TALL_CHAIR.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> WARPED_STEM_TALL_CHAIR_ITEM =
      deferredRegister.register("warped_stem_tall_chair",
          () -> new BlockItem(DecorationBlocks.WARPED_STEM_TALL_CHAIR.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> LIGHT_SWITCH =
      deferredRegister.register("light_switch",
          () -> new BlockItem(DecorationBlocks.LIGHT_SWITCH.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ELECTRICAL_SOCKET =
      deferredRegister.register("electrical_socket",
          () -> new BlockItem(DecorationBlocks.ELECTRICAL_SOCKET.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> ABANDONED_CAMPFIRE =
      deferredRegister.register("abandoned_campfire",
          () -> new BlockItem(DecorationBlocks.ABANDONED_CAMPFIRE.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> ABANDONED_CAMPFIRE_WITH_POT =
      deferredRegister.register("abandoned_campfire_with_pot",
          () -> new BlockItem(DecorationBlocks.ABANDONED_CAMPFIRE_WITH_POT.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> SLEEPING_BAG_CLOSED =
      deferredRegister.register("sleeping_bag_closed",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_CLOSED.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_CLOSED_BLACK =
      deferredRegister.register("sleeping_bag_closed_black",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_CLOSED_BLACK.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_CLOSED_BLUE =
      deferredRegister.register("sleeping_bag_closed_blue",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_CLOSED_BLUE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_CLOSED_DARKGREEN =
      deferredRegister.register("sleeping_bag_closed_darkgreen",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_CLOSED_DARKGREEN.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_CLOSED_ORANGE =
      deferredRegister.register("sleeping_bag_closed_orange",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_CLOSED_ORANGE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_CLOSED_PINK =
      deferredRegister.register("sleeping_bag_closed_pink",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_CLOSED_PINK.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_CLOSED_PURPLE =
      deferredRegister.register("sleeping_bag_closed_purple",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_CLOSED_PURPLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_CLOSED_RED =
      deferredRegister.register("sleeping_bag_closed_red",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_CLOSED_RED.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_CLOSED_YELLOW =
      deferredRegister.register("sleeping_bag_closed_yellow",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_CLOSED_YELLOW.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> SLEEPING_BAG_OPEN =
      deferredRegister.register("sleeping_bag_open",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_OPEN.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_OPEN_BLACK =
      deferredRegister.register("sleeping_bag_open_black",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_OPEN_BLACK.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_OPEN_BLUE =
      deferredRegister.register("sleeping_bag_open_blue",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_OPEN_BLUE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_OPEN_DARKGREEN =
      deferredRegister.register("sleeping_bag_open_darkgreen",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_OPEN_DARKGREEN.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_OPEN_ORANGE =
      deferredRegister.register("sleeping_bag_open_orange",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_OPEN_ORANGE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_OPEN_PINK =
      deferredRegister.register("sleeping_bag_open_pink",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_OPEN_PINK.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_OPEN_PURPLE =
      deferredRegister.register("sleeping_bag_open_purple",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_OPEN_PURPLE.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_OPEN_RED =
      deferredRegister.register("sleeping_bag_open_red",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_OPEN_RED.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> SLEEPING_BAG_OPEN_YELLOW =
      deferredRegister.register("sleeping_bag_open_yellow",
          () -> new BlockItem(DecorationBlocks.SLEEPING_BAG_OPEN_YELLOW.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> BOXES_OF_BULLETS =
      deferredRegister.register("box_of_bullets",
          () -> new BlockItem(DecorationBlocks.BOXES_OF_BULLETS.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> BOXES_OF_SHOTGUN_SHELLS_RED =
      deferredRegister.register("boxes_of_shotgun_shells_red",
          () -> new BlockItem(DecorationBlocks.BOXES_OF_SHOTGUN_SHELLS_RED.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> BOXES_OF_SHOTGUN_SHELLS_GREEN =
      deferredRegister.register("boxes_of_shotgun_shells_green",
          () -> new BlockItem(DecorationBlocks.BOXES_OF_SHOTGUN_SHELLS_GREEN.get(),
              new Item.Properties().tab(TAB)));
  public static final RegistryObject<Item> BOXES_OF_SHOTGUN_SHELLS_BLUE =
      deferredRegister.register("boxes_of_shotgun_shells_blue",
          () -> new BlockItem(DecorationBlocks.BOXES_OF_SHOTGUN_SHELLS_BLUE.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> SEWER_OPENING =
      deferredRegister.register("brown_stained_glass",
          () -> new BlockItem(DecorationBlocks.SEWER_OPENING.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> HL2_GLASS =
      deferredRegister.register("glass",
          () -> new BlockItem(DecorationBlocks.HL2_GLASS.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> HL2_TRASH =
      deferredRegister.register("lime_stained_glass",
          () -> new BlockItem(DecorationBlocks.HL2_TRASH.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> HL2_TRASH_2 =
      deferredRegister.register("pink_stained_glass",
          () -> new BlockItem(DecorationBlocks.HL2_TRASH_2.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> WALL_BUSH =
      deferredRegister.register("lime_stained_glass_pane",
          () -> new BlockItem(DecorationBlocks.WALL_BUSH.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> COBBLESTONE =
      deferredRegister.register("cobblestone",
          () -> new BlockItem(DecorationBlocks.COBBLESTONE.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> SMALL_FENCE =
      deferredRegister.register("nether_brick_fence",
          () -> new BlockItem(DecorationBlocks.SMALL_FENCE.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> TRAFFIC_SIGNS_POLE =
      deferredRegister.register("traffic_signs_01",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_POLE.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> TRAFFIC_SIGNS_EMPTY =
      deferredRegister.register("traffic_signs_02",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_EMPTY.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED20 =
      deferredRegister.register("traffic_signs_speed20",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED20.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED25 =
      deferredRegister.register("traffic_signs_speed25",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED25.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED30 =
      deferredRegister.register("traffic_signs_speed30",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED30.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED35 =
      deferredRegister.register("traffic_signs_speed35",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED35.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED40 =
      deferredRegister.register("traffic_signs_speed40",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED40.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED45 =
      deferredRegister.register("traffic_signs_speed45",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED45.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED50 =
      deferredRegister.register("traffic_signs_speed50",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED50.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED55 =
      deferredRegister.register("traffic_signs_speed55",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED55.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED60 =
      deferredRegister.register("traffic_signs_speed60",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED60.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED65 =
      deferredRegister.register("traffic_signs_speed65",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED65.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED70 =
      deferredRegister.register("traffic_signs_speed70",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED70.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED75 =
      deferredRegister.register("traffic_signs_speed75",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED75.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TRAFFIC_SIGNS_SPEED80 =
      deferredRegister.register("traffic_signs_speed80",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_SPEED80.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> TRAFFIC_SIGNS_DAMAGED_BULLET =
      deferredRegister.register("traffic_signs_damaged_bullet",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_DAMAGED_BULLET.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> TRAFFIC_SIGNS_DAMAGED =
      deferredRegister.register("traffic_signs_damaged",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_DAMAGED.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> TRAFFIC_SIGNS_NOTSAFE =
      deferredRegister.register("traffic_signs_notsafe",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_NOTSAFE.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> TRAFFIC_SIGNS_RADIOACTIVE =
      deferredRegister.register("traffic_signs_radioactive",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_RADIOACTIVE.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> TRAFFIC_SIGNS_RADIOACTIVE_DAMAGED =
      deferredRegister.register("traffic_signs_radioactive_damaged",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_RADIOACTIVE_DAMAGED.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> TRAFFIC_SIGNS_RADIOACTIVE_03 =
      deferredRegister.register("traffic_signs_03",
          () -> new BlockItem(DecorationBlocks.TRAFFIC_SIGNS_03.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED20 =
      deferredRegister.register("w_traffic_signs_speed20",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED20.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED25 =
      deferredRegister.register("w_traffic_signs_speed25",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED25.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED30 =
      deferredRegister.register("w_traffic_signs_speed30",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED30.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED35 =
      deferredRegister.register("w_traffic_signs_speed35",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED35.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED40 =
      deferredRegister.register("w_traffic_signs_speed40",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED40.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED45 =
      deferredRegister.register("w_traffic_signs_speed45",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED45.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED50 =
      deferredRegister.register("w_traffic_signs_speed50",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED50.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED55 =
      deferredRegister.register("w_traffic_signs_speed55",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED55.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED60 =
      deferredRegister.register("w_traffic_signs_speed60",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED60.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED65 =
      deferredRegister.register("w_traffic_signs_speed65",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED65.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED70 =
      deferredRegister.register("w_traffic_signs_speed70",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED70.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED75 =
      deferredRegister.register("w_traffic_signs_speed75",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED75.get(),
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_SPEED80 =
      deferredRegister.register("w_traffic_signs_speed80",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_SPEED80.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_DAMAGED_BULLET =
      deferredRegister.register("w_traffic_signs_damaged_bullet",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_DAMAGED_BULLET.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_DAMAGED =
      deferredRegister.register("w_traffic_signs_damaged",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_DAMAGED.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_NOTSAFE =
      deferredRegister.register("w_traffic_signs_notsafe",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_NOTSAFE.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_RADIOACTIVE =
      deferredRegister.register("w_traffic_signs_radioactive",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_RADIOACTIVE.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> W_TRAFFIC_SIGNS_RADIOACTIVE_DAMAGED =
      deferredRegister.register("w_traffic_signs_radioactive_damaged",
          () -> new BlockItem(DecorationBlocks.W_TRAFFIC_SIGNS_RADIOACTIVE_DAMAGED.get(),
              new Item.Properties().tab(TAB)));
  
  //HL2Pack Items
  public static final RegistryObject<Item> HEALTH_CHARGER =
      deferredRegister.register("acacia_button",
          () -> new BlockItem(DecorationBlocks.HEALTH_CHARGER.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> FENCE =
      deferredRegister.register("acacia_fence_gate",
          () -> new BlockItem(DecorationBlocks.FENCE.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> METAL_DUCT =
      deferredRegister.register("acacia_stairs",
          () -> new BlockItem(DecorationBlocks.METAL_DUCT.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> PAINTING_1 =
      deferredRegister.register("black_stained_glass_pane",
          () -> new BlockItem(DecorationBlocks.PAINTING_1.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<Item> CRATE =
      deferredRegister.register("dead_brain_coral",
          () -> new BlockItem(DecorationBlocks.CRATE.get(),
              new Item.Properties().tab(TAB)));
  
  // AE12 Glass
  public static final RegistryObject<BlockItem> QUARTZ_GLASS =
      deferredRegister.register("quartz_glass",
          () -> new BlockItem(DecorationBlocks.QUARTZ_GLASS.get(),
              new Item.Properties().tab(TAB)));
  
  public static final RegistryObject<BlockItem> VIBRANT_QUARTZ_GLASS =
      deferredRegister.register("quartz_vibrant_glass",
          () -> new BlockItem(DecorationBlocks.VIBRANT_QUARTZ_GLASS.get(),
              new Item.Properties().tab(TAB)));
}
