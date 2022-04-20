/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.renderer;

import java.util.Random;
import com.craftingdead.core.util.MutableVector2f;
import com.mojang.blaze3d.Blaze3D;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;

public class CameraManager {

  private static final Random random = new Random();

  private FloatSmoother lookPitchSmoother = new FloatSmoother(1.0F);
  private FloatSmoother lookYawSmoother = new FloatSmoother(1.0F);

  private float lastLookTime = Float.MIN_VALUE;

  private float largeAmpSlowFade;
  private float smallAmpQuickFade;

  public void randomRecoil(float pitchOffset, boolean modifyLookPosition) {
    this.joltCamera(pitchOffset, pitchOffset * (random.nextBoolean() ? 1.0F : -1.0F) / 2.0F,
        modifyLookPosition);
  }

  public void joltCamera(float pitchOffset, float yawOffset, boolean moveLookPosition) {
    if (moveLookPosition) {
      this.lookPitchSmoother.add(-pitchOffset);
      this.lookYawSmoother.add(yawOffset);
    }

    this.largeAmpSlowFade += pitchOffset;
    this.smallAmpQuickFade += pitchOffset / 2.0F;
  }

  public void tick() {
    if (this.largeAmpSlowFade > 0) {
      this.largeAmpSlowFade *= 0.7F;
    }

    if (this.smallAmpQuickFade > 0) {
      this.smallAmpQuickFade *= 0.25;
    }
  }

  public void getLookRotationDelta(MutableVector2f result) {
    float currentTime = (float) Blaze3D.getTime();
    float timeDelta = currentTime - this.lastLookTime;
    result.set(this.lookPitchSmoother.getAndDecelerate(timeDelta* 2.0F),
        this.lookYawSmoother.getAndDecelerate(timeDelta * 2.0F));
  }

  public void getCameraRotations(float partialTicks, Vector3f result) {
    final float time = (float) Util.getMillis() / 20.0F;
    float bounce = Mth.sin(time * 0.35F);
    float roll = bounce / 2.0F * this.largeAmpSlowFade;
    result.set(-this.smallAmpQuickFade, 0, roll);
  }

  public float getFov(float partialTicks) {
    final float time = (float) Util.getMillis() / 20.0F;
    float bounce = Mth.sin(time * 0.5F);
    return bounce / 125.0F * -this.largeAmpSlowFade;
  }
}
