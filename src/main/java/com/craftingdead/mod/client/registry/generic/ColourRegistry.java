package com.craftingdead.mod.client.registry.generic;

import com.craftingdead.mod.common.block.BlockLoot;
import com.craftingdead.mod.common.registry.forge.BlockRegistry;

import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;

public class ColourRegistry {

	public static void registerColourHandlers(BlockColors blockColors, ItemColors itemColors) {
		((BlockLoot) BlockRegistry.RESIDENTIAL_LOOT).registerColourHandler(blockColors, itemColors);
	}

}