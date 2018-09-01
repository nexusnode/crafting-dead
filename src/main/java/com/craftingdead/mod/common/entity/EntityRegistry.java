package com.craftingdead.mod.common.entity;

import com.craftingdead.mod.common.core.CraftingDead;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public class EntityRegistry {

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		int id = -1;
		EntityEntry zombie = EntityEntryBuilder.create().entity(EntityCDZombie.class)
				.id(new ResourceLocation(CraftingDead.MOD_ID, "cdzombie"), id++).name("cdzombie")
				.egg(0x000000, 0xFFFFFF).tracker(64, 20, false).build();
		event.getRegistry().registerAll(zombie);
	}

}
