package com.craftingdead.mod.common.registry.forge;

import com.craftingdead.mod.common.CraftingDead;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(CraftingDead.MOD_ID)
@Mod.EventBusSubscriber
public class ItemRegistry {

	public static final Item ARC = null;
	public static final Item ROAD = null;
	public static final Item LINED_ROAD = null;
	public static final Item BROKEN_LINED_ROAD = null;
	public static final Item BARBED_WIRE = null;
	public static final Item RESIDENTIAL_LOOT = null;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(appendRegistryName("arc", new Item()),
				appendRegistryName("road", new ItemBlock(BlockRegistry.ROAD)),
				appendRegistryName("lined_road", new ItemBlock(BlockRegistry.LINED_ROAD)),
				appendRegistryName("broken_lined_road", new ItemBlock(BlockRegistry.BROKEN_LINED_ROAD)),
				appendRegistryName("barbed_wire", new ItemBlock(BlockRegistry.BARBED_WIRE)),
				appendRegistryName("residential_loot", new ItemBlock(BlockRegistry.RESIDENTIAL_LOOT)));
	}

	private static Item appendRegistryName(String registryName, Item item) {
		return item.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, registryName))
				.setTranslationKey(String.format("%s%s%s", CraftingDead.MOD_ID, ".", registryName));
	}

}
