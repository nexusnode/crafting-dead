package com.craftingdead.core.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.Vec3d;

public class ScubaEffect extends Effect {

  protected ScubaEffect() {
    super(EffectType.BENEFICIAL, 0x1F1FA1);
  }

  @Override
  public void performEffect(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.isInWater()) {
      livingEntity.setAir(livingEntity.getMaxAir());
      livingEntity.moveRelative(0.1F, new Vec3d(0.0D, 0.0D, 0.2D));
    }
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return true;
  }
}
