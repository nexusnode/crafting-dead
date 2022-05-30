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

package com.craftingdead.immerse.client.gui.view.style.state;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.immerse.client.gui.view.style.StyleNode;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleNodeState;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public class StateManager {

  private final List<StateListener> listeners = new ArrayList<>();

  private final StyleNode owner;

  private int state;

  public StateManager(StyleNode owner) {
    this.owner = owner;
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

  public void addListener(StateListener listener) {
    this.listeners.add(listener);
  }

  public void removeListener(StateListener listener) {
    this.listeners.remove(listener);
  }

  public void notifyListeners() {
    var powerSet = powerBitSet(States.split(this.state)).toIntArray();
    for (var property : this.listeners) {
      for (int i = powerSet.length - 1; i >= 0; i--) {
        if (property.transition(new StyleNodeState(this.owner, powerSet[i]))) {
          break;
        }
      }
    }
  }

  public static IntSet powerBitSet(IntSet set) {
    if (set.isEmpty()) {
      return IntSet.of(0);
    }

    var firstElement = set.iterator().nextInt();
    var subsetWithoutFirstElement = new IntOpenHashSet();
    set.forEach(element -> {
      if (element != firstElement) {
        subsetWithoutFirstElement.add(element);
      }
    });

    var subsetWithoutFirstElementPowerSet = powerBitSet(subsetWithoutFirstElement);

    var powerSet = new IntOpenHashSet();
    for (var subset : subsetWithoutFirstElementPowerSet) {
      powerSet.add(firstElement | subset);
    }

    powerSet.addAll(subsetWithoutFirstElementPowerSet);
    return powerSet;
  }
}
