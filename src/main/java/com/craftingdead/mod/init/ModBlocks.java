package com.craftingdead.mod.init;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.block.BlockLoot;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(CraftingDead.MOD_ID)
@Mod.EventBusSubscriber
public class ModBlocks {

	public static final Block RESIDENTIAL_LOOT = null;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry()
				.registerAll(appendRegistryName("residential_loot",
						new BlockLoot(0xFFFFFF, ImmutableMap.of(new ResourceLocation(CraftingDead.MOD_ID, "road"), 1)))
								.setHardness(-1F));
	}

	private static Block appendRegistryName(String registryName, Block block) {
		return block.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, registryName))
				.setTranslationKey(String.format("%s%s%s", CraftingDead.MOD_ID, ".", registryName));
	}

}
