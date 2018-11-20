package com.craftingdead.mod.init;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.entity.monster.EntityCDZombie;
import com.craftingdead.mod.entity.monster.EntityCDZombieFast;
import com.craftingdead.mod.entity.monster.EntityCDZombieTank;
import com.craftingdead.mod.entity.monster.EntityCDZombieWeak;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod.EventBusSubscriber
public class ModEntities {

	private static int id = -1;

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		EntityEntry zombie = buildZombie("zombie", id++, EntityCDZombie.class, 40, 3, 8);
		EntityEntry zombieFast = buildZombie("zombie_fast", id++, EntityCDZombieFast.class, 15, 2, 4);
		EntityEntry zombieTank = buildZombie("zombie_tank", id++, EntityCDZombieTank.class, 5, 2, 4);
		EntityEntry zombieWeak = buildZombie("zombie_weak", id++, EntityCDZombieWeak.class, 30, 4, 12);
		event.getRegistry().registerAll(zombie, zombieFast, zombieTank, zombieWeak);
	}

	private static EntityEntry buildZombie(String registryName, int id, Class<? extends EntityCDZombie> zombieType,
			int spawnWeight, int spawnMin, int spawnMax) {
		return startBuilding(registryName, id).entity(zombieType).tracker(64, 3, false).egg(0x000000, 0xFFFFFF)
				.spawn(EnumCreatureType.MONSTER, spawnWeight, spawnMin, spawnMax, ModBiomes.getZombieBiomes())
				.build();
	}

	private static EntityEntryBuilder<Entity> startBuilding(String registryName, int id) {
		return EntityEntryBuilder.create().id(new ResourceLocation(CraftingDead.MOD_ID, registryName), id)
				.name(String.format("%s%s%s", CraftingDead.MOD_ID, ".", registryName));
	}

}
