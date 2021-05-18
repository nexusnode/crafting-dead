package com.craftingdead.immerse.client.gui.view.layout;

public interface EmptyLayout extends Layout {

  @Override
  default float getLeft() {
    return 0;
  }

  @Override
  default float getLeftPadding() {
    return 0;
  }

  @Override
  default float getRight() {
    return 0;
  }

  @Override
  default float getRightPadding() {
    return 0;
  }

  @Override
  default float getTop() {
    return 0;
  }

  @Override
  default float getTopPadding() {
    return 0;
  }

  @Override
  default float getBottom() {
    return 0;
  }

  @Override
  default float getBottomPadding() {
    return 0;
  }

  @Override
  default void setMeasureFunction(MeasureFunction measureFunction) {}

  @Override
  default void layout() {}

  @Override
  default void close() {}
}
