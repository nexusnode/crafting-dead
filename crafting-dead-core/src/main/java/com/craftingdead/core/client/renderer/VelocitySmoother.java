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
