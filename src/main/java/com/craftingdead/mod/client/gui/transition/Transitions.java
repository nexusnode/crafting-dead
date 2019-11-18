package com.craftingdead.mod.client.gui.transition;

import com.craftingdead.mod.client.gui.transition.TransitionManager.TransitionType;
import com.mojang.blaze3d.platform.GlStateManager;

public enum Transitions implements ITransition {

  GROW {

    private static final float SCALE = 0.075F;

    @Override
    public void transform(double width, double height, float progress) {
      float scaleOffset = (1.0F - progress) * SCALE;
      GlStateManager.translated(width * -scaleOffset, height * -scaleOffset, 0.0F);
      GlStateManager.scalef(1.0F + 2.0F * scaleOffset, 1.0F + 2.0F * scaleOffset, 1.0F);
    }

    @Override
    public int getTransitionTime() {
      return 200;
    }

    @Override
    public TransitionType getTransitionType() {
      return TransitionType.SINE;
    }
  };
}
