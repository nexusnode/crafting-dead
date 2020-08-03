package com.craftingdead.core.client.renderer;

import java.util.Random;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

public class CameraManager {

  private static final Random random = new Random();

  /**
   * Current look rotation velocity.
   */
  private Vec2f lookRotationVelocity = Vec2f.ZERO;

  /**
   * Look rotation velocity of last tick.
   */
  private Vec2f prevLookRotationVelocity = this.lookRotationVelocity;

  private long rollDurationMs = 250L;

  private long joltStartTime = 0;

  private float pitchMultiplier;

  private float rollMultiplier;

  private float fovMultiplier;

  public void joltCamera(float amountPercent, boolean modifyLookPosition) {
    if (amountPercent == 0.0F) {
      return;
    }
    float randomAmount = amountPercent * (random.nextFloat() + 1.0F);
    float randomNegativeAmount = randomAmount * (random.nextBoolean() ? 1.0F : -1.0F);
    if (modifyLookPosition) {
      this.lookRotationVelocity = new Vec2f(-randomAmount * 3.75F, randomNegativeAmount * 3.25F);
    }
    this.joltStartTime = Util.milliTime();
    this.rollMultiplier = randomNegativeAmount / 2.0F;
    this.pitchMultiplier = randomAmount / 2.0F;
    this.fovMultiplier = 0.03F;
    this.rollDurationMs = (long) (500L * (amountPercent));
  }

  public Vec2f getLookRotationVelocity() {
    float pct = 1.0F - MathHelper
        .clamp((float) (Util.milliTime() - this.joltStartTime) / this.rollDurationMs, 0.0F, 1.0F);
    Vec2f newRotationVelocity = new Vec2f(
        MathHelper.lerp(0.15F, this.prevLookRotationVelocity.x, this.lookRotationVelocity.x * pct),
        MathHelper.lerp(0.15F, this.prevLookRotationVelocity.y, this.lookRotationVelocity.y * pct));
    this.prevLookRotationVelocity = newRotationVelocity;
    return newRotationVelocity;
  }

  public Vector3f getCameraRotation() {
    float pct = MathHelper
        .clamp((float) (Util.milliTime() - this.joltStartTime) / this.rollDurationMs, 0.0F, 1.0F);
    if (pct == 1.0F) {
      return new Vector3f(0.0F, 0.0F, 0.0F);
    }
    float roll = (float) Math.sin(Math.toRadians(180 * pct)) * this.rollMultiplier;
    float pitch = (float) Math.sin(Math.toRadians(360 * pct)) * this.pitchMultiplier;
    return new Vector3f(pitch, 0, roll);
  }

  public float getFov() {
    float pct = MathHelper
        .clamp((float) (Util.milliTime() - this.joltStartTime) / (this.rollDurationMs / 2.0F),
            0.0F, 1.0F);
    return (float) Math.sin(Math.toRadians(180 * pct)) * this.fovMultiplier;
  }
}
