package com.craftingdead.core.client.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.math.Vector3f;

public interface Animation {

  void apply(float partialTicks, PoseStack poseStack);

  void applyHand(InteractionHand hand, HumanoidArm handSide, float partialTicks, PoseStack poseStack);

  void applyCamera(float partialTicks, Vector3f rotations);

  void tick();

  boolean isAlive();

  void remove();

  default boolean handlePerspective(ItemTransforms.TransformType transformType) {
    return true;
  }
}
