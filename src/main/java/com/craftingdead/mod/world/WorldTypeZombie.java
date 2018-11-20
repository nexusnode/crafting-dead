package com.craftingdead.mod.world;

import com.craftingdead.mod.world.gen.layer.GenLayerZombie;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;

public class WorldTypeZombie extends WorldType {

	public WorldTypeZombie() {
		super("zombie");
	}

	@Override
	public int getSpawnFuzz(WorldServer world, MinecraftServer server) {
		return 100;
	}

	@Override
	public GenLayer getBiomeLayer(long worldSeed, GenLayer parentLayer, ChunkGeneratorSettings chunkSettings) {
		return new GenLayerZombie(worldSeed, parentLayer);
	}

}