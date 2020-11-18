package com.craftingdead.immerse.client.gui.component;

import org.lwjgl.util.yoga.Yoga;

public enum FlexDirection {
  COLUMN(Yoga.YGFlexDirectionColumn), COLUMN_REVERSE(Yoga.YGFlexDirectionColumnReverse), ROW(
      Yoga.YGFlexDirectionRow), ROW_REVERSE(Yoga.YGFlexDirectionRowReverse);

  private final int yogaType;

  private FlexDirection(int yogaType) {
    this.yogaType = yogaType;

  }

  public int getYogaType() {
    return this.yogaType;
  }
}
