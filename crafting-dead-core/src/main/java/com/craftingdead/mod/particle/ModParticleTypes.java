package com.craftingdead.mod.particle;

import com.craftingdead.mod.CraftingDead;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticleTypes {

  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
      new DeferredRegister<>(ForgeRegistries.PARTICLE_TYPES, CraftingDead.ID);

  public static final RegistryObject<ParticleType<RGBFlashParticleData>> RGB_FLASH =
      PARTICLE_TYPES.register("rgb_flash",
          () -> new ParticleType<>(true, RGBFlashParticleData.DESERIALIZER));

  public static final RegistryObject<ParticleType<GrenadeSmokeParticleData>> GRENADE_SMOKE =
      PARTICLE_TYPES.register("grenade_smoke",
          () -> new ParticleType<>(true, GrenadeSmokeParticleData.DESERIALIZER));
}
