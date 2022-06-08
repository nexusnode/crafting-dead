package sm0keysa1m0n.bliss.layout.yoga;

import org.lwjgl.util.yoga.Yoga;
import sm0keysa1m0n.bliss.layout.Align;
import sm0keysa1m0n.bliss.layout.FlexDirection;
import sm0keysa1m0n.bliss.layout.Justify;
import sm0keysa1m0n.bliss.layout.PositionType;
import sm0keysa1m0n.bliss.layout.Wrap;

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
