/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.survival.world.effect;

import com.craftingdead.survival.CraftingDeadSurvival;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SurvivalMobEffects {

  public static final DeferredRegister<MobEffect> MOB_EFFECTS =
      DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CraftingDeadSurvival.ID);

  public static final RegistryObject<MobEffect> INFECTION =
      MOB_EFFECTS.register("infection", InfectionMobEffect::new);

  public static final RegistryObject<MobEffect> BLEEDING =
      MOB_EFFECTS.register("bleeding", BleedingMobEffect::new);

  public static final RegistryObject<MobEffect> BROKEN_LEG =
      MOB_EFFECTS.register("broken_leg", BrokenLegMobEffect::new);
}
