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

package com.craftingdead.survival.particles;

import com.craftingdead.survival.CraftingDeadSurvival;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SurvivalParticleTypes {

  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
      DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CraftingDeadSurvival.ID);

  public static final RegistryObject<SimpleParticleType> MILITARY_LOOT_GEN =
      PARTICLE_TYPES.register("military_loot_gen", () -> new SimpleParticleType(false));

  public static final RegistryObject<SimpleParticleType> MEDIC_LOOT_GEN =
      PARTICLE_TYPES.register("medic_loot_gen", () -> new SimpleParticleType(false));

  public static final RegistryObject<SimpleParticleType> CIVILIAN_LOOT_GEN =
      PARTICLE_TYPES.register("civilian_loot_gen", () -> new SimpleParticleType(false));

  public static final RegistryObject<SimpleParticleType> CIVILIAN_RARE_LOOT_GEN =
      PARTICLE_TYPES.register("civilian_rare_loot_gen", () -> new SimpleParticleType(false));

  public static final RegistryObject<SimpleParticleType> POLICE_LOOT_GEN =
      PARTICLE_TYPES.register("police_loot_gen", () -> new SimpleParticleType(false));
}
