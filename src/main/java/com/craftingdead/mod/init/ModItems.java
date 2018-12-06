package com.craftingdead.mod.init;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.item.ItemGun;

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
	public static final Item AK47 = null;

	public static final Item RESIDENTIAL_LOOT = null;

	public static final Item CLIP = null;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
				appendRegistryName("desert_eagle", new ItemGun(-1, 0, 8, 0, () -> ModSoundEvents.DESERT_EAGLE_SHOOT)),
				appendRegistryName("acr", new ItemGun(80, 0, 7, 0, () -> ModSoundEvents.ACR_SHOOT)),
				appendRegistryName("ak47", new ItemGun(80, 0, 7, 0, () -> ModSoundEvents.AK47_SHOOT)),
				appendRegistryName("residential_loot", new ItemBlock(ModBlocks.RESIDENTIAL_LOOT)),
				appendRegistryName("clip", new Item()));
	}

	private static Item appendRegistryName(String registryName, Item item) {
		return item.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, registryName))
				.setTranslationKey(String.format("%s%s%s", CraftingDead.MOD_ID, ".", registryName));
	}

}
