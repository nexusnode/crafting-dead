package com.craftingdead.immerse.client.gui.view.style.adapter;

import java.lang.reflect.Array;

public class ArrayParser<T> implements StyleParser<T[]> {

  private final Class<T> type;
  private final StyleParser<T> elementParser;

  public ArrayParser(Class<T> type, StyleParser<T> elementParser) {
    this.type = type;
    this.elementParser = elementParser;
  }

  @Override
  public int validate(String style) {
    return style.length();
  }

  @Override
  public T[] parse(String style) {
    var elements = style.replace("'", "").replace("\"", "").split(",");
    @SuppressWarnings("unchecked")
    T[] result = (T[]) Array.newInstance(this.type, elements.length);
    for (int i = 0; i < elements.length; i++) {
      result[i] = this.elementParser.parse(elements[i].trim());
    }
    return result;
  }
}
