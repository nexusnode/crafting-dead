package com.craftingdead.mod.common.world.biome;

import net.minecraft.init.Blocks;

public class BiomeZombieDesert extends BiomeZombie {

	public BiomeZombieDesert(BiomeProperties properties) {
		super(properties);
		this.topBlock = Blocks.SAND.getDefaultState();
		this.fillerBlock = Blocks.SAND.getDefaultState();
		this.decorator.treesPerChunk = -999;
		this.decorator.deadBushPerChunk = 0;
		this.decorator.reedsPerChunk = 0;
		this.decorator.cactiPerChunk = 0;
	}

}
