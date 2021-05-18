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

package com.craftingdead.immerse.client.gui.tween;

import java.util.function.BiConsumer;
import java.util.function.Function;
import io.noties.tumbleweed.TweenType;

public class FloatTweenType<T> implements TweenType<T> {

  private final Function<T, Float> getter;
  private final BiConsumer<T, Float> setter;

  public FloatTweenType(Function<T, Float> getter, BiConsumer<T, Float> setter) {
    this.getter = getter;
    this.setter = setter;
  }

  @Override
  public int getValuesSize() {
    return 1;
  }

  @Override
  public void getValues(T t, float[] values) {
    values[0] = this.getter.apply(t);
  }

  @Override
  public void setValues(T t, float[] values) {
    this.setter.accept(t, values[0]);
  }
}
