package com.craftingdead.core.capability.animation;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IAnimation {

  void apply(MatrixStack matrixStack, double progress);

  float getLength();
}
