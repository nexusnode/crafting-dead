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
