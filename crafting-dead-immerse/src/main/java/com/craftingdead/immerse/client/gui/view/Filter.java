package com.craftingdead.immerse.client.gui.view;

import io.github.humbleui.skija.FilterTileMode;
import io.github.humbleui.skija.ImageFilter;
import io.github.humbleui.skija.Paint;

public interface Filter {

  void apply(Paint paint);

  static Filter blur(float sigma) {
    return paint -> paint.setImageFilter(ImageFilter.makeBlur(sigma, sigma, FilterTileMode.CLAMP));
  }
}
