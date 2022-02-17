/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

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

  public void applyArm(InteractionHand hand, HumanoidArm arm, float partialTicks,
      PoseStack poseStack) {
    for (var animation : this.animations) {
      if (animation.isAlive()) {
        animation.applyArm(hand, arm, partialTicks, poseStack);
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
