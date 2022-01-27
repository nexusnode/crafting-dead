package com.craftingdead.core.util;

import java.util.function.UnaryOperator;
import net.minecraft.util.math.MathHelper;

public enum EasingFunction implements UnaryOperator<Float> {

  SINE_IN_OUT(t -> -(MathHelper.cos((float) Math.PI * t) - 1.0F) / 2.0F),
  SINE_IN(t -> 1.0F - MathHelper.cos((float) ((t * Math.PI) / (2.0F)))),
  SINE_OUT(t -> MathHelper.sin((float) ((t * Math.PI) / (2.0F)))),
  ELASTIC_OUT(t -> {
    float c4 = (float) ((2.0F * Math.PI) / 3.0F);
    return t == 0.0F ? 0.0F
        : t == 1.0F ? 1.0F
            : (float) Math.pow(2.0F, -10.0F * t) * MathHelper.sin((t * 10 - 0.75F) * c4) + 1.0F;
  }),
  EXPO_OUT(t -> (t == 1.0F) ? 1.0F : -(float) Math.pow(2.0F, -10.0F * t) + 1.0F),
  BOUNCE_OUT(t -> {
    final float n1 = 7.5625F;
    final float d1 = 2.75F;
    if (t < 1.0F / d1) {
      return n1 * t * t;
    } else if (t < 2.0F / d1) {
      return n1 * (t -= 1.5F / d1) * t + 0.75F;
    } else if (t < 2.5F / d1) {
      return n1 * (t -= 2.25F / d1) * t + 0.9375F;
    } else {
      return n1 * (t -= 2.625F / d1) * t + 0.984375F;
    }
  }),
  BOUNCE_IN(t -> 1.0F - BOUNCE_OUT.apply(1.0F - t)),
  BOUNCE_IN_OUT(t -> t < 0.5F
      ? (1.0F - BOUNCE_OUT.apply(1.0F - 2.0F * t)) / 2.0F
      : (1.0F + BOUNCE_OUT.apply(2.0F * t - 1.0F)) / 2.0F);

  private final UnaryOperator<Float> function;

  private EasingFunction(UnaryOperator<Float> function) {
    this.function = function;
  }

  @Override
  public Float apply(Float t) {
    return this.function.apply(t);
  }
}
