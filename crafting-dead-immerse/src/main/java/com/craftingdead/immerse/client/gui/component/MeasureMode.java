package com.craftingdead.immerse.client.gui.component;

import org.lwjgl.util.yoga.Yoga;

public enum MeasureMode {
  AT_MOST(Yoga.YGMeasureModeAtMost), EXACTLY(Yoga.YGMeasureModeExactly), UNDEFINED(
      Yoga.YGMeasureModeUndefined);

  private final int yogaType;

  private MeasureMode(int yogaType) {
    this.yogaType = yogaType;
  }

  public int getYogaType() {
    return this.yogaType;
  }

  public static MeasureMode fromYogaType(int yogaType) {
    for (MeasureMode measureMode : values()) {
      if (measureMode.yogaType == yogaType) {
        return measureMode;
      }
    }
    throw new IllegalArgumentException("Invalid yoga type");
  }
}
