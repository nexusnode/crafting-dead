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

import java.util.function.Function;
import com.craftingdead.immerse.client.gui.view.Colour;
import io.noties.tumbleweed.TweenType;

public class ColourTweenType<T> implements TweenType<T> {

  private final Function<T, Colour> getter;

  public ColourTweenType(Function<T, Colour> getter) {
    this.getter = getter;
  }

  @Override
  public int getValuesSize() {
    return 4;
  }

  @Override
  public void getValues(T t, float[] values) {
    System.arraycopy(this.getter.apply(t).getColour4f(), 0, values, 0, 4);
  }

  @Override
  public void setValues(T t, float[] values) {
    this.getter.apply(t).setColour4f(values);
  }
}
