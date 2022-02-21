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

package com.craftingdead.core.client.renderer;

import net.minecraft.util.Mth;

public class FloatSmoother {

  private final float acceleration;

  private volatile float targetValue;
  private volatile float remainingValue;
  private volatile float lastAmount;

  public FloatSmoother(float acceleration) {
    this.acceleration = acceleration;
  }

  /**
   * Accumulate value.
   * 
   * @param value - amount to add
   */
  public void add(float value) {
    this.targetValue += value;
  }

  public void reset() {
    this.targetValue = 0.0F;
    this.remainingValue = 0.0F;
    this.lastAmount = 0.0F;
  }

  public float getAndDecelerate(float deceleration) {
    float deltaAmount = this.targetValue - this.remainingValue;
    float lerpAmount = Mth.lerp(this.acceleration, this.lastAmount, deltaAmount);
    float signum = Math.signum(deltaAmount);
    if (signum * deltaAmount > signum * this.lastAmount) {
      deltaAmount = lerpAmount;
    }

    this.lastAmount = lerpAmount;

    float result = deltaAmount * Math.min(deceleration, 1.0F);
    this.remainingValue += result;

    if (Math.abs(result) < 0.001F) {
      result = 0.0F;
      this.reset();
    }

    return result;
  }
}
