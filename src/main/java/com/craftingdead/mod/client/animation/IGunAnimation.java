package com.craftingdead.mod.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IGunAnimation {

  public static enum Type {
    SHOOT;
  }

  boolean tick();

  void apply(MatrixStack matrixStack, float partialTicks);
}
