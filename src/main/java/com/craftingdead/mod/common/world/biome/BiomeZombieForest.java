package com.craftingdead.mod.common.world.biome;

import net.minecraft.init.Blocks;

public class BiomeZombieForest extends BiomeZombie {

	public BiomeZombieForest(BiomeProperties properties) {
		super(properties);
		this.decorator.treesPerChunk = 4;
		this.decorator.grassPerChunk = 5;
		this.decorator.flowersPerChunk = 0;
		this.decorator.generateFalls = false;
		this.topBlock = Blocks.GRASS.getDefaultState();
		this.fillerBlock = Blocks.DIRT.getDefaultState();
	}

}
