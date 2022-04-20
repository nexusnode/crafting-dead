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

package com.craftingdead.immerse.client.gui.view.style.shorthand;

import java.util.ArrayList;
import java.util.function.Predicate;
import com.craftingdead.immerse.client.gui.view.property.Transition;
import com.craftingdead.immerse.client.gui.view.style.PropertyDispatcher;
import com.craftingdead.immerse.client.gui.view.style.StyleSource;

public class GenericShorthandDispatcher implements PropertyDispatcher<String> {

  private final String name;
  private final PropertyDispatcher<?>[] dispatchers;

  public GenericShorthandDispatcher(String name, PropertyDispatcher<?>... dispatchers) {
    this.name = name;
    this.dispatchers = dispatchers;
  }

  @Override
  public boolean defineState(StyleSource source, String rawValue, int state) {
    var current = rawValue;
    var alreadySet = new ArrayList<PropertyDispatcher<?>>();

    while (!current.isEmpty()) {
      var anySet = false;
      for (var child : this.dispatchers) {
        if (alreadySet.contains(child)) {
          continue;
        }

        var validated = child.validate(current);

        if (validated != 0) {
          child.defineState(source, current.substring(0, validated), state);
          current = current.substring(validated).trim();

          alreadySet.add(child);
          anySet = true;
          break;
        }
      }

      if (!anySet) {
        return false;
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
}
