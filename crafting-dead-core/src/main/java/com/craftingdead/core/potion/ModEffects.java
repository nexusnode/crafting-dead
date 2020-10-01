/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core.potion;

import com.craftingdead.core.CraftingDead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffects {

  public static final DeferredRegister<Effect> EFFECTS =
      DeferredRegister.create(ForgeRegistries.POTIONS, CraftingDead.ID);

  public static final RegistryObject<Effect> SCUBA = EFFECTS.register("scuba", ScubaEffect::new);

  public static final RegistryObject<Effect> BLEEDING =
      EFFECTS.register("bleeding", BleedingEffect::new);

  public static final RegistryObject<Effect> BROKEN_LEG =
      EFFECTS.register("broken_leg", BrokenLegEffect::new);

  public static final RegistryObject<Effect> INFECTION =
      EFFECTS.register("infection", InfectionEffect::new);

  public static final RegistryObject<Effect> HYDRATE =
      EFFECTS.register("hydrate", HydrateEffect::new);

  public static final RegistryObject<Effect> FLASH_BLINDNESS =
      EFFECTS.register("flash_blindness", FlashBlindnessEffect::new);

  public static final RegistryObject<Effect> ADRENALINE =
      EFFECTS.register("adrenaline", AdrenalineEffect::new);

  /**
   * If the potion effect is not present, the potion effect is applied. Otherwise, overrides the
   * potion effect if its duration is longer than the current instance.
   *
   * @return <code>true</code> if the effect was applied. <code>false</code> otherwise.
   */
  public static boolean applyOrOverrideIfLonger(LivingEntity target, EffectInstance effect) {
    EffectInstance currentEffect = target.getActivePotionEffect(effect.getPotion());
    if (currentEffect == null || currentEffect.getDuration() < effect.getDuration()) {
      target.removePotionEffect(effect.getPotion());
      return target.addPotionEffect(effect);
    }
    return false;
  }
}
