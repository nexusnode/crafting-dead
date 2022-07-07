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

public class ShootAnimation extends TimedAnimation {

  private final float bounceAmplification;
  private final float kickbackAmplifcation;

  public ShootAnimation(int lifetimeTicks, float bounceAmplification, float kickbackAmplifcation) {
    super(lifetimeTicks);
    this.bounceAmplification = bounceAmplification;
    this.kickbackAmplifcation = kickbackAmplifcation;
  }

  @Override
  public void apply(float partialTick, PoseStack poseStack) {
    var percent = this.lerpProgress(partialTick);
    poseStack.translate(0, 0, this.getKickbackTranslation(percent));
    poseStack.mulPose(this.getBounceRotation(percent));
  }

  private Quaternion getBounceRotation(float percent) {
    var easedProgress =
        EasingFunction.SINE_IN.andThen(EasingFunction.ELASTIC_OUT).apply(percent);
    return Vector3f.XP.rotationDegrees(this.bounceAmplification * Mth.sin(easedProgress * Mth.PI));
  }

  private float getKickbackTranslation(float percent) {
    var easedProgress = EasingFunction.EXPO_OUT.apply(percent);
    return Mth.sin(easedProgress * Mth.PI) * this.kickbackAmplifcation;
  }

  @Override
  public void applyArm(InteractionHand hand, HumanoidArm arm, float partialTick,
      PoseStack poseStack) {
    var lerpProgress = this.lerpProgress(partialTick);
    var translation = this.getKickbackTranslation(lerpProgress);
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
  public void applyCamera(float partialTick, Vector3f rotations) {}

  public static ShootAnimation rifle() {
    return new ShootAnimation(5, 2.0F, 0.25F);
  }

  public static ShootAnimation submachineGun() {
    return new ShootAnimation(5, 2.0F, 0.15F);
  }

  public static ShootAnimation pistol() {
    return new ShootAnimation(7, 3.5F, 0.5F);
  }
}
