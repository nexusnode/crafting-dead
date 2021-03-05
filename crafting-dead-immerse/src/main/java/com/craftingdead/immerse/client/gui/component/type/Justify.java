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
package com.craftingdead.immerse.client.gui.component.type;

import org.lwjgl.util.yoga.Yoga;

public enum Justify {
  FLEX_START(Yoga.YGJustifyFlexStart), CENTER(Yoga.YGJustifyCenter), FLEX_END(
      Yoga.YGJustifyFlexEnd), SPACE_BETWEEN(Yoga.YGJustifySpaceBetween), SPACE_AROUND(
          Yoga.YGJustifySpaceAround), SPACE_EVENLY(Yoga.YGJustifySpaceEvenly);

  private final int yogaType;

  private Justify(int yogaType) {
    this.yogaType = yogaType;
  }

  public int getYogaType() {
    return this.yogaType;
  }
}
