package com.craftingdead.core.client.animation;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;

public class AnimationController {

  private final Queue<Animation> animations = new ArrayDeque<>();

  public void addAnimation(Animation animation) {
    this.animations.add(animation);
  }

  public void tick() {
    Iterator<Animation> iterator = this.animations.iterator();
    while (iterator.hasNext()) {
      Animation animation = iterator.next();
      animation.tick();
      if (!animation.isAlive()) {
        iterator.remove();
      }
    }
  }

  public void apply(float partialTicks, MatrixStack poseStack) {
    for (Animation animation : this.animations) {
      if (animation.isAlive()) {
        animation.apply(partialTicks, poseStack);
      }
    }
  }

  public void applyHand(Hand hand, HandSide handSide, float partialTicks, MatrixStack poseStack) {
    for (Animation animation : this.animations) {
      if (animation.isAlive()) {
        animation.applyHand(hand, handSide, partialTicks, poseStack);
      }
    }
  }

  public void applyCamera(float partialTicks, Vector3f rotations) {
    for (Animation animation : this.animations) {
      if (animation.isAlive()) {
        animation.applyCamera(partialTicks, rotations);
      }
    }
  }
}
