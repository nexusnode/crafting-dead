package com.craftingdead.mod.client.animation;

import javax.vecmath.Matrix4f;

public interface IGunAnimation {

  public static enum Type {
    SHOOT;
  }

  boolean tick();

  void apply(Matrix4f matrix, float partialTicks);
}
