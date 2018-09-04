package com.craftingdead.mod.common.entity;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.entity.monster.EntityCDZombie;
import com.craftingdead.mod.common.world.WorldRegistry;

import net.minecraft.entity.EnumCreatureType;
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
				.tracker(64, 3, false).spawn(EnumCreatureType.MONSTER, 40, 3, 8, WorldRegistry.ZOMBIE_BIOMES).build();
		EntityEntry zombieFast = EntityEntryBuilder.create().entity(EntityCDZombie.class)
				.id(new ResourceLocation(CraftingDead.MOD_ID, "zombie.fast"), id++).name("zombie.fast")
				.egg(0x000000, 0xFFFFFF).tracker(64, 3, false)
				.spawn(EnumCreatureType.MONSTER, 15, 2, 4, WorldRegistry.ZOMBIE_BIOMES).build();
		EntityEntry zombieTank = EntityEntryBuilder.create().entity(EntityCDZombie.class)
				.id(new ResourceLocation(CraftingDead.MOD_ID, "zombie.tank"), id++).name("zombie.tank")
				.egg(0x000000, 0xFFFFFF).tracker(64, 3, false)
				.spawn(EnumCreatureType.MONSTER, 5, 2, 4, WorldRegistry.ZOMBIE_BIOMES).build();
		EntityEntry zombieWeak = EntityEntryBuilder.create().entity(EntityCDZombie.class)
				.id(new ResourceLocation(CraftingDead.MOD_ID, "zombie.weak"), id++).name("zombie.weak")
				.egg(0x000000, 0xFFFFFF).tracker(64, 3, false)
				.spawn(EnumCreatureType.MONSTER, 30, 4, 12, WorldRegistry.ZOMBIE_BIOMES).build();
		event.getRegistry().registerAll(zombie, zombieFast, zombieTank, zombieWeak);
	}

}
