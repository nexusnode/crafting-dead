package com.craftingdead.immerse.client.gui.view.style.adapter;

import com.craftingdead.immerse.client.gui.view.style.Percentage;
import com.craftingdead.immerse.util.StringCountUtil;

public class PercentageParser implements StyleParser<Percentage> {

  public static final PercentageParser INSTANCE = new PercentageParser();

  private PercentageParser() {}

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

  @Override
  public Percentage parse(String style) {
    return style.contains("%")
        ? new Percentage(Float.parseFloat(style.replace("%", "")) / 100.0F)
        : new Percentage(Float.parseFloat(style));
  }
}
