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
