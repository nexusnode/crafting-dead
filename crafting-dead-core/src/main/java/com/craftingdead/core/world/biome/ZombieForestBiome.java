package com.craftingdead.core.world.biome;

import com.craftingdead.core.entity.ModEntityTypes;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class ZombieForestBiome extends Biome {
  public ZombieForestBiome() {
    super((new Biome.Builder())
        .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
        .precipitation(Biome.RainType.RAIN)
        .category(Biome.Category.FOREST)
        .depth(0.1F)
        .scale(0.05F)
        .temperature(0.7F)
        .downfall(0.4F)
        .waterColor(4020182)
        .waterFogColor(329011)
        .parent(null));
    DefaultBiomeFeatures.addCarvers(this);
    DefaultBiomeFeatures.addStructures(this);
    DefaultBiomeFeatures.addLakes(this);
    DefaultBiomeFeatures.addStoneVariants(this);
    DefaultBiomeFeatures.addOres(this);
    DefaultBiomeFeatures.addSedimentDisks(this);
    //DefaultBiomeFeatures.addTaigaConifers(this);
    DefaultBiomeFeatures.addGrass(this);
    DefaultBiomeFeatures.addMushrooms(this);
    DefaultBiomeFeatures.addReedsAndPumpkins(this);
    DefaultBiomeFeatures.addSprings(this);
    DefaultBiomeFeatures.addFreezeTopLayer(this);

    this.addSpawn(EntityClassification.MONSTER,
        new Biome.SpawnListEntry(ModEntityTypes.advancedZombie, 400, 2, 8));
    this.addSpawn(EntityClassification.MONSTER,
        new Biome.SpawnListEntry(ModEntityTypes.fastZombie, 150, 2, 4));
    this.addSpawn(EntityClassification.MONSTER,
        new Biome.SpawnListEntry(ModEntityTypes.tankZombie, 50, 2, 4));
    this.addSpawn(EntityClassification.MONSTER,
        new Biome.SpawnListEntry(ModEntityTypes.weakZombie, 300, 2, 12));
 }
}