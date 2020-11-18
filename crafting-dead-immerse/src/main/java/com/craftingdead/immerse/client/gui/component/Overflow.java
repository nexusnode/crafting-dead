package com.craftingdead.immerse.client.gui.component;

import org.lwjgl.util.yoga.Yoga;

public enum Overflow {
  VISIBLE(Yoga.YGOverflowVisible), HIDDEN(Yoga.YGOverflowHidden), SCROLL(Yoga.YGOverflowScroll);

  private final int yogaType;

  private Overflow(int yogaType) {
    this.yogaType = yogaType;
  }

  public int getYogaType() {
    return this.yogaType;
  }

  public static Overflow fromYogaType(int yogaType) {
    for (Overflow overflow : values()) {
      if (overflow.yogaType == yogaType) {
        return overflow;
      }
    }
    throw new IllegalArgumentException("Invalid yoga type");
  }
}
