package com.craftingdead.immerse.client.gui.view.style.adapter;

import com.craftingdead.immerse.client.gui.view.Point;
import com.craftingdead.immerse.util.StringCountUtil;

public class PointTranslator
    implements StyleEncoder<Point>, StyleDecoder<Point>, StyleValidator<Point> {

  @Override
  public String encode(Point value, boolean prettyPrint) {
    return switch (value.type()) {
      case FIXED -> String.valueOf(value.value());
      case PERCENTAGE -> String.valueOf(value.value()) + "%";
      case AUTO -> "auto";
    };
  }

  @Override
  public Point decode(String style) {
    if (style.contains("%")) {
      return Point.percentage(Float.parseFloat(style.replace('%', '\0')));
    } else if (style.equals("auto")) {
      return Point.AUTO;
    } else {
      return Point.fixed(Float.parseFloat(style));
    }
  }

  @Override
  public int validate(String style) {
    int floatLength = StringCountUtil.floatAtStart(style);

    if (floatLength == 0) {
      return 0;
    }

    if (floatLength < style.length() && style.charAt(floatLength) == '%') {
      return floatLength + 1;
    }
    return floatLength;
  }
}
