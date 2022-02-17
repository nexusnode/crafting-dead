/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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
