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

package com.craftingdead.immerse.client.gui.view.layout;

import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.view.Overflow;
import net.minecraft.world.phys.Vec2;

public interface Layout {

  default Overflow getOverflow() {
    return Overflow.VISIBLE;
  }

  default float getLeft() {
    return 0;
  }

  default float getLeftPadding() {
    return 0;
  }

  default float getLeftBorder() {
    return 0;
  }

  default float getRight() {
    return 0;
  }

  default float getRightPadding() {
    return 0;
  }

  default float getRightBorder() {
    return 0;
  }

  default float getTop() {
    return 0;
  }

  default float getTopPadding() {
    return 0;
  }

  default float getTopBorder() {
    return 0;
  }

  default float getBottom() {
    return 0;
  }

  default float getBottomPadding() {
    return 0;
  }

  default float getBottomBorder() {
    return 0;
  }

  float getWidth();

  float getHeight();

  default void setMeasureFunction(@Nullable MeasureFunction measureFunction) {}

  default void markDirty() {}

  default void close() {}

  interface MeasureFunction {

    Vec2 measure(MeasureMode widthMode, float width, MeasureMode heightMode, float height);
  }
}
