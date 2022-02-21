/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.particle;

import com.craftingdead.core.CraftingDead;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticleTypes {

  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
      DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CraftingDead.ID);

  public static final RegistryObject<ParticleType<GrenadeSmokeParticleData>> GRENADE_SMOKE =
      PARTICLE_TYPES.register("grenade_smoke",
          () -> create(true, GrenadeSmokeParticleData.DESERIALIZER,
              GrenadeSmokeParticleData.CODEC));

  public static final RegistryObject<ParticleType<FlashParticleOptions>> RGB_FLASH =
      PARTICLE_TYPES.register("rgb_flash",
          () -> create(true, FlashParticleOptions.DESERIALIZER, FlashParticleOptions.CODEC));

  private static <T extends ParticleOptions> ParticleType<T> create(boolean alwaysShow,
      @SuppressWarnings("deprecation") ParticleOptions.Deserializer<T> deserializer,
      Codec<T> codec) {
    return new ParticleType<T>(alwaysShow, deserializer) {
      @Override
      public Codec<T> codec() {
        return codec;
      }
    };
  }
}
