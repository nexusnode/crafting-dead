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

package com.craftingdead.immerse.client.gui.component;

import java.util.Optional;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;

public interface IView extends IGuiEventListener, IRenderable, Comparable<IView> {

  /**
   * Similar to {@link #isMouseOver(double, double)} but is aware of z-offset.
   * 
   * @return {@code false} if mouse is not over the component or if there's another component over
   *         it
   * @see #isMouseOver(double, double)
   */
  boolean isHovered();

  void mouseEntered(double mouseX, double mouseY);

  void mouseLeft(double mouseX, double mouseY);

  default float getScaledContentX() {
    return 0.0F;
  }

  default float getScaledContentY() {
    return 0.0F;
  }

  default float getContentX() {
    return 0.0F;
  }

  default float getContentY() {
    return 0.0F;
  }

  float getWidth();

  float getHeight();

  float getZOffset();

  default float getAlpha() {
    return 1.0F;
  }

  default float getXScale() {
    return 1.0F;
  }

  default float getYScale() {
    return 1.0F;
  }

  Optional<IParentView> getParent();

  @Override
  default int compareTo(IView another) {
    if (another == null) {
      return 1;
    }
    if (this.getZOffset() < another.getZOffset()) {
      return -1;
    } else if (this.getZOffset() > another.getZOffset()) {
      return 1;
    }
    return 0;
  }
}
