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

package com.craftingdead.immerse.client.gui.component.type;

import org.lwjgl.util.yoga.Yoga;

public enum Overflow {
  VISIBLE(Yoga.YGOverflowVisible), HIDDEN(Yoga.YGOverflowHidden), SCROLL(Yoga.YGOverflowScroll);

  private final int yogaType;

  private Overflow(int yogaType) {
    this.yogaType = yogaType;
  }

  public int getYogaType() {
    return this.yogaType;
  }

  public static Overflow fromYogaType(int yogaType) {
    for (Overflow overflow : values()) {
      if (overflow.yogaType == yogaType) {
        return overflow;
      }
    }
    throw new IllegalArgumentException("Invalid yoga type");
  }
}
