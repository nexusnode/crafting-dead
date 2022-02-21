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

package com.craftingdead.core.util;

import java.util.Objects;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.minecraft.util.Mth;

public enum EasingFunction implements FloatUnaryOperator {

  SINE_IN_OUT(t -> -(Mth.cos((float) Math.PI * t) - 1.0F) / 2.0F),
  SINE_IN(t -> 1.0F - Mth.cos((float) ((t * Math.PI) / (2.0F)))),
  SINE_OUT(t -> Mth.sin((float) ((t * Math.PI) / (2.0F)))),
  ELASTIC_OUT(t -> {
    float c4 = (float) ((2.0F * Math.PI) / 3.0F);
    return t == 0.0F ? 0.0F
        : t == 1.0F ? 1.0F
            : (float) Math.pow(2.0F, -10.0F * t) * Mth.sin((t * 10 - 0.75F) * c4) + 1.0F;
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

  private final FloatUnaryOperator function;

  private EasingFunction(FloatUnaryOperator function) {
    this.function = function;
  }

  @Override
  public float apply(float t) {
    return this.function.apply(t);
  }

  public FloatUnaryOperator andThen(FloatUnaryOperator after) {
    Objects.requireNonNull(after);
    return (float t) -> after.apply(this.apply(t));
  }
}
