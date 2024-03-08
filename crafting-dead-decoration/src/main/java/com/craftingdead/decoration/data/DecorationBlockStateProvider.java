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

package com.craftingdead.decoration.data;

import com.craftingdead.decoration.CraftingDeadDecoration;
import com.craftingdead.decoration.world.level.block.DecorationBlocks;
import com.craftingdead.decoration.world.level.block.DoubleBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class DecorationBlockStateProvider extends BlockStateProvider {

  public DecorationBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super(gen, CraftingDeadDecoration.ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    this.horizontalBlock(DecorationBlocks.WOODEN_PALLET);
    this.horizontalBlock(DecorationBlocks.STACKED_WOODEN_PALLETS);
    this.horizontalBlock(DecorationBlocks.CRATE_ON_WOODEN_PALLET);
    this.horizontalBlock(DecorationBlocks.SECURITY_CAMERA);
    this.horizontalBlock(DecorationBlocks.WASHING_MACHINE);
    this.horizontalBlock(DecorationBlocks.BROKEN_WASHING_MACHINE);
    this.horizontalBlock(DecorationBlocks.LIGHT_SWITCH);
    this.horizontalBlock(DecorationBlocks.ELECTRICAL_SOCKET);
    this.horizontalBlock(DecorationBlocks.ABANDONED_CAMPFIRE);

    this.horizontalBlock(DecorationBlocks.CLOTHING_RACK.get(),
        state -> this.blockModel("clothing_rack_"
            + state.getValue(DoubleBlock.PART).getSerializedName()));

    this.horizontalBlock(DecorationBlocks.BATTEN_LIGHT);
    this.horizontalBlock(DecorationBlocks.LIT_BATTEN_LIGHT);

    this.horizontalBlock(DecorationBlocks.OAK_PLANK_BARRICADE_1);
    this.horizontalBlock(DecorationBlocks.OAK_PLANK_BARRICADE_2);
    this.horizontalBlock(DecorationBlocks.OAK_PLANK_BARRICADE_3);
    this.horizontalBlock(DecorationBlocks.SPRUCE_PLANK_BARRICADE_1);
    this.horizontalBlock(DecorationBlocks.SPRUCE_PLANK_BARRICADE_2);
    this.horizontalBlock(DecorationBlocks.SPRUCE_PLANK_BARRICADE_3);
    this.horizontalBlock(DecorationBlocks.BIRCH_PLANK_BARRICADE_1);
    this.horizontalBlock(DecorationBlocks.BIRCH_PLANK_BARRICADE_2);
    this.horizontalBlock(DecorationBlocks.BIRCH_PLANK_BARRICADE_3);
    this.horizontalBlock(DecorationBlocks.DARK_OAK_PLANK_BARRICADE_1);
    this.horizontalBlock(DecorationBlocks.DARK_OAK_PLANK_BARRICADE_2);
    this.horizontalBlock(DecorationBlocks.DARK_OAK_PLANK_BARRICADE_3);

    this.horizontalBlock(DecorationBlocks.BLUE_BARREL_1);
    this.horizontalBlock(DecorationBlocks.WORN_BLUE_BARREL_1);
    this.horizontalBlock(DecorationBlocks.BLUE_BARREL_2);
    this.horizontalBlock(DecorationBlocks.WORN_BLUE_BARREL_2);

    this.horizontalBlock(DecorationBlocks.GRAY_BARREL_1);
    this.horizontalBlock(DecorationBlocks.WORN_GRAY_BARREL_1);
    this.horizontalBlock(DecorationBlocks.GRAY_BARREL_2);
    this.horizontalBlock(DecorationBlocks.WORN_GRAY_BARREL_2);

    this.horizontalBlock(DecorationBlocks.GREEN_BARREL_1);
    this.horizontalBlock(DecorationBlocks.WORN_GREEN_BARREL_1);
    this.horizontalBlock(DecorationBlocks.GREEN_BARREL_2);
    this.horizontalBlock(DecorationBlocks.WORN_GREEN_BARREL_2);

    this.horizontalBlock(DecorationBlocks.RED_BARREL_1);
    this.horizontalBlock(DecorationBlocks.WORN_RED_BARREL_1);
    this.horizontalBlock(DecorationBlocks.RED_BARREL_2);
    this.horizontalBlock(DecorationBlocks.WORN_RED_BARREL_2);

    this.horizontalBlock(DecorationBlocks.COMPUTER_1);
    this.horizontalBlock(DecorationBlocks.COMPUTER_2);
    this.horizontalBlock(DecorationBlocks.COMPUTER_3);
    this.horizontalBlock(DecorationBlocks.BROKEN_COMPUTER);

    this.horizontalBlock(DecorationBlocks.YELLOW_GAS_TANK);
    this.horizontalBlock(DecorationBlocks.BLUE_GAS_TANK);
    this.horizontalBlock(DecorationBlocks.GRAY_GAS_TANK);

    this.horizontalBlock(DecorationBlocks.LAPTOP_1);
    this.horizontalBlock(DecorationBlocks.LAPTOP_2);
    this.horizontalBlock(DecorationBlocks.LAPTOP_3);
    this.horizontalBlock(DecorationBlocks.BROKEN_LAPTOP);

    this.horizontalBlock(DecorationBlocks.OFFICE_CHAIR_1);
    this.horizontalBlock(DecorationBlocks.OFFICE_CHAIR_2);
    this.horizontalBlock(DecorationBlocks.OFFICE_CHAIR_3);
    this.horizontalBlock(DecorationBlocks.RIPPED_OFFICE_CHAIR_1);
    this.horizontalBlock(DecorationBlocks.RIPPED_OFFICE_CHAIR_2);
    this.horizontalBlock(DecorationBlocks.RIPPED_OFFICE_CHAIR_3);

    this.horizontalBlock(DecorationBlocks.OLD_TELEVISION);
    this.horizontalBlock(DecorationBlocks.CRACKED_OLD_TELEVISION);
    this.horizontalBlock(DecorationBlocks.TELEVISION);
    this.horizontalBlock(DecorationBlocks.CRACKED_TELEVISION);

    this.horizontalBlock(DecorationBlocks.TOILET);
    this.horizontalBlock(DecorationBlocks.FULL_TOILET);
    this.horizontalBlock(DecorationBlocks.DIRTY_TOILET);
    this.horizontalBlock(DecorationBlocks.FULL_DIRTY_TOILET);

    this.horizontalBlock(DecorationBlocks.TOOL_1);
    this.horizontalBlock(DecorationBlocks.TOOL_2);
    this.horizontalBlock(DecorationBlocks.TOOL_3);
    this.horizontalBlock(DecorationBlocks.TOOL_4);

    this.horizontalBlock(DecorationBlocks.BOX_STORE_SHELF_1);
    this.horizontalBlock(DecorationBlocks.BOX_STORE_SHELF_2);
    this.horizontalBlock(DecorationBlocks.BOX_STORE_SHELF_3);
    this.horizontalBlock(DecorationBlocks.BOX_STORE_SHELF_4);
    this.horizontalBlock(DecorationBlocks.BOTTOM_BOX_STORE_SHELF_1);
    this.horizontalBlock(DecorationBlocks.BOTTOM_BOX_STORE_SHELF_2);
    this.horizontalBlock(DecorationBlocks.BOTTOM_BOX_STORE_SHELF_3);
    this.horizontalBlock(DecorationBlocks.BOTTOM_BOX_STORE_SHELF_4);

    this.horizontalBlock(DecorationBlocks.CAN_STORE_SHELF_1);
    this.horizontalBlock(DecorationBlocks.CAN_STORE_SHELF_2);
    this.horizontalBlock(DecorationBlocks.CAN_STORE_SHELF_3);
    this.horizontalBlock(DecorationBlocks.CAN_STORE_SHELF_4);
    this.horizontalBlock(DecorationBlocks.BOTTOM_CAN_STORE_SHELF_1);
    this.horizontalBlock(DecorationBlocks.BOTTOM_CAN_STORE_SHELF_2);
    this.horizontalBlock(DecorationBlocks.BOTTOM_CAN_STORE_SHELF_3);
    this.horizontalBlock(DecorationBlocks.BOTTOM_CAN_STORE_SHELF_4);

    this.horizontalBlock(DecorationBlocks.STORE_SHELF_1);
    this.horizontalBlock(DecorationBlocks.STORE_SHELF_2);
    this.horizontalBlock(DecorationBlocks.STORE_SHELF_3);
    this.horizontalBlock(DecorationBlocks.STORE_SHELF_4);
    this.horizontalBlock(DecorationBlocks.BOTTOM_STORE_SHELF_1);
    this.horizontalBlock(DecorationBlocks.BOTTOM_STORE_SHELF_2);
    this.horizontalBlock(DecorationBlocks.BOTTOM_STORE_SHELF_3);
    this.horizontalBlock(DecorationBlocks.BOTTOM_STORE_SHELF_4);

    this.horizontalBlock(DecorationBlocks.BLUE_GAS_CAN_1);
    this.horizontalBlock(DecorationBlocks.BLUE_GAS_CAN_2);
    this.horizontalBlock(DecorationBlocks.BLUE_GAS_CAN_3);
    this.horizontalBlock(DecorationBlocks.GREEN_GAS_CAN_1);
    this.horizontalBlock(DecorationBlocks.GREEN_GAS_CAN_2);
    this.horizontalBlock(DecorationBlocks.GREEN_GAS_CAN_3);
    this.horizontalBlock(DecorationBlocks.RED_GAS_CAN_1);
    this.horizontalBlock(DecorationBlocks.RED_GAS_CAN_2);
    this.horizontalBlock(DecorationBlocks.RED_GAS_CAN_3);

    this.horizontalBlock(DecorationBlocks.AA_POSTER);

    this.horizontalBlock(DecorationBlocks.FUSE_BOX);
    
    this.simpleBlock(DecorationBlocks.QUARTZ_GLASS);
    this.simpleBlock(DecorationBlocks.VIBRANT_QUARTZ_GLASS);
    
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_CLOSED);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_CLOSED_BLACK);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_CLOSED_BLUE);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_CLOSED_DARKGREEN);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_CLOSED_ORANGE);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_CLOSED_PINK);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_CLOSED_PURPLE);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_CLOSED_RED);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_CLOSED_YELLOW);
    
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_OPEN);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_OPEN_BLACK);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_OPEN_BLUE);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_OPEN_DARKGREEN);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_OPEN_ORANGE);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_OPEN_PINK);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_OPEN_PURPLE);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_OPEN_RED);
    this.horizontalBlock(DecorationBlocks.SLEEPING_BAG_OPEN_YELLOW);

    // HL2Pack
    this.horizontalBlock(DecorationBlocks.HEALTH_CHARGER);
  }

  private void simpleBlock(RegistryObject<? extends Block> block) {
    var model = this.blockModel(block.getId().getPath());
    this.simpleBlock(block.get(), model);
    this.simpleBlockItem(block.get(), model);
  }

  private void horizontalBlock(RegistryObject<? extends Block> block) {
    this.horizontalBlock(block.get(), block.getId().getPath());
  }

  private void horizontalBlock(Block block, String path) {
    var model = this.blockModel(path);
    this.horizontalBlock(block, model);
    this.simpleBlockItem(block, model);
  }

  private ModelFile blockModel(String path) {
    return this.model("block/" + path);
  }

  private ModelFile model(String path) {
    return this.models().getExistingFile(new ResourceLocation(CraftingDeadDecoration.ID, path));
  }
}
