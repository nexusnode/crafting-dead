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

import java.util.Random;
import com.craftingdead.core.util.MutableVector2f;
import net.minecraft.client.util.NativeUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class CameraManager {

  private static final Random random = new Random();

  private static final float LOOK_ROTATION_DECELERATION = 10.0F;

  private VelocitySmoother lookPitchSmoother = new VelocitySmoother(0.5F);
  private VelocitySmoother lookYawSmoother = new VelocitySmoother(0.5F);

  private float lastLookTime = Float.MIN_VALUE;

  private VelocitySmoother pitchSmoother = new VelocitySmoother(1F);

  private float pitch;
  private float lastPitch;

  private VelocitySmoother rollSmoother = new VelocitySmoother(1F);

  private float roll;
  private float lastRoll;

  public void joltCamera(float amountPercent, boolean modifyLookPosition) {
    if (amountPercent == 0.0F) {
      return;
    }
    float randomAmount = amountPercent * (random.nextFloat() + 1.0F) / 2.0F;
    float randomNegativeAmount = randomAmount * (random.nextBoolean() ? 1.0F : -1.0F);
    if (modifyLookPosition) {
      this.lookPitchSmoother.add(-randomAmount * 25.0F);
      this.lookYawSmoother.add(randomNegativeAmount * 12.5F);
    }

    this.pitchSmoother.add(-randomAmount * 12.0F);
    this.rollSmoother.add(randomNegativeAmount * 7.5F);
  }

  public void tick() {
    this.lastPitch = this.pitch;
    this.pitch = this.pitchSmoother.getAndDecelerate(0.5F);

    this.lastRoll = this.roll;
    this.roll = this.rollSmoother.getAndDecelerate(0.5F);
  }

  public void getLookRotationDelta(MutableVector2f result) {
    float currentTime = (float) NativeUtil.getTime();
    float timeDelta = currentTime - this.lastLookTime;
    this.lastLookTime = currentTime;
    final float deceleration = timeDelta * LOOK_ROTATION_DECELERATION;
    result.set(this.lookPitchSmoother.getAndDecelerate(deceleration),
        this.lookYawSmoother.getAndDecelerate(deceleration));
  }

  public void getCameraRotations(float partialTicks, Vector3f result) {
    result.set(this.lerpPitch(partialTicks), 0, this.lerpRoll(partialTicks));
  }

  public float getFov(float partialTicks) {
    return -this.lerpPitch(partialTicks) * 0.015F;
  }

  private float lerpPitch(float partialTicks) {
    return MathHelper.lerp(partialTicks, this.lastPitch, this.pitch);
  }

  private float lerpRoll(float partialTicks) {
    return MathHelper.lerp(partialTicks, this.lastRoll, this.roll);
  }
}
