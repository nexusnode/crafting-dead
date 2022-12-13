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

package com.craftingdead.decoration.data.loot;

import com.craftingdead.decoration.world.level.block.DecorationBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class DecorationBlockLoot extends BlockLoot {

  @Override
  protected void addTables() {
    for (var entry : DecorationBlocks.deferredRegister.getEntries()) {
      this.dropSelf(entry.get());
    }
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return DecorationBlocks.deferredRegister.getEntries().stream()
        .map(RegistryObject::get)
        .toList();
  }
}
