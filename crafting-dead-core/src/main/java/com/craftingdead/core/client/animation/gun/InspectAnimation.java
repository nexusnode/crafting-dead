/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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
  public void applyArm(InteractionHand hand, HumanoidArm handSide, float partialTicks, PoseStack poseStack) {}

  @Override
  public void applyCamera(float partialTicks, Vector3f rotations) {}
}
