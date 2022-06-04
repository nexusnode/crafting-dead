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

package com.craftingdead.immerse.client.gui.view.property;

import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.Lists;

public class BaseProperty<T> implements AnimatableProperty<T> {

  private final String name;

  private final Class<T> type;

  private final T defaultValue;

  private final List<Consumer<T>> listeners;

  private T value;

  @Nullable
  private T animatedValue;

  @SafeVarargs
  public BaseProperty(String name, Class<T> type, T defaultValue, Consumer<T>... listeners) {
    this.name = name;
    this.type = type;
    this.defaultValue = this.value = defaultValue;
    this.listeners = Lists.newArrayList(listeners);
  }

  @Override
  public final String getName() {
    return this.name;
  }

  @Override
  public final Class<T> getType() {
    return this.type;
  }

  public final T getDefaultValue() {
    return this.defaultValue;
  }

  public final void addListener(Consumer<T> listener) {
    this.listeners.add(listener);
  }

  /**
   * Get the value, ignoring any animations.
   * 
   * @return the value
   */
  protected final T getDirect() {
    return this.value;
  }

  @Override
  public final T get() {
    return this.animatedValue != null ? this.animatedValue : this.value;
  }

  @Override
  public final void set(T value) {
    this.value = value;
    this.changed(value);
  }

  @Override
  public final boolean isBeingAnimated() {
    return this.animatedValue != null;
  }

  @Override
  public final void setAnimatedValue(@Nullable T animatedValue) {
    this.animatedValue = animatedValue;
    this.changed(this.get());
  }

  private void changed(T value) {
    this.listeners.forEach(listener -> listener.accept(value));
  }

  @Override
  public final String toString() {
    return "Property[" + this.name + "=" + this.value + "]";
  }

  @Override
  public final int hashCode() {
    return this.getName().hashCode();
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof Property<?> that && that.getName().equals(this.getName());
  }
}
