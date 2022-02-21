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

package com.craftingdead.core.world.effect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.phys.Vec3;

public class ScubaMobEffect extends MobEffect {

  protected ScubaMobEffect() {
    super(MobEffectCategory.BENEFICIAL, 0x1F1FA1);
  }

  @Override
  public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.isInWater()) {
      livingEntity.setAirSupply(livingEntity.getMaxAirSupply());
      livingEntity.moveRelative(0.1F, new Vec3(0.0D, 0.0D, 0.2D));
    }
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    return true;
  }
}
