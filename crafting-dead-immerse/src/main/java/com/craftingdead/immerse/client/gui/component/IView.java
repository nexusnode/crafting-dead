package com.craftingdead.immerse.client.gui.component;

import net.minecraft.client.gui.screen.Screen;

public interface IView {

  default float getContentXOffset() {
    return 0.0F;
  }

  default float getContentYOffset() {
    return 0.0F;
  }

  float getWidth();

  float getHeight();

  Screen getScreen();
}
