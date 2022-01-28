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

import javax.annotation.Nonnull;

public abstract class StyleProperty<T> {

  private final String name;

  protected StyleProperty(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public StyleProperty<T> setBaseValue(@Nonnull T baseValue) {
    return this.setBaseValue(baseValue, true);
  }

  protected abstract StyleProperty<T> setBaseValue(@Nonnull T baseValue, boolean define);

  public abstract StyleProperty<T> setTransition(Transition<T> transition);

  public abstract StyleProperty<T> defineState(T value, State... states);

  public abstract StyleProperty<T> set(@Nonnull T value);

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
    return obj instanceof ValueStyleProperty
        && ((ValueStyleProperty<?>) obj).getName().equals(this.getName());
  }
}
