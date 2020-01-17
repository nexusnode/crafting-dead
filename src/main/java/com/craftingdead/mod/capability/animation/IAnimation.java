package com.craftingdead.mod.capability.animation;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IAnimation {

  boolean tick();

  void apply(MatrixStack matrixStack, float partialTicks);
}
