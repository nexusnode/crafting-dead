package com.craftingdead.core.client.animation.gun;

import com.craftingdead.core.client.animation.TimedAnimation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.math.Vector3f;

public class InspectAnimation extends TimedAnimation {

  public InspectAnimation() {
    super(20);
  }

  @Override
  public void apply(float partialTicks, PoseStack poseStack) {}

  @Override
  public void applyHand(InteractionHand hand, HumanoidArm handSide, float partialTicks, PoseStack poseStack) {}

  @Override
  public void applyCamera(float partialTicks, Vector3f rotations) {}
}
