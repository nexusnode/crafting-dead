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
import com.craftingdead.decoration.world.item.DecorationItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DecorationItemModelProvider extends ItemModelProvider {

  public DecorationItemModelProvider(DataGenerator generator,
      ExistingFileHelper existingFileHelper) {
    super(generator, CraftingDeadDecoration.ID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    this.withExistingParent(DecorationItems.CLOTHING_RACK.getId().toString(),
        this.modLoc("block/clothing_rack_inventory"));
  }
}
