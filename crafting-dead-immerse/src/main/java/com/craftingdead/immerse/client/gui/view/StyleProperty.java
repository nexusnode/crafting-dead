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

  public abstract StyleProperty<T> registerState(T value, State... states);

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
