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

package com.craftingdead.core.util;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.google.common.base.Suppliers;

public class FunctionalUtil {

  public static <T> Optional<T> optional(@Nullable Supplier<T> supplier) {
    return Optional.ofNullable(supplier).map(Supplier::get);
  }

  // Java is not that smart with implicit casting the google commons supplier to java util supplier
  public static <T> Supplier<T> supplier(@NotNull T instance) {
    return Suppliers.ofInstance(instance);
  }

  public static <T> Supplier<T> supply(T value) {
    return () -> value;
  }

  public static <T, U, R> BiFunction<@Nullable T, U, R> nullsafeFunction(
      BiFunction<@NotNull T, U, R> original, Function<U, R> fallback) {
    return (arg1, arg2) -> {
      if (arg1 == null) {
        return fallback.apply(arg2);
      } else {
        return original.apply(arg1, arg2);
      }
    };
  }
}
