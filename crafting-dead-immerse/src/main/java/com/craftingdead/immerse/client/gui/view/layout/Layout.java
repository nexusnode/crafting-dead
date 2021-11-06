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

import com.craftingdead.immerse.client.gui.view.Overflow;
import net.minecraft.util.math.vector.Vector2f;

public interface Layout {

  Overflow getOverflow();

  float getLeft();

  float getLeftPadding();

  float getLeftBorder();

  float getRight();

  float getRightPadding();

  float getRightBorder();

  float getTop();

  float getTopPadding();

  float getTopBorder();

  float getBottom();

  float getBottomPadding();

  float getBottomBorder();

  float getWidth();

  float getHeight();

  void setMeasureFunction(MeasureFunction measureFunction);

  void layout();

  void close();

  interface MeasureFunction {

    Vector2f measure(MeasureMode widthMode, float width, MeasureMode heightMode, float height);
  }
}
