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
import net.minecraft.util.math.vector.Vector2f;

public class Vector2fTweenType<T> implements TweenType<T> {

  private final Function<T, Vector2f> getter;
  private final BiConsumer<T, Vector2f> setter;

  public Vector2fTweenType(Function<T, Vector2f> getter, BiConsumer<T, Vector2f> setter) {
    this.getter = getter;
    this.setter = setter;
  }

  @Override
  public int getValuesSize() {
    return 3;
  }

  @Override
  public void getValues(T t, float[] values) {
    Vector2f vec = this.getter.apply(t);
    values[0] = vec.x;
    values[1] = vec.y;
  }

  @Override
  public void setValues(T t, float[] values) {
    this.setter.accept(t, new Vector2f(values[0], values[1]));
  }
}
