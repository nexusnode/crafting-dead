package com.craftingdead.mod.world.biome;

import java.util.Random;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class BiomeZombieSnow extends BiomeZombie {

	public BiomeZombieSnow(BiomeProperties properties) {
		super(properties);
	}

	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
		return rand.nextInt(18) == 1 ? new WorldGenTaiga1() : new WorldGenTaiga2(false);
	}

}
