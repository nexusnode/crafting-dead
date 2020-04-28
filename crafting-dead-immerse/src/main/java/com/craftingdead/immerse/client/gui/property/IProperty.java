package com.craftingdead.immerse.client.gui.property;

import java.util.Optional;
import java.util.function.Supplier;

public interface IProperty<T> extends Supplier<T> {

  void set(T value);

  default Optional<T> getOptional() {
    return Optional.ofNullable(this.get());
  }
}
