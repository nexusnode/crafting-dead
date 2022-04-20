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

package com.craftingdead.core.client.animation.gun;

import com.craftingdead.core.client.animation.TimedAnimation;
import com.craftingdead.core.util.EasingFunction;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

public class RifleShootAnimation extends TimedAnimation {

  public RifleShootAnimation() {
    super(5);
  }

  @Override
  public void apply(float partialTicks, PoseStack poseStack) {
    float lerpProgress = this.lerpProgress(partialTicks);
    poseStack.translate(0, 0, getPushbackTranslation(lerpProgress));
    poseStack.mulPose(getBounceRotation(lerpProgress));
  }

  private static Quaternion getBounceRotation(float lerpProgress) {
    final float amplification = 3.0F;
    float easedProgress =
        EasingFunction.SINE_IN.andThen(EasingFunction.ELASTIC_OUT).apply(lerpProgress);
    return Vector3f.XP
        .rotationDegrees(amplification * Mth.sin(easedProgress * ((float) Math.PI)));
  }

  private static float getPushbackTranslation(float lerpProgress) {
    float easedProgress = EasingFunction.EXPO_OUT.apply(lerpProgress);
    return Mth.sin(easedProgress * ((float) Math.PI)) * 0.5F;
  }

  @Override
  public void applyArm(InteractionHand hand, HumanoidArm arm, float partialTicks, PoseStack poseStack) {
    float lerpProgress = this.lerpProgress(partialTicks);
    float translation = getPushbackTranslation(lerpProgress);
    switch (arm) {
      case LEFT:
        poseStack.translate(0, -translation * 0.5F, translation);
        break;
      case RIGHT:
        poseStack.translate(0, 0, translation);
        break;
    }
  }

  @Override
  public void applyCamera(float partialTicks, Vector3f rotations) {}
}
