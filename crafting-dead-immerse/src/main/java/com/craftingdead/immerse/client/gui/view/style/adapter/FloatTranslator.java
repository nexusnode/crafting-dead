package com.craftingdead.immerse.client.gui.view.style.adapter;

import com.craftingdead.immerse.util.StringCountUtil;

public class FloatTranslator
    implements StyleDecoder<Float>, StyleEncoder<Float>, StyleValidator<Float> {

  @Override
  public String encode(Float value, boolean prettyPrint) {
    return value.toString();
  }

  @Override
  public Float decode(String style) {
    if (style.contains("%")) {
      return Float.valueOf(style.replace('%', '\0')) / 100;
    }
    return Float.valueOf(style);
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
