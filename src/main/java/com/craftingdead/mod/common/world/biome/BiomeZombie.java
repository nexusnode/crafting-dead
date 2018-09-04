package com.craftingdead.mod.common.world.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class BiomeZombie extends Biome {

	public BiomeZombie(BiomeProperties properties) {
		super(properties);
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
	}

	@Override
	public float getSpawningChance() {
		return 0.15F;
	}

	@Override
	public int getSkyColorByTemp(float currentTemperature) {
		return 0x1F2D52;
	}

	@Override
	public int getGrassColorAtPos(BlockPos pos) {
		return 0x324137;
	}

	@Override
	public int getFoliageColorAtPos(BlockPos pos) {
		return 0x413C32;
	}

}
