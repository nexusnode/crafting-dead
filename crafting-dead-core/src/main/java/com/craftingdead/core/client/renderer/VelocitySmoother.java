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

import net.minecraft.util.math.MathHelper;

public class VelocitySmoother {

  private float targetVelocity;
  private float remainingVelocity;
  private float lastVelocity;

  /**
   * Accumulate velocity.
   * 
   * @param velocity - amount to add
   */
  public void add(float velocity) {
    this.targetVelocity += velocity;
  }

  public void reset() {
    this.targetVelocity = 0.0F;
    this.remainingVelocity = 0.0F;
    this.lastVelocity = 0.0F;
  }

  public float getAndDecelerate(float deceleration) {
    float nextVelocity = this.targetVelocity - this.remainingVelocity;
    float currentVelocity = MathHelper.lerp(0.5F, this.lastVelocity, nextVelocity);
    float signum = Math.signum(nextVelocity);
    if (signum * nextVelocity > signum * this.lastVelocity) {
      nextVelocity = currentVelocity;
    }

    this.lastVelocity = currentVelocity;
    this.remainingVelocity += nextVelocity * deceleration;
    return nextVelocity * deceleration;
  }
}
