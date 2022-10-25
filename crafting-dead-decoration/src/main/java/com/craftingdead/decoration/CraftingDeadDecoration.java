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

package com.craftingdead.decoration;

import com.craftingdead.decoration.client.ClientDist;
import com.craftingdead.decoration.data.DecorationBlockStateProvider;
import com.craftingdead.decoration.world.item.DecorationItems;
import com.craftingdead.decoration.world.level.block.DecorationBlocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod(CraftingDeadDecoration.ID)
public class CraftingDeadDecoration {

  public static final String ID = "craftingdeaddecoration";

  public CraftingDeadDecoration() {
    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientDist::new);

    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleGatherData);

    DecorationBlocks.deferredRegister.register(modEventBus);
    DecorationItems.deferredRegister.register(modEventBus);
  }

  private void handleGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var existingFileHelper = event.getExistingFileHelper();
    if (event.includeClient()) {
      generator.addProvider(new DecorationBlockStateProvider(generator, existingFileHelper));
    }
  }
}
