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

public interface EmptyLayout extends Layout {

  @Override
  default float getLeft() {
    return 0;
  }

  @Override
  default float getLeftPadding() {
    return 0;
  }

  @Override
  default float getRight() {
    return 0;
  }

  @Override
  default float getRightPadding() {
    return 0;
  }

  @Override
  default float getTop() {
    return 0;
  }

  @Override
  default float getTopPadding() {
    return 0;
  }

  @Override
  default float getBottom() {
    return 0;
  }

  @Override
  default float getBottomPadding() {
    return 0;
  }

  @Override
  default void setMeasureFunction(MeasureFunction measureFunction) {}

  @Override
  default void layout() {}

  @Override
  default void close() {}
}
