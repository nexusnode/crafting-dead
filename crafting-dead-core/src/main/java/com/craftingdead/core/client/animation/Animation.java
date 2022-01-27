package com.craftingdead.core.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;

public interface Animation {

  void apply(float partialTicks, MatrixStack poseStack);

  void applyHand(Hand hand, HandSide handSide, float partialTicks, MatrixStack poseStack);

  void applyCamera(float partialTicks, Vector3f rotations);

  void tick();

  boolean isAlive();

  void remove();

  default boolean handlePerspective(ItemCameraTransforms.TransformType transformType) {
    return true;
  }
}
