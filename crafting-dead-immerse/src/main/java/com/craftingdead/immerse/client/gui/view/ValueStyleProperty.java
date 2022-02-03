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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class ValueStyleProperty<T> extends StyleProperty<T> {

  private final Class<T> type;

  private boolean defined;

  private T baseValue;

  private final Int2ObjectMap<T> stateValues = new Int2ObjectOpenHashMap<>();

  private T value;

  private Transition<T> transition = Transition.instant();

  @Nullable
  private Runnable transitionStopListener;

  @Nullable
  private T overrideValue;

  protected ValueStyleProperty(String name, Class<T> type, @Nonnull T baseValue) {
    super(name);
    this.type = type;
    this.setBaseValue(baseValue, false);
  }

  @Override
  protected StyleProperty<T> setBaseValue(@Nonnull T baseValue, boolean defined) {
    Objects.nonNull(baseValue);
    this.baseValue = baseValue;
    this.defined = defined;
    this.stateValues.put(0, this.baseValue);
    this.value = this.baseValue;
    return this;
  }

  @Override
  public StyleProperty<T> setTransition(Transition<T> transition) {
    this.transition = transition;
    return this;
  }

  @Override
  public StyleProperty<T> defineState(T value, int state) {
    if (state == 0) {
      return this;
    }
    this.stateValues.put(state, value);
    return this;
  }

  @Override
  public StyleProperty<T> set(@Nonnull T value) {
    this.value = value;
    return this;
  }

  public boolean isDefined() {
    return this.defined;
  }

  public void setOverrideValue(@Nullable T overrideValue) {
    this.overrideValue = overrideValue;
  }

  public T get() {
    return this.overrideValue != null ? this.overrideValue : this.value;
  }

  public boolean transition(int state, boolean animate) {
    T newValue = this.stateValues.get(state);
    if (newValue == null) {
      return false;
    }

    if (this.transitionStopListener != null) {
      this.transitionStopListener.run();
    }

    if (!Objects.equals(newValue, this.value)) {
      if (animate) {
        this.transitionStopListener = this.transition.transition(this, newValue);
      } else {
        this.set(newValue);
      }
    }

    return true;
  }

  public Class<T> getValueType() {
    return this.type;
  }

  public static <T> ValueStyleProperty<T> create(String name, Class<T> type, @Nonnull T baseValue) {
    return new ValueStyleProperty<>(name, type, baseValue);
  }
}
