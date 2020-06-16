package com.craftingdead.core.world.biome;

import com.craftingdead.core.CraftingDead;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomes {

  public static final DeferredRegister<Biome> BIOMES =
      new DeferredRegister<>(ForgeRegistries.BIOMES, CraftingDead.ID);

  public static final RegistryObject<Biome> ZOMBIE_FOREST = BIOMES
      .register("zombie_forest",
          () -> new ZombieForestBiome());

}
