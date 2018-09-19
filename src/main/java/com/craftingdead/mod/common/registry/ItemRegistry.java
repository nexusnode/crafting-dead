package com.craftingdead.mod.common.registry;

import com.craftingdead.mod.common.item.ItemArc;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemRegistry {

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(new ItemArc());
	}

}
