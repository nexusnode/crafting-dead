package com.craftingdead.immerse.client.gui.view.style.tree;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleSelector;

public record StyleRule(StyleSelector selector, List<StyleProperty> properties) {

  public StyleRule(StyleSelector selector) {
    this(selector, new ArrayList<>());
  }

  public void mergeProperties(Iterable<StyleProperty> properties) {
    properties.forEach(rule -> {
      if (this.properties.stream().noneMatch(other -> other.name().equals(rule.name()))) {
        this.addProperty(rule);
      }
    });
  }

  public StyleRule addProperty(StyleProperty rule) {
    if (!this.properties.contains(rule)) {
      this.properties.add(rule);
    }
    return this;
  }
}
