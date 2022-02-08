package com.craftingdead.immerse.client.gui.view.style;

import java.util.function.Predicate;
import com.craftingdead.immerse.client.gui.view.Transition;

public interface PropertyDispatcher<T> {

  void setTransition(Transition transition);

  boolean defineState(StyleSource source, String style, int state);

  void reset(Predicate<StyleSource> filter);

  default int validate(String style) {
    return style.length();
  }

  String getName();
}
