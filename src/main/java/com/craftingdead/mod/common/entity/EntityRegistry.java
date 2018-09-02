package com.craftingdead.mod.common.entity;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.entity.monster.EntityCDZombie;

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
				.id(new ResourceLocation(CraftingDead.MOD_ID, "zombie"), id++).name("zombie").egg(0x000000, 0xFFFFFF)
				.tracker(64, 3, false).build();
		EntityEntry zombieFast = EntityEntryBuilder.create().entity(EntityCDZombie.class)
				.id(new ResourceLocation(CraftingDead.MOD_ID, "zombieFast"), id++).name("zombieFast")
				.egg(0x000000, 0xFFFFFF).tracker(64, 3, false).build();
		event.getRegistry().registerAll(zombie, zombieFast);
	}

}
