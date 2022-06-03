/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view.style;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.craftingdead.immerse.client.gui.view.style.parser.TransitionParser;

public class StyleManager {

  private final Map<String, PropertyDispatcher<?>> properties = new HashMap<>();
  private final List<PropertyDispatcher<?>> listeners = new ArrayList<>();

  private final StyleNode owner;

  private int state;

  public StyleManager(StyleNode owner) {
    this.owner = owner;
  }

  public StyleNode getOwner() {
    return this.owner;
  }

  public int getState() {
    return this.state;
  }

  public void addState(int state) {
    this.state |= state;
  }

  public void removeState(int state) {
    this.state &= ~state;
  }

  public boolean hasState(int state) {
    return (this.state & state) == state;
  }

  public boolean toggleState(int state) {
    if (this.hasState(state)) {
      this.removeState(state);
      return false;
    }
    this.addState(state);
    return true;
  }

  public void addListener(PropertyDispatcher<?> listener) {
    this.listeners.add(listener);
  }

  public void removeListener(PropertyDispatcher<?> listener) {
    this.listeners.remove(listener);
  }

  public void notifyListeners() {
    for (var listener : this.listeners) {
      listener.refreshState();
    }
  }

  public void parseInlineCSS(String css) {
    for (var propertyStr : css.split(";")) {
      var split = propertyStr.split(":", 2);
      var propertyName = split[0].trim();
      var property = this.properties.get(propertyName);
      if (property != null) {
        property.defineState(split[1].trim(), 1000, Set.of());
      }
    }
  }

  public Collection<PropertyDispatcher<?>> getProperties() {
    return this.properties.values();
  }

  public <T> PropertyDispatcher<T> registerProperty(PropertyDispatcher<T> property) {
    this.properties.put(property.getName(), property);
    return property;
  }

  public void refresh() {
    var styleList = this.owner.getStyleList();
    if (styleList == null) {
      return;
    }

    var rules = styleList.getRulesMatching(this.owner);
    var transitions = new ArrayList<StyleTransition>();

    this.properties.values().forEach(PropertyDispatcher::reset);

    for (var entry : rules.entrySet()) {
      var rule = entry.getKey();
      var nodeStates = entry.getValue();
      TransitionParser transitionParser = null;
      for (var property : rule.properties()) {
        var dispatcher = this.properties.get(property.name());
        if (dispatcher != null) {
          dispatcher.defineState(property.value(), rule.selector().getSpecificity(),
              nodeStates);
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
      transition.apply(this.properties);
    }

    this.properties.values().forEach(PropertyDispatcher::refreshState);

    this.owner.styleRefreshed(styleList.createFontManager());
  }
}
