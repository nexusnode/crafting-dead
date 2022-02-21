/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view.property;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public interface ValueAccessor<T> {

  T get();

  void set(T value);

  void reset();

  static <T> ValueAccessor<T> getterSetter(Supplier<T> getter, Consumer<T> setter,  @Nullable T defaultValue) {
    var accessor = new ValueAccessor<T>() {

      @Override
      public T get() {
        return getter.get();
      }

      @Override
      public void set(T value) {
        setter.accept(value);
      }

      @Override
      public void reset() {
        if (defaultValue != null) {
          this.set(defaultValue);
        }
      }
    };
    accessor.reset();
    return accessor;
  }

  static <T> ValueAccessor<T> simple(T defaultValue, @Nullable Consumer<T> listener) {
    var accessor = new ValueAccessor<T>() {

      private T value;

      @Override
      public T get() {
        return this.value;
      }

      @Override
      public void set(T value) {
        this.value = value;
        if (listener != null) {
          listener.accept(value);
        }
      }

      @Override
      public void reset() {
        this.set(defaultValue);
      }
    };
    accessor.reset();
    return accessor;
  }
}
