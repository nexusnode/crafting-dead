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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMobEffects {

  public static final DeferredRegister<MobEffect> MOB_EFFECTS =
      DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CraftingDead.ID);

  public static final RegistryObject<MobEffect> SCUBA =
      MOB_EFFECTS.register("scuba", ScubaMobEffect::new);

  public static final RegistryObject<MobEffect> FLASH_BLINDNESS =
      MOB_EFFECTS.register("flash_blindness", FlashBlindnessMobEffect::new);

  public static final RegistryObject<MobEffect> ADRENALINE =
      MOB_EFFECTS.register("adrenaline", AdrenalineMobEffect::new);

  public static final RegistryObject<MobEffect> PARACHUTE =
      MOB_EFFECTS.register("parachute", ParachuteMobEffect::new);

  /**
   * If the potion effect is not present, the potion effect is applied. Otherwise, overrides the
   * potion effect if its duration is longer than the current instance.
   *
   * @return <code>true</code> if the effect was applied. <code>false</code> otherwise.
   */
  public static boolean applyOrOverrideIfLonger(LivingEntity target, MobEffectInstance effect) {
    MobEffectInstance currentEffect = target.getEffect(effect.getEffect());
    if (currentEffect == null || currentEffect.getDuration() < effect.getDuration()) {
      target.removeEffect(effect.getEffect());
      return target.addEffect(effect);
    }
    return false;
  }
}
