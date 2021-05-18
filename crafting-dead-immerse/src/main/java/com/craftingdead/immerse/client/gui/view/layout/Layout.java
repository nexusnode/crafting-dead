package com.craftingdead.immerse.client.gui.view.layout;

import net.minecraft.util.math.vector.Vector2f;

public interface Layout {

  float getLeft();

  float getLeftPadding();

  float getRight();

  float getRightPadding();

  float getTop();

  float getTopPadding();

  float getBottom();

  float getBottomPadding();

  float getWidth();

  float getHeight();

  void setMeasureFunction(MeasureFunction measureFunction);

  void layout();

  void close();

  interface MeasureFunction {

    Vector2f measure(MeasureMode widthMode, float width, MeasureMode heightMode, float height);
  }
}
