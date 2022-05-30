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
import java.util.Map;
import com.craftingdead.immerse.client.gui.view.style.parser.TransitionParser;

public class StyleManager {

  private static final StyleSource INLINE_SOURCE = new StyleSource(StyleSource.Type.INLINE, 10_000);

  private final Map<String, PropertyDispatcher<?>> properties = new HashMap<>();

  private final StyleNode owner;

  public StyleManager(StyleNode owner) {
    this.owner = owner;
  }

  public StyleNode getOwner() {
    return this.owner;
  }

  public void parseInlineCSS(String css) {
    for (var propertyStr : css.split(";")) {
      var split = propertyStr.split(":", 2);
      var propertyName = split[0].trim();
      var property = this.properties.get(propertyName);
      if (property != null) {
        property.defineState(INLINE_SOURCE, split[1].trim(), null);
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

    this.resetToDefault();
    for (var entry : rules.entrySet()) {
      var rule = entry.getKey();
      var nodeStates = entry.getValue();
      var source = new StyleSource(StyleSource.Type.AUTHOR, rule.selectorList().getSpecificity());
      TransitionParser transitionParser = null;
      for (var definition : rule.properties()) {
        var property = this.properties.get(definition.name());
        if (property != null) {
          property.defineState(source, definition.value(), nodeStates);
          continue;
        }

        if (TransitionParser.isTransitionProperty(definition.name())) {
          if (transitionParser == null) {
            transitionParser = new TransitionParser();
          }

          transitionParser.tryParse(definition.name(), definition.value());
        }
      }

      if (transitionParser != null) {
        transitionParser.build().ifPresent(transitions::add);
      }
    }

    for (var transition : transitions) {
      transition.apply(this.properties);
    }

    this.owner.styleRefreshed(styleList.createFontManager());
  }


  private void resetToDefault() {
    this.properties.values().forEach(property -> property.reset(
        source -> source.is(StyleSource.Type.AUTHOR) || source.is(StyleSource.Type.USER_AGENT)));
  }
}
