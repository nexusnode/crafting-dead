package com.craftingdead.mod.common.world.biome;

public class BiomeZombiePlains extends BiomeZombie {

	public BiomeZombiePlains(BiomeProperties properties) {
		super(properties);
		this.decorator.treesPerChunk = -999;
		this.decorator.flowersPerChunk = 0;
		this.decorator.grassPerChunk = 5;
		this.decorator.generateFalls = false;
	}

}
