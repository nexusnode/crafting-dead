package com.craftingdead.mod.common.registry.forge;

import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.common.block.BlockBarbedWire;
import com.craftingdead.mod.common.block.BlockLoot;
import com.craftingdead.mod.common.block.BlockRoad;
import com.craftingdead.mod.common.block.BlockRoadDirectional;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(CraftingDead.MOD_ID)
@Mod.EventBusSubscriber
public class BlockRegistry {

	public static final Block ROAD = null;
	public static final Block LINED_ROAD = null;
	public static final Block BROKEN_LINED_ROAD = null;
	public static final Block BARBED_WIRE = null;
	public static final Block RESIDENTIAL_LOOT = null;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(appendRegistryName("road", new BlockRoad()),
				appendRegistryName("lined_road", new BlockRoadDirectional()),
				appendRegistryName("broken_lined_road", new BlockRoadDirectional()),
				appendRegistryName("barbed_wire", new BlockBarbedWire(1)).setHardness(2F),
				appendRegistryName("residential_loot",
						new BlockLoot(0xFFFFFF, ImmutableMap.of(new ResourceLocation(CraftingDead.MOD_ID, "road"), 1)))
								.setHardness(-1F));
	}

	private static Block appendRegistryName(String registryName, Block block) {
		return block.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, registryName))
				.setTranslationKey(String.format("%s%s%s", CraftingDead.MOD_ID, ".", registryName));
	}

}
