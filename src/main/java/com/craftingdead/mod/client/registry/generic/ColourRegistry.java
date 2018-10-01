package com.craftingdead.mod.client.registry.generic;

import com.craftingdead.mod.client.renderer.color.BasicColourHandler;
import com.craftingdead.mod.common.block.BlockLoot;
import com.craftingdead.mod.common.registry.forge.BlockRegistry;

import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;

public class ColourRegistry {

	public static void registerColourHandlers(BlockColors blockColors, ItemColors itemColors) {
		registerLootColourHandler((BlockLoot) BlockRegistry.RESIDENTIAL_LOOT, blockColors, itemColors);
	}

	private static void registerLootColourHandler(BlockLoot loot, BlockColors blockColors, ItemColors itemColors) {
		blockColors.registerBlockColorHandler(new BasicColourHandler(loot.getColour()), loot);
		itemColors.registerItemColorHandler(new BasicColourHandler(loot.getColour()), loot);
	}

}