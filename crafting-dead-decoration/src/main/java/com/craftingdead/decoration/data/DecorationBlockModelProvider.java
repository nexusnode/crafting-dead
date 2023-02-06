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

import java.util.Set;
import com.craftingdead.decoration.CraftingDeadDecoration;
import com.craftingdead.decoration.world.level.block.DecorationBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class DecorationBlockModelProvider extends BlockModelProvider {

  public DecorationBlockModelProvider(DataGenerator generator,
      ExistingFileHelper existingFileHelper) {
    super(generator, CraftingDeadDecoration.ID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    this.modelVariant("oak", "plank_barricade", 3);
    this.modelVariant("spruce", "plank_barricade", 3);
    this.modelVariant("birch", "plank_barricade", 3);
    this.modelVariant("dark_oak", "plank_barricade", 3);

    Set.of("blue", "gray", "green", "red")
        .forEach(color -> {
          this.modelVariant(color, "barrel", 2);
          this.modelVariant("worn_" + color, "barrel", 2);
        });

    this.textureVariant(DecorationBlocks.COMPUTER_1, "computer");
    this.textureVariant(DecorationBlocks.COMPUTER_2, "computer");
    this.textureVariant(DecorationBlocks.COMPUTER_3, "computer");

    this.textureVariant(DecorationBlocks.YELLOW_GAS_TANK, "gas_tank");
    this.textureVariant(DecorationBlocks.BLUE_GAS_TANK, "gas_tank");
    this.textureVariant(DecorationBlocks.GRAY_GAS_TANK, "gas_tank");

    this.textureVariant(DecorationBlocks.LAPTOP_1, "laptop");
    this.textureVariant(DecorationBlocks.LAPTOP_2, "laptop");
    this.textureVariant(DecorationBlocks.LAPTOP_3, "laptop");

    this.modelVariant("ripped", "office_chair", 3);

    this.textureVariant(DecorationBlocks.CRACKED_OLD_TELEVISION, "old_television");
    this.textureVariant(DecorationBlocks.CRACKED_TELEVISION, "television");

    this.textureVariant(DecorationBlocks.FULL_TOILET, "toilet");
    this.textureVariant(DecorationBlocks.DIRTY_TOILET, "toilet");
    this.textureVariant(DecorationBlocks.FULL_DIRTY_TOILET, "toilet");

    this.bottomStoreShelfs("box_store_shelf");
    this.bottomStoreShelfs("can_store_shelf");
    this.bottomStoreShelfs("store_shelf");

    this.modelVariant("blue", "gas_can", 3);
    this.modelVariant("green", "gas_can", 3);
    this.modelVariant("red", "gas_can", 3);
  }

  private void bottomStoreShelfs(String model) {
    var texture1 = this.modLoc("block/bottom_store_shelf_1");
    var texture = this.modLoc("block/bottom_store_shelf");
    for (int i = 1; i <= 4; i++) {
      var tex = i == 1 ? texture1 : texture;
      var name = "%s:block/bottom_%s_%s".formatted(CraftingDeadDecoration.ID, model, i);
      this.singleTexture(name, this.modLoc("block/%s_%s".formatted(model, i)), tex);
    }
  }

  private void textureVariant(RegistryObject<? extends Block> block, String model) {
    this.singleTexture(block.getId().toString(), this.modLoc("block/" + model),
        this.modLoc("block/" + block.getId().getPath().toString()));
  }

  private void modelVariant(String variant, String model, int count) {
    this.modelVariant(variant, model, 1, count);
  }

  private void modelVariant(String variant, String model, int from, int to) {
    var texture = this.modLoc("block/%s_%s".formatted(variant, model));
    for (int i = from; i <= to; i++) {
      var name = "%s:block/%s_%s_%s".formatted(CraftingDeadDecoration.ID, variant, model, i);
      this.singleTexture(name, this.modLoc("block/%s_%s".formatted(model, i)), texture);
    }
  }
}
