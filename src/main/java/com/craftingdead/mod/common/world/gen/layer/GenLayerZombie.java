package com.craftingdead.mod.common.world.gen.layer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.craftingdead.mod.common.world.WorldRegistry;
import com.google.common.collect.ImmutableList;

import net.minecraft.init.Biomes;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

public class GenLayerZombie extends GenLayer {

	private EnumMap<BiomeType, List<BiomeEntry>> biomeMap = new EnumMap<BiomeType, List<BiomeEntry>>(BiomeType.class);

	public GenLayerZombie(long worldSeed, GenLayer parentLayer) {
		super(worldSeed);
		this.parent = parentLayer;

		for (BiomeType type : BiomeType.values()) {
			biomeMap.put(type, new ArrayList<BiomeEntry>());
		}

		biomeMap.get(BiomeType.DESERT).add(new BiomeEntry(WorldRegistry.ZOMBIE_DESERT, 10));
		biomeMap.get(BiomeType.WARM).add(new BiomeEntry(WorldRegistry.ZOMBIE_PLAINS, 10));
		biomeMap.get(BiomeType.COOL).addAll(ImmutableList.of(new BiomeEntry(WorldRegistry.ZOMBIE_FOREST, 10),
				new BiomeEntry(WorldRegistry.ZOMBIE_WASTELAND, 10)));
		biomeMap.get(BiomeType.ICY).add(new BiomeEntry(WorldRegistry.ZOMBIE_SNOW, 10));

	}

	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
		int[] parentBiomes = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
		int[] biomes = IntCache.getIntCache(areaWidth * areaHeight);

		for (int y = 0; y < areaHeight; ++y) {
			for (int x = 0; x < areaWidth; ++x) {
				this.initChunkSeed((long) (x + areaX), (long) (y + areaY));
				int parentBiome = parentBiomes[x + y * areaWidth];

				if (isBiomeOceanic(parentBiome)) {
					biomes[x + y * areaWidth] = parentBiome;
				} else if (parentBiome == Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND)) {
					biomes[x + y * areaWidth] = parentBiome;
				} else if (parentBiome == 1) {
					biomes[x + y * areaWidth] = Biome.getIdForBiome(getWeightedBiomeEntry(BiomeType.DESERT).biome);
				} else if (parentBiome == 2) {
					biomes[x + y * areaWidth] = Biome.getIdForBiome(getWeightedBiomeEntry(BiomeType.WARM).biome);
				} else if (parentBiome == 3) {
					biomes[x + y * areaWidth] = Biome.getIdForBiome(getWeightedBiomeEntry(BiomeType.COOL).biome);
				} else if (parentBiome == 4) {
					biomes[x + y * areaWidth] = Biome.getIdForBiome(getWeightedBiomeEntry(BiomeType.ICY).biome);
				} else {
					biomes[x + y * areaWidth] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND);
				}
			}
		}

		return biomes;
	}

	protected BiomeEntry getWeightedBiomeEntry(BiomeType type) {
		List<BiomeEntry> biomeList = biomeMap.get(type);
		int totalWeight = WeightedRandom.getTotalWeight(biomeList);
		int weight = BiomeManager.isTypeListModded(type) ? nextInt(totalWeight) : nextInt(totalWeight / 10) * 10;
		return WeightedRandom.getRandomItem(biomeList, weight);
	}
}