package com.craftingdead.immerse.client.gui.property;

public class Property<T> implements IProperty<T> {

  private T value;

  public Property(T value) {
    this.value = value;
  }

  @Override
  public T get() {
    return this.value;
  }

  @Override
  public void set(T value) {
    this.value = value;
  }
}
