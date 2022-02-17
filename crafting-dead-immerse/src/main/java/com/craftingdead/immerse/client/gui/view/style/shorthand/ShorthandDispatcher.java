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

package com.craftingdead.immerse.client.gui.view.style.shorthand;

import java.util.ArrayList;
import java.util.function.Predicate;
import com.craftingdead.immerse.client.gui.view.property.Transition;
import com.craftingdead.immerse.client.gui.view.style.PropertyDispatcher;
import com.craftingdead.immerse.client.gui.view.style.StyleSource;
import com.craftingdead.immerse.client.gui.view.style.adapter.StyleTranslatorRegistry;
import com.craftingdead.immerse.client.gui.view.style.adapter.StyleValidator;

public class ShorthandDispatcher<T> implements PropertyDispatcher<T> {

  private final String name;
  private final StyleValidator<T> validator;
  private final ShorthandArgMapper argMapper;
  private final PropertyDispatcher<T>[] dispatchers;

  @SafeVarargs
  public ShorthandDispatcher(String name, StyleValidator<T> validator, ShorthandArgMapper argMapper,
      PropertyDispatcher<T>... dispatchers) {
    this.name = name;
    this.validator = validator;
    this.argMapper = argMapper;
    this.dispatchers = dispatchers;
  }

  @Override
  public boolean defineState(StyleSource source, String rawValue, int state) {
    var values = new ArrayList<String>();
    var current = rawValue;
    while (!current.isEmpty()) {
      var validated = this.validator.validate(current);
      if (validated == 0) {
        break;
      }

      values.add(current.substring(0, validated));
      current = current.substring(validated).trim();
    }

    for (int i = 0; i < values.size(); i++) {
      for (int childIndex : this.argMapper.map(i, values.size())) {
        this.dispatchers[childIndex].defineState(source, values.get(i), state);
      }
    }

    return true;
  }

  @Override
  public void reset(Predicate<StyleSource> filter) {}

  @Override
  public String getName() {
    return this.name;
  }
  
  @Override
  public void setTransition(Transition transition) {
    for (var dispatcher : this.dispatchers) {
      dispatcher.setTransition(transition);
    }
  }

  @SafeVarargs
  public static <T> ShorthandDispatcher<T> create(String name, Class<T> type,
      ShorthandArgMapper argMapper, PropertyDispatcher<T>... properties) {
    return new ShorthandDispatcher<>(name, StyleTranslatorRegistry.getInstance().getValidator(type),
        argMapper, properties);
  }
}
