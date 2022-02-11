package com.craftingdead.immerse.client.gui.view.style;

public record StyleSource(Type type, int specificity) implements Comparable<StyleSource> {

  public static final StyleSource DEFAULT = new StyleSource(Type.USER_AGENT, 0);
  public static final StyleSource CODE = new StyleSource(Type.CODE, 10_000);

  public boolean is(Type type) {
    return this.type == type;
  }

  @Override
  public int compareTo(StyleSource o) {
    if (this.type.ordinal() > o.type.ordinal()) {
      return 1;
    }

    if (this.type.ordinal() == o.type.ordinal()) {
      if (this.specificity > o.specificity) {
        return 1;
      } else if (this.specificity == o.specificity) {
        return 0;
      }
    }

    return -1;
  }

  public enum Type {

    USER_AGENT, AUTHOR, INLINE, CODE
  }
}
