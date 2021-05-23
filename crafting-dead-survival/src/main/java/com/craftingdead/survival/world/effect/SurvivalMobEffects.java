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

package com.craftingdead.survival.world.effect;

import com.craftingdead.survival.CraftingDeadSurvival;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SurvivalMobEffects {

  public static final DeferredRegister<Effect> MOB_EFFECTS =
      DeferredRegister.create(ForgeRegistries.POTIONS, CraftingDeadSurvival.ID);

  public static final RegistryObject<Effect> INFECTION =
      MOB_EFFECTS.register("infection", InfectionMobEffect::new);

  public static final RegistryObject<Effect> BLEEDING =
      MOB_EFFECTS.register("bleeding", BleedingMobEffect::new);

  public static final RegistryObject<Effect> BROKEN_LEG =
      MOB_EFFECTS.register("broken_leg", BrokenLegMobEffect::new);
}
