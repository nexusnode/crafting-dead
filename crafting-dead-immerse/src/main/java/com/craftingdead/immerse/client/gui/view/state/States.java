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

package com.craftingdead.immerse.client.gui.view.state;

import java.util.OptionalInt;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;

public class States {

  private static final int NILL = -1;

  private static final Object2IntMap<String> states =
      Util.make(new Object2IntOpenHashMap<>(), map -> map.defaultReturnValue(NILL));

  public static final int ENABLED = register("enabled");
  public static final int DISABLED = register("disabled");
  public static final int HOVER = register("hover");
  public static final int FOCUS = register("focus");
  public static final int FOCUS_VISIBLE = register("focus-visible");

  private static int n = 0;

  private static int register(String name) {
    var value = 1 << n++;
    states.put(name, value);
    return value;
  }

  public static OptionalInt get(String name) {
    var value = states.getInt(name);
    return value == NILL ? OptionalInt.empty() : OptionalInt.of(value);
  }

  public static int combine(int... states) {
    var combined = 0;
    for (int i = 0; i < states.length; i++) {
      combined |= states[i];
    }
    return combined;
  }

  public static IntSet split(int state) {
    var splitStates = new IntOpenHashSet();
    states.values().forEach(value -> {
      if ((state & value) == value) {
        splitStates.add(value);
      }
    });
    return splitStates;
  }
}
