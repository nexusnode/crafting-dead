package com.craftingdead.core.client.animation.gun;

import com.craftingdead.core.client.animation.TimedAnimation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.math.Vector3f;

public class SubmachineShootAnimation extends TimedAnimation {

  public SubmachineShootAnimation() {
    super(5);
  }

  @Override
  public void apply(float partialTicks, PoseStack poseStack) {

  }

  @Override
  public void applyArm(InteractionHand hand, HumanoidArm arm, float partialTicks, PoseStack poseStack) {

  }

  @Override
  public void applyCamera(float partialTicks, Vector3f rotations) {

  }
}
