/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view.layout;

import com.craftingdead.immerse.client.gui.view.ViewStyle;

public interface LayoutParent {

  LayoutParent NILL = new LayoutParent() {

    @Override
    public Layout addChild(int index) {
      return Layout.NILL;
    }

    @Override
    public void removeChild(Layout layout) {}

    @Override
    public void layout(float width, float height) {}

    @Override
    public float getContentWidth() {
      return 0.0F;
    }

    @Override
    public float getContentHeight() {
      return 0.0F;
    }

    @Override
    public void close() {}
  };

  default void setAll(ViewStyle style) {}

  default void setFlexDirection(FlexDirection flexDirection) {}

  default void setFlexWrap(Wrap flexDirection) {}

  default void setAlignItems(Align align) {}

  default void setAlignContent(Align align) {}

  default void setJustifyContent(Justify justify) {}

  Layout addChild(int index);

  void removeChild(Layout layout);

  /**
   * Calculate the layout.
   * 
   * @param width - width of container or {@link Float#NaN} to let layout decide.
   * @param height - height of container or {@link Float#NaN} to let layout decide.
   */
  void layout(float width, float height);

  float getContentWidth();

  float getContentHeight();

  void close();
}
