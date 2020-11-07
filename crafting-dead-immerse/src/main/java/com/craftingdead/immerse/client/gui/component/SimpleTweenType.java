/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.immerse.client.gui.component;

import java.util.function.BiConsumer;
import java.util.function.Function;
import io.noties.tumbleweed.TweenType;
import net.minecraft.util.math.Vec2f;

public class SimpleTweenType<T> implements TweenType<T> {

  private final int size;
  private final Function<T, float[]> getter;
  private final BiConsumer<T, float[]> setter;

  public SimpleTweenType(Function<T, Float> getter, BiConsumer<T, Float> setter) {
    this(1, t -> new float[] {getter.apply(t)}, (t, v) -> setter.accept(t, v[0]));
  }

  public SimpleTweenType(Vec2fGetter<T> getter, Vec2fSetter<T> setter) {
    this(1, t -> {
      Vec2f v = getter.get(t);
      return new float[] {v.x, v.y};
    }, (t, v) -> setter.set(t, new Vec2f(v[0], v[1])));
  }

  public SimpleTweenType(int size, Function<T, float[]> getter, BiConsumer<T, float[]> setter) {
    this.size = size;
    this.getter = getter;
    this.setter = setter;
  }

  @Override
  public int getValuesSize() {
    return this.size;
  }

  @Override
  public void getValues(T t, float[] values) {
    System.arraycopy(this.getter.apply(t), 0, values, 0, this.size);
  }

  @Override
  public void setValues(T t, float[] values) {
    this.setter.accept(t, values);
  }

  @FunctionalInterface
  public static interface Vec2fGetter<T> {
    Vec2f get(T t);
  }

  @FunctionalInterface
  public static interface Vec2fSetter<T> {
    void set(T t, Vec2f v);
  }
}
