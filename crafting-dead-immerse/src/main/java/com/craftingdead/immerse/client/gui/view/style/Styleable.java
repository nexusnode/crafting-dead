package com.craftingdead.immerse.client.gui.view.style;

import java.util.Set;

public interface Styleable {

  String getId();

  Set<String> getStyleClasses();

  StyleHolder getStyle();

  default void setStyle(String style) {
    this.getStyle().parseInlineCSS(style);
  }

  String getType();
}
