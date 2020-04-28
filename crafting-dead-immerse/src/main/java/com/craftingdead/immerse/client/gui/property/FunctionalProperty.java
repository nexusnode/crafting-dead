package com.craftingdead.immerse.client.gui.property;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FunctionalProperty<T> implements IProperty<T> {

  private final Supplier<T> getter;
  private final Consumer<T> setter;

  public FunctionalProperty(Supplier<T> getter, Consumer<T> setter) {
    this.getter = getter;
    this.setter = setter;
  }

  @Override
  public T get() {
    return this.getter.get();
  }

  @Override
  public void set(T value) {
    this.setter.accept(value);
  }
}
