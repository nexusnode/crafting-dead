package com.craftingdead.mod.capability.animation;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IAnimationController {

  void tick();

  void addAnimation(IAnimation animation);

  void cancelCurrentAnimation();

  void clearAnimations();

  void apply(MatrixStack matrixStack);
}
