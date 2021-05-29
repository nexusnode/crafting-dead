package com.craftingdead.survival.particles;

import com.craftingdead.survival.CraftingDeadSurvival;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SurvivalParticleTypes {

  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
      DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CraftingDeadSurvival.ID);

  public static final RegistryObject<BasicParticleType> MILITARY_LOOT_GEN =
      PARTICLE_TYPES.register("military_loot_gen", () -> new BasicParticleType(false));

  public static final RegistryObject<BasicParticleType> MEDIC_LOOT_GEN =
      PARTICLE_TYPES.register("medic_loot_gen", () -> new BasicParticleType(false));

  public static final RegistryObject<BasicParticleType> CIVILIAN_LOOT_GEN =
      PARTICLE_TYPES.register("civilian_loot_gen", () -> new BasicParticleType(false));

  public static final RegistryObject<BasicParticleType> CIVILIAN_RARE_LOOT_GEN =
      PARTICLE_TYPES.register("civilian_rare_loot_gen", () -> new BasicParticleType(false));

  public static final RegistryObject<BasicParticleType> POLICE_LOOT_GEN =
      PARTICLE_TYPES.register("police_loot_gen", () -> new BasicParticleType(false));
}
