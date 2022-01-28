package com.craftingdead.core.client.animation;

import java.util.ArrayDeque;
import java.util.Queue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class AnimationController {

  private final Queue<Animation> animations = new ArrayDeque<>();

  public void addAnimation(Animation animation) {
    this.animations.add(animation);
  }

  public void tick() {
    var iterator = this.animations.iterator();
    while (iterator.hasNext()) {
      var animation = iterator.next();
      animation.tick();
      if (!animation.isAlive()) {
        iterator.remove();
      }
    }
  }

  public void apply(float partialTicks, PoseStack poseStack) {
    for (var animation : this.animations) {
      if (animation.isAlive()) {
        animation.apply(partialTicks, poseStack);
      }
    }
  }

  public void applyHand(InteractionHand hand, HumanoidArm handSide, float partialTicks,
      PoseStack poseStack) {
    for (var animation : this.animations) {
      if (animation.isAlive()) {
        animation.applyHand(hand, handSide, partialTicks, poseStack);
      }
    }
  }

  public void applyCamera(float partialTicks, Vector3f rotations) {
    for (var animation : this.animations) {
      if (animation.isAlive()) {
        animation.applyCamera(partialTicks, rotations);
      }
    }
  }
}
