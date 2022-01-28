/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

  public static final RegistryObject<ParticleType<RGBFlashParticleData>> RGB_FLASH =
      PARTICLE_TYPES.register("rgb_flash",
          () -> create(true, RGBFlashParticleData.DESERIALIZER, RGBFlashParticleData.CODEC));

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
