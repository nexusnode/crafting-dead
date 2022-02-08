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

import java.util.Objects;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class ValueProperty<T> implements StateListener {

  private final String name;

  private final Class<T> type;

  private final ValueAccessor<T> accessor;

  private final Int2ObjectMap<T> stateValues = new Int2ObjectOpenHashMap<>();

  private Transition transition = Transition.INSTANT;

  @Nullable
  private Runnable transitionStopListener;

  @Nullable
  private T overrideValue;

  protected ValueProperty(String name, Class<T> type, ValueAccessor<T> accessor) {
    this.name = name;
    this.type = type;
    this.accessor = accessor;
    this.resetState(0);
  }

  public String getName() {
    return this.name;
  }

  public Class<T> getType() {
    return this.type;
  }

  public void setTransition(Transition transition) {
    this.transition = transition;
  }

  public void defineState(T value, int state) {
    this.stateValues.put(state, value);
    if (state == 0) {
      this.set(value);
    }
  }

  public void resetState(int state) {
    if (state == 0) {
      this.accessor.reset();
      this.defineState(this.accessor.get(), 0);
    } else {
      this.stateValues.remove(state);
    }
  }

  public T get() {
    return this.overrideValue != null ? this.overrideValue : this.accessor.get();
  }

  public void set(T value) {
    this.accessor.set(value);
  }

  public boolean isBeingAnimated() {
    return this.overrideValue != null;
  }

  public void setOverrideValue(@Nullable T overrideValue) {
    this.overrideValue = overrideValue;
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
    return obj instanceof ValueProperty<?> that && that.getName().equals(this.getName());
  }
}
