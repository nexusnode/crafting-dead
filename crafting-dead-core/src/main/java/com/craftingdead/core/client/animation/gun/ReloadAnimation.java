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
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;

public class ReloadAnimation extends TimedAnimation {

  private static final Vector3f MUTABLE_VEC = new Vector3f();

  public ReloadAnimation(int lifetimeTicks) {
    super(lifetimeTicks);
  }

  @Override
  public void apply(float partialTicks, PoseStack poseStack) {
    this.applyTranslations(partialTicks, poseStack);
  }

  @Override
  public void applyArm(InteractionHand hand, HumanoidArm arm, float partialTicks, PoseStack poseStack) {
    this.applyTranslations(partialTicks, poseStack);
  }

  private void applyTranslations(float partialTicks, PoseStack poseStack) {
    float sineProgress = Mth.sin(this.lerpProgress(partialTicks) * (float) Math.PI);
    poseStack.mulPose(Vector3f.YP.rotationDegrees(sineProgress * 30.0F));
    poseStack.mulPose(Vector3f.XP.rotationDegrees(sineProgress * -30.0F));
    poseStack.translate(0.0D, 0.0D, sineProgress / 4.0D);

    applyRandomMovements(MUTABLE_VEC, partialTicks);
    poseStack.mulPose(Vector3f.XP.rotationDegrees(MUTABLE_VEC.x()));
    poseStack.mulPose(Vector3f.ZP.rotationDegrees(MUTABLE_VEC.z()));
  }

  @Override
  public void applyCamera(float partialTicks, Vector3f rotations) {
    applyRandomMovements(rotations, partialTicks);
  }

  private static void applyRandomMovements(Vector3f vec, float partialTicks) {
    final float time = Util.getMillis() / 20.0F;
    float angle1 = Mth.sin(time / 30.0F);
    float angle2 = Mth.sin(time / 25.0F);
    float angle3 = Mth.sin(time / 5.0F);
    float angle4 = Mth.sin(time / 15.0F);
    angle1 *= angle4;
    angle2 *= angle3 * angle4;
    vec.setX(-2.0F * (angle1 * 5.0F + angle2));
    vec.setZ(2.0F * angle2);
  }
}
