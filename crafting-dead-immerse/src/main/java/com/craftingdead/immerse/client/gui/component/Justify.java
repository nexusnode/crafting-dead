package com.craftingdead.immerse.client.gui.component;

import org.lwjgl.util.yoga.Yoga;

public enum Justify {
  FLEX_START(Yoga.YGJustifyFlexStart), CENTER(Yoga.YGJustifyCenter), FLEX_END(
      Yoga.YGJustifyFlexEnd), SPACE_BETWEEN(Yoga.YGJustifySpaceBetween), SPACE_AROUND(
          Yoga.YGJustifySpaceAround), SPACE_EVENLY(Yoga.YGJustifySpaceEvenly);

  private final int yogaType;

  private Justify(int yogaType) {
    this.yogaType = yogaType;
  }

  public int getYogaType() {
    return this.yogaType;
  }
}
