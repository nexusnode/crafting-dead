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

import org.slf4j.Logger;
import com.craftingdead.decoration.client.ClientDist;
import com.craftingdead.decoration.data.DecorationBlockStateProvider;
import com.craftingdead.decoration.data.DecorationRecipeProvider;
import com.craftingdead.decoration.data.loot.DecorationLootTableProvider;
import com.craftingdead.decoration.world.item.DecorationItems;
import com.craftingdead.decoration.world.level.block.DecorationBlocks;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(CraftingDeadDecoration.ID)
public class CraftingDeadDecoration {

  public static final String ID = "craftingdeaddecoration";
  private static final String OLD_ID = "cityblocks";

  private static final Logger logger = LogUtils.getLogger();

  public CraftingDeadDecoration() {
    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientDist::new);

    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleGatherData);

    DecorationBlocks.deferredRegister.register(modEventBus);
    DecorationItems.deferredRegister.register(modEventBus);

    MinecraftForge.EVENT_BUS.addGenericListener(Block.class, this::handleMissingBlocks);
    MinecraftForge.EVENT_BUS.addGenericListener(Item.class, this::handleMissingItems);
  }

  private void handleGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var existingFileHelper = event.getExistingFileHelper();
    if (event.includeClient()) {
      generator.addProvider(new DecorationBlockStateProvider(generator, existingFileHelper));
    } else if (event.includeServer()) {
      generator.addProvider(new DecorationLootTableProvider(generator));
      generator.addProvider(new DecorationRecipeProvider(generator));
    }
  }

  private void handleMissingItems(RegistryEvent.MissingMappings<Item> event) {
    var missingMappings = event.getMappings(OLD_ID);
    for (var mapping : missingMappings) {
      var newKey = new ResourceLocation(ID, mapping.key.getPath());
      var newValue = ForgeRegistries.ITEMS.getValue(newKey);
      if (newValue == null || newValue == Items.AIR) {
        throw new IllegalStateException("Failed to re-map: " + mapping.key.toString());
      }
      mapping.remap(newValue);
      logger.info("Remapped item {} -> {}/{}", mapping.key, newKey, newValue);
    }
  }

  private void handleMissingBlocks(RegistryEvent.MissingMappings<Block> event) {
    var missingMappings = event.getMappings(OLD_ID);
    for (var mapping : missingMappings) {
      var newKey = new ResourceLocation(ID, mapping.key.getPath());
      var newValue = ForgeRegistries.BLOCKS.getValue(newKey);
      if (newValue == null || newValue == Blocks.AIR) {
        throw new IllegalStateException("Failed to re-map: " + mapping.key.toString());
      }
      mapping.remap(newValue);
      logger.info("Remapped block {} -> {}/{}", mapping.key, newKey, newValue);
    }
  }
}
