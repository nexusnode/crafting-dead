/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.client.gui.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public class States {

  private static final Map<String, State> states = new HashMap<>();

  public static final State ENABLED = register("enabled");
  public static final State DISABLED = register("disabled");
  public static final State HOVER = register("hover");
  public static final State FOCUS = register("focus");
  public static final State FOCUS_VISIBLE = register("focus-visible");

  private static int n = 0;

  private static State register(String name) {
    var value = 1 << n++;
    var state = new State(name, value);
    states.put(name, state);
    return state;
  }

  public static Optional<State> get(String name) {
    return Optional.ofNullable(states.get(name));
  }

  public static int combine(int... states) {
    var combined = 0;
    for (int i = 0; i < states.length; i++) {
      combined |= states[i];
    }
    return combined;
  }

  public static int combine(Collection<Integer> collection) {
    var combined = 0;
    for (var state : collection) {
      combined |= state;
    }
    return combined;
  }

  public static IntSet split(int state) {
    var states = new IntOpenHashSet();
    States.states.values().forEach(s -> {
      if ((state & s.value()) == s.value()) {
        states.add(s.value());
      }
    });
    return states;
  }
}
