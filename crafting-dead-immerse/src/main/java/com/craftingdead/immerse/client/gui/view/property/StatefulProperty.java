/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

package com.craftingdead.immerse.client.gui.view.property;

import java.util.Objects;
import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.view.state.StateListener;
import com.craftingdead.immerse.client.gui.view.state.States;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class StatefulProperty<T> implements StateListener, AnimatedProperty<T> {

  private final String name;

  private final Class<T> type;

  private final ValueAccessor<T> accessor;

  private final Int2ObjectMap<T> stateValues = new Int2ObjectOpenHashMap<>();

  private Transition transition = Transition.INSTANT;

  @Nullable
  private Runnable transitionStopListener;

  @Nullable
  private T animatedValue;

  protected StatefulProperty(String name, Class<T> type, ValueAccessor<T> accessor) {
    this.name = name;
    this.type = type;
    this.accessor = accessor;
    this.resetState(0);
  }

  public final String getName() {
    return this.name;
  }

  public final Class<T> getType() {
    return this.type;
  }

  public final void setTransition(Transition transition) {
    this.transition = transition;
  }

  /**
   * Varargs version of {@link #defineState(Object, int)}.
   */
  public final void defineState(T value, int... states) {
    this.defineState(value, States.combine(states));
  }

  /**
   * User-facing method to define a state.
   * 
   * @param value - the value to associate the state with
   * @param state - the state
   */
  public void defineState(T value, int state) {
    this.setState(state, value);
  }

  /**
   * Internal method used to associate a value with a given state.
   * 
   * @param state - the state
   * @param value - the value to associate the state with
   */
  protected final void setState(int state, T value) {
    this.stateValues.put(state, value);
    if (state == 0) {
      this.set(value);
    }
  }

  /**
   * Internal method used to reset a specific state.
   * 
   * @param state - the state to reset
   */
  protected final void resetState(int state) {
    if (state == 0) {
      this.accessor.reset();
      this.setState(0, this.accessor.get());
    } else {
      this.stateValues.remove(state);
    }
  }

  @Override
  public final T get() {
    return this.animatedValue != null ? this.animatedValue : this.accessor.get();
  }

  @Override
  public final void set(T value) {
    this.accessor.set(value);
  }

  @Override
  public final boolean isBeingAnimated() {
    return this.animatedValue != null;
  }

  @Override
  public final void setAnimatedValue(@Nullable T animatedValue) {
    this.animatedValue = animatedValue;
  }

  @Override
  public boolean transition(int state, boolean animate) {
    T newValue = this.stateValues.get(state);
    if (newValue == null) {
      return false;
    }

    if (this.transitionStopListener != null) {
      this.transitionStopListener.run();
    }

    if (!Objects.equals(newValue, this.accessor.get())) {
      if (animate) {
        this.transitionStopListener = this.transition.transition(this, newValue);
      } else {
        this.set(newValue);
      }
    }

    return true;
  }

  @Override
  public String toString() {
    return this.getName();
  }

  @Override
  public int hashCode() {
    return this.getName().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof StatefulProperty<?> that && that.getName().equals(this.getName());
  }
}
