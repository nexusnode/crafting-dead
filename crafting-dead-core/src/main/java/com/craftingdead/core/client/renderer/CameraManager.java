/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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

import java.util.Random;
import net.minecraft.client.util.NativeUtil;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;

public class CameraManager {

  private static final Random random = new Random();

  private static final float LOOK_ROTATION_DECELERATION = 10.0F;

  private long joltDurationMs;

  private long joltStartTimeMs;

  private float pitchMultiplier;

  private float rollMultiplier;

  private float fovMultiplier;

  private VelocitySmoother pitchSmoother = new VelocitySmoother();
  private VelocitySmoother yawSmoother = new VelocitySmoother();

  private float lastLookTime = Float.MIN_VALUE;

  public void joltCamera(float amountPercent, boolean modifyLookPosition) {
    if (amountPercent == 0.0F) {
      return;
    }
    float randomAmount = amountPercent * (random.nextFloat() + 1.0F) / 2.0F;
    float randomNegativeAmount = randomAmount * (random.nextBoolean() ? 1.0F : -1.0F);
    if (modifyLookPosition) {
      this.pitchSmoother.add(-randomAmount * 25.0F);
      this.yawSmoother.add(randomNegativeAmount * 12.5F);
    }
    this.joltStartTimeMs = Util.milliTime();
    this.rollMultiplier = randomNegativeAmount * 0.75F;
    this.pitchMultiplier = randomAmount / 2.0F;
    this.fovMultiplier = 0.1F * amountPercent;
    this.joltDurationMs = (long) (150L);
  }

  public Vector2f getLookRotationDelta() {
    float currentTime = (float) NativeUtil.getTime();
    float timeDelta = currentTime - this.lastLookTime;
    this.lastLookTime = currentTime;
    final float deceleration = timeDelta * LOOK_ROTATION_DECELERATION;
    return new Vector2f(this.pitchSmoother.getAndDecelerate(deceleration),
        this.yawSmoother.getAndDecelerate(deceleration));
  }

  public Vector3f getCameraRotations() {
    float pct = MathHelper.clamp(
        (float) (Util.milliTime() - this.joltStartTimeMs) / (this.joltDurationMs * 1.15F),
        0.0F, 1.0F);
    if (pct == 1.0F) {
      return new Vector3f(0.0F, 0.0F, 0.0F);
    }
    float roll = (float) Math.sin(Math.toRadians(180 * pct)) * this.rollMultiplier;
    float pitch = (float) Math.sin(Math.toRadians(360 * pct)) * this.pitchMultiplier;
    return new Vector3f(pitch, 0, roll);
  }

  public float getFov() {
    float pct = MathHelper.clamp(
        (float) (Util.milliTime() - this.joltStartTimeMs) / (this.joltDurationMs / 2.0F), 0.0F,
        1.0F);
    if (pct == 1.0F) {
      return 0.0F;
    }
    return (float) Math.sin(Math.toRadians(180 * pct)) * this.fovMultiplier;
  }
}
