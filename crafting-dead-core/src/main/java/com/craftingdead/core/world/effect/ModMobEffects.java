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

package com.craftingdead.core.world.effect;

import com.craftingdead.core.CraftingDead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModMobEffects {

  public static final DeferredRegister<Effect> MOB_EFFECTS =
      DeferredRegister.create(ForgeRegistries.POTIONS, CraftingDead.ID);

  public static final RegistryObject<Effect> SCUBA =
      MOB_EFFECTS.register("scuba", ScubaMobEffect::new);

  public static final RegistryObject<Effect> FLASH_BLINDNESS =
      MOB_EFFECTS.register("flash_blindness", FlashBlindnessMobEffect::new);

  public static final RegistryObject<Effect> ADRENALINE =
      MOB_EFFECTS.register("adrenaline", AdrenalineMobEffect::new);

  public static final RegistryObject<Effect> PARACHUTE =
      MOB_EFFECTS.register("parachute", ParachuteMobEffect::new);

  /**
   * If the potion effect is not present, the potion effect is applied. Otherwise, overrides the
   * potion effect if its duration is longer than the current instance.
   *
   * @return <code>true</code> if the effect was applied. <code>false</code> otherwise.
   */
  public static boolean applyOrOverrideIfLonger(LivingEntity target, EffectInstance effect) {
    EffectInstance currentEffect = target.getEffect(effect.getEffect());
    if (currentEffect == null || currentEffect.getDuration() < effect.getDuration()) {
      target.removeEffect(effect.getEffect());
      return target.addEffect(effect);
    }
    return false;
  }
}
