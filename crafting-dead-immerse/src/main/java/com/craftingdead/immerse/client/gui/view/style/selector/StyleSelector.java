package com.craftingdead.immerse.client.gui.view.style.selector;

import java.util.Set;
import com.craftingdead.immerse.client.gui.view.style.StyleHolder;

public interface StyleSelector {

  boolean match(StyleHolder styleHolder);

  boolean match(StyleSelector selector);

  int getSpecificity();

  StyleSelector add(StyleSelectorType type, String selector);

  Set<String> getPseudoClasses();
}
