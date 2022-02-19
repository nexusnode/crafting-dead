package com.craftingdead.core;

import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public class FunctionalUtil {

  public static <T> Optional<T> optional(@Nullable Supplier<T> supplier) {
    return Optional.ofNullable(supplier).map(Supplier::get);
  }

  public static <T> Supplier<T> supply(T value) {
    return () -> value;
  }
}
