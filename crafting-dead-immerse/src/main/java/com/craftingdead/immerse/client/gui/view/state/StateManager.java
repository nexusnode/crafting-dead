package com.craftingdead.immerse.client.gui.view.state;

import java.util.ArrayList;
import java.util.List;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public class StateManager {

  private final List<StateListener> listeners = new ArrayList<>();

  private int state;

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

  public void notifyListeners(boolean animate) {
    var powerSet = powerBitSet(States.split(this.state)).toIntArray();
    for (var property : this.listeners) {
      for (int i = powerSet.length - 1; i >= 0; i--) {
        if (property.transition(powerSet[i], animate)) {
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
