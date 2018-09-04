package com.craftingdead.mod.common.world.biome;

public class BiomeZombieWasteland extends BiomeZombie {

	public BiomeZombieWasteland(BiomeProperties properties) {
		super(properties);
		this.decorator.deadBushPerChunk = 3;
		this.decorator.generateFalls = false;
		this.decorator.treesPerChunk = -999;
	}

}
