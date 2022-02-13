package com.craftingdead.immerse.client.gui.view.property;

public interface Property<T> {

  /**
   * Retrieve this property's value.
   * 
   * @return the value
   */
  T get();

  /**
   * Set this property's value.
   * 
   * @param value to set
   */
  void set(T value);
}
