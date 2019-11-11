package com.craftingdead.mod.client;

import java.util.Random;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

public class RecoilHelper {

  /**
   * Random.
   */
  private static final Random random = new Random();

  /**
   * Current camera velocity.
   */
  private Vec2f rotationVelocity = Vec2f.ZERO;

  /**
   * Camera velocity of last tick.
   */
  private Vec2f prevRotationVelocity = this.rotationVelocity;

  public Vec2f update() {
    float smoothYaw = MathHelper.lerp(0.5F, this.prevRotationVelocity.x, this.rotationVelocity.x);
    float smoothPitch = MathHelper.lerp(0.5F, this.prevRotationVelocity.y, this.rotationVelocity.y);
    this.rotationVelocity = Vec2f.ZERO;
    this.prevRotationVelocity = new Vec2f(smoothYaw, smoothPitch);
    return this.prevRotationVelocity;
  }

  public void jolt(float accuracy) {
    float amount = ((1.0F - accuracy) * 100) / 5.0F;
    this.rotationVelocity =
        new Vec2f(this.rotationVelocity.x + (random.nextBoolean() ? amount : -amount),
            this.rotationVelocity.y - amount);
  }
}
