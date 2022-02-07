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

package com.craftingdead.immerse.client.gui.view.layout.yoga;

import org.lwjgl.util.yoga.Yoga;

public enum YogaPositionType {
  /**
   * The component is positioned relative to its normal position.
   */
  RELATIVE(Yoga.YGPositionTypeRelative),
  /**
   * The component is positioned relative to its parent.
   */
  ABSOLUTE(Yoga.YGPositionTypeAbsolute);

  private final int yogaType;

  private YogaPositionType(int yogaType) {
    this.yogaType = yogaType;
  }

  public int getYogaType() {
    return this.yogaType;
  }
}
