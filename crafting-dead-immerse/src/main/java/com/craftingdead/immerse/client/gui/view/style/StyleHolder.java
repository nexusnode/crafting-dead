/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view.style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.view.property.StyleableProperty;
import com.craftingdead.immerse.client.gui.view.state.States;
import com.craftingdead.immerse.client.gui.view.style.parser.TransitionParser;
import com.craftingdead.immerse.client.gui.view.style.tree.StyleList;

public class StyleHolder {

  private static final StyleSource INLINE_SOURCE = new StyleSource(StyleSource.Type.INLINE, 10_000);

  private final Map<String, PropertyDispatcher<?>> dispatchers = new HashMap<>();
  @Nullable
  private CascadeStyleable parent;
  private CascadeStyleable owner;

  private Supplier<StyleList> styleSupplier;

  public StyleHolder(CascadeStyleable owner) {
    this.owner = owner;
  }

  public void parseInlineCSS(String css) {
    for (var property : css.split(";")) {
      var split = property.split(":", 2);
      var propertyName = split[0].trim();
      this.setProperty(propertyName, split[1].trim(), INLINE_SOURCE);
    }
  }

  private void setProperty(String propertyName, String value, StyleSource source) {
    var dispatcher = this.dispatchers.get(propertyName);
    if (dispatcher != null) {
      dispatcher.defineState(source, value, 0);
    }
  }

  public <T> PropertyDispatcher<T> registerDispatcher(PropertyDispatcher<T> property) {
    this.dispatchers.put(property.getName(), property);
    return property;
  }

  public void removeDispatcher(PropertyDispatcher<?> property) {
    this.dispatchers.remove(property.getName(), property);
  }

  public void removeProperty(String name) {
    this.dispatchers.remove(name);
  }

  @SuppressWarnings("unchecked")
  public <T> StyleableProperty<T> getStyleProperty(String name, Class<T> valueClass) {
    return (StyleableProperty<T>) this.dispatchers.get(name);
  }

  public Supplier<StyleList> getStyleSupplier() {
    return this.styleSupplier;
  }

  public void setStyleSupplier(Supplier<StyleList> styleSupplier) {
    this.styleSupplier = styleSupplier;
  }

  public void refresh() {
    if (this.styleSupplier == null) {
      return;
    }

    var styleList = this.styleSupplier.get();
    if (styleList == null) {
      return;
    }

    var rules = styleList.getRulesMatching(this);

    var transitions = new ArrayList<StyleTransition>();

    this.resetToDefault();
    for (var rule : rules) {
      var source = new StyleSource(StyleSource.Type.AUTHOR, rule.selector().getSpecificity());
      TransitionParser transitionParser = null;
      for (var property : rule.properties()) {

        var dispatcher = this.dispatchers.get(property.name());
        if (dispatcher != null) {
          var state = rule.selector().getPseudoClasses().stream()
              .map(States::get)
              .flatMapToInt(OptionalInt::stream)
              .reduce(States::combine)
              .orElse(0);
          dispatcher.defineState(source, property.value(), state);
          continue;
        }

        if (TransitionParser.isTransitionProperty(property.name())) {
          if (transitionParser == null) {
            transitionParser = new TransitionParser();
          }

          transitionParser.tryParse(property.name(), property.value());
        }
      }

      if (transitionParser != null) {
        transitionParser.build().ifPresent(transitions::add);
      }
    }

    for (var transition : transitions) {
      transition.apply(this.dispatchers);
    }
  }

  @Nullable
  public CascadeStyleable getParent() {
    return this.parent;
  }

  public void setParent(@Nullable CascadeStyleable parent) {
    this.parent = parent;
  }

  public CascadeStyleable getOwner() {
    return owner;
  }

  void resetToDefault() {
    // Iterate over a copy to prevent concurrent modification.
    Map.copyOf(this.dispatchers).values().forEach(property -> property.reset(
        source -> source.is(StyleSource.Type.AUTHOR) || source.is(StyleSource.Type.USER_AGENT)));
  }
}
