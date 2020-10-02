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
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

public class CameraManager {

  private static final Random random = new Random();

  private Vec2f lookRotationVelocity;

  private long joltDurationMs;

  private long joltStartTime;

  private float pitchMultiplier;

  private float rollMultiplier;

  private float fovMultiplier;

  public void joltCamera(float amountPercent, boolean modifyLookPosition) {
    if (amountPercent == 0.0F) {
      return;
    }
    float randomAmount = amountPercent * (random.nextFloat() + 1.0F) / 2.0F;
    float randomNegativeAmount = randomAmount * (random.nextBoolean() ? 1.0F : -1.0F);
    this.lookRotationVelocity =
        modifyLookPosition ? new Vec2f(-randomAmount * 3F, randomNegativeAmount * 3.25F) : null;
    this.joltStartTime = Util.milliTime();
    this.rollMultiplier = randomNegativeAmount * 0.75F;
    this.pitchMultiplier = randomAmount / 2.0F;
    this.fovMultiplier = 0.035F;
    this.joltDurationMs = (long) (450L * (randomAmount * 2.15F));
  }

  public Vec2f getLookRotationVelocity() {
    if (this.lookRotationVelocity == null) {
      return new Vec2f(0.0F, 0.0F);
    }
    float pct = 1.0F - MathHelper
        .clamp((float) (Util.milliTime() - this.joltStartTime) / (this.joltDurationMs / 2.0F), 0.0F,
            1.0F);
    Vec2f newRotationVelocity =
        new Vec2f(this.lookRotationVelocity.x * pct, this.lookRotationVelocity.y * pct);
    return newRotationVelocity;
  }

  public Vector3f getCameraRotation() {
    float pct = MathHelper
        .clamp((float) (Util.milliTime() - this.joltStartTime) / (this.joltDurationMs * 1.15F),
            0.0F, 1.0F);
    if (pct == 1.0F) {
      return new Vector3f(0.0F, 0.0F, 0.0F);
    }
    float roll = (float) Math.sin(Math.toRadians(180 * pct)) * this.rollMultiplier;
    float pitch = (float) Math.sin(Math.toRadians(360 * pct)) * this.pitchMultiplier;
    return new Vector3f(pitch, 0, roll);
  }

  public float getFov() {
    float pct = MathHelper
        .clamp((float) (Util.milliTime() - this.joltStartTime) / (this.joltDurationMs / 2.0F),
            0.0F, 1.0F);
    if (pct == 1.0F) {
      return 0.0F;
    }
    return (float) Math.sin(Math.toRadians(180 * pct)) * this.fovMultiplier;
  }
}
