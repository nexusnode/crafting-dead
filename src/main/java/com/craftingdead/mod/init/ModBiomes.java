package com.craftingdead.mod.init;

import java.util.List;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.world.WorldTypeZombie;
import com.craftingdead.mod.world.biome.BiomeZombieDesert;
import com.craftingdead.mod.world.biome.BiomeZombieForest;
import com.craftingdead.mod.world.biome.BiomeZombiePlains;
import com.craftingdead.mod.world.biome.BiomeZombieSnow;
import com.craftingdead.mod.world.biome.BiomeZombieWasteland;
import com.google.common.collect.ImmutableList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ModBiomes {

	public static final Biome ZOMBIE_DESERT = new BiomeZombieDesert(new BiomeProperties("Zombie Desert"))
			.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, "zombie_desert"));
	public static final Biome ZOMBIE_FOREST = new BiomeZombieForest(
			new BiomeProperties("Zombie Forest").setWaterColor(0x00c99e).setBaseHeight(0.11F).setHeightVariation(0.12F))
					.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, "zombie_forest"));
	public static final Biome ZOMBIE_PLAINS = new BiomeZombiePlains(
			new BiomeProperties("Zombie Plains").setWaterColor(0x00c99e))
					.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, "zombie_plains"));
	public static final Biome ZOMBIE_SNOW = new BiomeZombieSnow(
			new BiomeProperties("Zombie Snow").setTemperature(0.0F).setRainfall(0.5F).setSnowEnabled())
					.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, "zombie_snow"));
	public static final Biome ZOMBIE_WASTELAND = new BiomeZombieWasteland(new BiomeProperties("Zombie Wasteland"))
			.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, "zombie_wasteland"));

	public static final WorldType ZOMBIE_WORLD_TYPE = new WorldTypeZombie();

	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {
		event.getRegistry().registerAll(getZombieBiomes().toArray(new Biome[0]));
	}

	public static List<Biome> getZombieBiomes() {
		return ImmutableList.of(ZOMBIE_DESERT, ZOMBIE_FOREST, ZOMBIE_PLAINS, ZOMBIE_SNOW, ZOMBIE_WASTELAND);
	}

}
