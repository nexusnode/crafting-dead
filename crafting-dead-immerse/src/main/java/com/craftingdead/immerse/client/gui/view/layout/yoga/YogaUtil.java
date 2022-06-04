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

package com.craftingdead.immerse.client.gui.view.layout.yoga;

import org.lwjgl.util.yoga.Yoga;
import com.craftingdead.immerse.client.gui.view.layout.Align;
import com.craftingdead.immerse.client.gui.view.layout.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.Justify;
import com.craftingdead.immerse.client.gui.view.layout.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.Wrap;

public class YogaUtil {

  public static int getPositionType(PositionType positionType) {
    return switch (positionType) {
      case RELATIVE -> Yoga.YGPositionTypeRelative;
      case ABSOLUTE -> Yoga.YGPositionTypeAbsolute;
    };
  }

  public static int getFlexDirection(FlexDirection flexDirection) {
    return switch (flexDirection) {
      case COLUMN -> Yoga.YGFlexDirectionColumn;
      case COLUMN_REVERSE -> Yoga.YGFlexDirectionColumnReverse;
      case ROW -> Yoga.YGFlexDirectionRow;
      case ROW_REVERSE -> Yoga.YGFlexDirectionRowReverse;
    };
  }

  public static int getWrap(Wrap wrap) {
    return switch (wrap) {
      case NO_WRAP -> Yoga.YGWrapNoWrap;
      case WRAP -> Yoga.YGWrapWrap;
      case WRAP_REVERSE -> Yoga.YGWrapReverse;
    };
  }

  public static int getAlign(Align align) {
    return switch (align) {
      case AUTO -> Yoga.YGAlignAuto;
      case FLEX_START -> Yoga.YGAlignFlexStart;
      case CENTER -> Yoga.YGAlignCenter;
      case FLEX_END -> Yoga.YGAlignFlexEnd;
      case STRETCH -> Yoga.YGAlignStretch;
      case BASELINE -> Yoga.YGAlignBaseline;
      case SPACE_BETWEEN -> Yoga.YGAlignSpaceBetween;
      case SPACE_AROUND -> Yoga.YGAlignSpaceAround;
    };
  }

  public static int getJustify(Justify justify) {
    return switch (justify) {
      case FLEX_START -> Yoga.YGJustifyFlexStart;
      case CENTER -> Yoga.YGJustifyCenter;
      case FLEX_END -> Yoga.YGJustifyFlexEnd;
      case SPACE_BETWEEN -> Yoga.YGJustifySpaceBetween;
      case SPACE_AROUND -> Yoga.YGJustifySpaceAround;
      case SPACE_EVENLY -> Yoga.YGJustifySpaceEvenly;
    };
  }
}
