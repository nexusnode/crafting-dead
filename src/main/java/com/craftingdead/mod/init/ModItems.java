package com.craftingdead.mod.init;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.item.ItemAcr;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(CraftingDead.MOD_ID)
@Mod.EventBusSubscriber
public class ModItems {

	public static final Item ACR = null;

	public static final Item ROAD = null;
	public static final Item LINED_ROAD = null;
	public static final Item BROKEN_LINED_ROAD = null;
	public static final Item BARBED_WIRE = null;

	public static final Item RESIDENTIAL_LOOT = null;

	public static final Item CLIP = null;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
				appendRegistryName("acr", new ItemAcr().setFireRate(80).setUseBowAndArrowPose(true)),
				appendRegistryName("road", new ItemBlock(ModBlocks.ROAD)),
				appendRegistryName("lined_road", new ItemBlock(ModBlocks.LINED_ROAD)),
				appendRegistryName("broken_lined_road", new ItemBlock(ModBlocks.BROKEN_LINED_ROAD)),
				appendRegistryName("barbed_wire", new ItemBlock(ModBlocks.BARBED_WIRE)),
				appendRegistryName("residential_loot", new ItemBlock(ModBlocks.RESIDENTIAL_LOOT)),
				appendRegistryName("clip", new Item()));
	}

	private static Item appendRegistryName(String registryName, Item item) {
		return item.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, registryName))
				.setTranslationKey(String.format("%s%s%s", CraftingDead.MOD_ID, ".", registryName));
	}

}
