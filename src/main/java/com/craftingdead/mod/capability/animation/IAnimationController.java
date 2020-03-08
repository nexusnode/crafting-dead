package com.craftingdead.mod.capability.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

@SuppressWarnings("deprecation")
public interface IAnimationController {

  void addAnimation(IAnimation animation);

  void cancelCurrentAnimation();

  void clearAnimations();

  void apply(MatrixStack matrixStack);

  boolean isAcceptedPerspective(ItemCameraTransforms.TransformType cameraTransformType);
}
