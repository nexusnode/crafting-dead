package com.craftingdead.mod.init;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.entity.EntityCorpse;
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
		event.getRegistry().registerAll( //
				startBuilding("zombie") //
						.entity(EntityCDZombie.class) //
						.tracker(64, 3, false) //
						.egg(0x000000, 0xFFFFFF) //
						.spawn(EnumCreatureType.MONSTER, 40, 3, 8, ModBiomes.getZombieBiomes()) //
						.build(), //
				startBuilding("zombie_fast") //
						.entity(EntityCDZombieFast.class) //
						.tracker(64, 3, false) //
						.egg(0x000000, 0xFFFFFF) //
						.spawn(EnumCreatureType.MONSTER, 15, 2, 4, ModBiomes.getZombieBiomes()) //
						.build(),
				startBuilding("zombie_tank") //
						.entity(EntityCDZombieTank.class) //
						.tracker(64, 3, false) //
						.egg(0x000000, 0xFFFFFF) //
						.spawn(EnumCreatureType.MONSTER, 5, 2, 4, ModBiomes.getZombieBiomes()) //
						.build(),
				startBuilding("zombie_weak") //
						.entity(EntityCDZombieWeak.class) //
						.tracker(64, 3, false) //
						.egg(0x000000, 0xFFFFFF) //
						.spawn(EnumCreatureType.MONSTER, 30, 4, 12, ModBiomes.getZombieBiomes()) //
						.build(),
				startBuilding("corpse") //
						.entity(EntityCorpse.class) //
						.tracker(64, 10, false) //
						.egg(0x000000, 0xFFFFFF) //
						.build() //
		);
	}

	private static EntityEntryBuilder<Entity> startBuilding(String registryName) {
		return EntityEntryBuilder.create().id(new ResourceLocation(CraftingDead.MOD_ID, registryName), id++)
				.name(String.format("%s%s%s", CraftingDead.MOD_ID, ".", registryName));
	}

}
