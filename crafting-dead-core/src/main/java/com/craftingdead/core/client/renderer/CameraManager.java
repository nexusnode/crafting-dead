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

  private static final float LOOK_ROTATION_DECELERATION_PER_SECOND = 100.0F;
  private static final float JOLT_DECELERATION_PER_TICK = 1.0F;

  private FloatSmoother lookPitchSmoother = new FloatSmoother(0.5F);
  private FloatSmoother lookYawSmoother = new FloatSmoother(0.5F);

  private float lastLookTime = Float.MIN_VALUE;

  private FloatSmoother joltPitchSmoother = new FloatSmoother(1.1F);

  private float joltPitch;
  private float lastJoltPitch;

  private FloatSmoother joltRollSmoother = new FloatSmoother(0.75F);

  private float joltRoll;
  private float lastJoltRoll;

  public void randomRecoil(float pitchOffset, boolean modifyLookPosition) {
    this.joltCamera(pitchOffset, pitchOffset * (random.nextBoolean() ? 1.0F : -1.0F) * 0.5F,
        modifyLookPosition);
  }

  public void joltCamera(float pitchOffset, float yawOffset, boolean moveLookPosition) {
    if (moveLookPosition) {
      this.lookPitchSmoother.add(-pitchOffset);
      this.lookYawSmoother.add(yawOffset);
    }

    this.joltPitchSmoother.add(-pitchOffset * 0.175F);
    this.joltRollSmoother.add(yawOffset * 0.75F);
  }

  public void tick() {
    this.lastJoltPitch = this.joltPitch;
    this.joltPitch = this.joltPitchSmoother.getAndDecelerate(JOLT_DECELERATION_PER_TICK);

    this.lastJoltRoll = this.joltRoll;
    this.joltRoll = this.joltRollSmoother.getAndDecelerate(0.5F);
  }

  public void getLookRotationDelta(MutableVector2f result) {
    float currentTime = (float) NativeUtil.getTime();
    float timeDelta = Math.min(currentTime - this.lastLookTime, 0.1F);
    this.lastLookTime = currentTime;
    final float deceleration = timeDelta * LOOK_ROTATION_DECELERATION_PER_SECOND;
    result.set(this.lookPitchSmoother.getAndDecelerate(deceleration),
        this.lookYawSmoother.getAndDecelerate(deceleration));
  }

  public void getCameraRotations(float partialTicks, Vector3f result) {
    result.set(this.lerpPitch(partialTicks), 0.0F, this.lerpRoll(partialTicks));
  }

  public float getFov(float partialTicks) {
    return -this.lerpPitch(partialTicks) * 0.025F;
  }

  private float lerpPitch(float partialTicks) {
    return MathHelper.lerp(partialTicks, this.lastJoltPitch, this.joltPitch);
  }

  private float lerpRoll(float partialTicks) {
    return MathHelper.lerp(partialTicks, this.lastJoltRoll, this.joltRoll);
  }
}
