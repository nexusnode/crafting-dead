package com.craftingdead.immerse.client.gui.view.style.adapter;

import com.craftingdead.immerse.util.StringCountUtil;

public class DoubleTranslator
    implements StyleDecoder<Double>, StyleEncoder<Double>, StyleValidator<Double> {

  @Override
  public String encode(Double value, boolean prettyPrint) {
    return value.toString();
  }

  @Override
  public Double decode(String style) {
    if (style.contains("%")) {
      return Double.valueOf(style.replace('%', '\0')) / 100;
    }
    return Double.valueOf(style);
  }

  @Override
  public int validate(String style) {
    int doubleLength = StringCountUtil.floatAtStart(style);

    if (doubleLength == 0) {
      return 0;
    }

    if (doubleLength < style.length() && style.charAt(doubleLength) == '%') {
      return doubleLength + 1;
    }
    return doubleLength;
  }
}
