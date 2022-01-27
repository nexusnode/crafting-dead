package com.craftingdead.core.client.animation.gun;

import com.craftingdead.core.client.animation.TimedAnimation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;

public class SubmachineShootAnimation extends TimedAnimation {

  public SubmachineShootAnimation() {
    super(5);
  }

  @Override
  public void apply(float partialTicks, MatrixStack poseStack) {

  }

  @Override
  public void applyHand(Hand hand, HandSide handSide, float partialTicks, MatrixStack poseStack) {

  }

  @Override
  public void applyCamera(float partialTicks, Vector3f rotations) {

  }
}
