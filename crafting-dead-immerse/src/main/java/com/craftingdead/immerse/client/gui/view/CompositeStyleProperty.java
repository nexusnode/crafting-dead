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

import java.util.Set;
import javax.annotation.Nonnull;
import com.google.common.collect.Sets;

public class CompositeStyleProperty<T> extends StyleProperty<T> {

  private final Set<StyleProperty<T>> properties;

  @SafeVarargs
  protected CompositeStyleProperty(String name, StyleProperty<T>... properties) {
    super(name);
    this.properties = Sets.newHashSet(properties);
  }

  @Override
  protected StyleProperty<T> setBaseValue(@Nonnull T baseValue, boolean define) {
    this.properties.forEach(property -> property.setBaseValue(baseValue, define));
    return this;
  }

  @Override
  public StyleProperty<T> setTransition(Transition<T> transition) {
    this.properties.forEach(property -> property.setTransition(transition));
    return this;
  }

  @Override
  public StyleProperty<T> defineState(T value, State... states) {
    this.properties.forEach(property -> property.defineState(value, states));
    return this;
  }

  @Override
  public StyleProperty<T> set(@Nonnull T value) {
    this.properties.forEach(property -> property.set(value));
    return this;
  }

  @SafeVarargs
  public static <T> CompositeStyleProperty<T> create(String name, StyleProperty<T>... properties) {
    return new CompositeStyleProperty<>(name, properties);
  }
}
