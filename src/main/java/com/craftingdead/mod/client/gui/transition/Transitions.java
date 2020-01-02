package com.craftingdead.mod.client.gui.transition;

import com.craftingdead.mod.client.gui.transition.TransitionManager.TransitionType;
import com.mojang.blaze3d.systems.RenderSystem;

public enum Transitions implements ITransition {

  GROW {

    private static final float SCALE = 0.075F;

    @Override
    public void transform(double width, double height, float progress) {
      float scaleOffset = (1.0F - progress) * SCALE;
      RenderSystem.translated(width * -scaleOffset, height * -scaleOffset, 0.0F);
      RenderSystem.scalef(1.0F + 2.0F * scaleOffset, 1.0F + 2.0F * scaleOffset, 1.0F);
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
