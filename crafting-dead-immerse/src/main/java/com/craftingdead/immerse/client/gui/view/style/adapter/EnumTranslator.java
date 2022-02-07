package com.craftingdead.immerse.client.gui.view.style.adapter;

import java.util.Locale;

public class EnumTranslator<T extends Enum<T>>
    implements StyleDecoder<T>, StyleEncoder<T>, StyleValidator<T> {

  private final Class<T> type;

  public EnumTranslator(Class<T> type) {
    this.type = type;
  }

  @Override
  public String encode(T value, boolean prettyPrint) {
    return value.toString().toLowerCase(Locale.ROOT).replace('_', '-');
  }

  @Override
  public T decode(String style) {
    return Enum.valueOf(this.type, style.toUpperCase(Locale.ROOT).replace('-', '_'));
  }

  @Override
  public int validate(String style) {
    return style.length();
  }
}
