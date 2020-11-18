/**
 * Crafting Dead Copyright (C) 2020 Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.immerse.client.gui.component;

import net.minecraft.util.math.Vec2f;

public enum FitType {
  FILL, COVER {
    @Override
    public Vec2f getSize(float contentWidth, float contentHeight, float containerWidth,
        float containerHeight) {
      float widthScale = containerWidth / contentWidth;
      float heightScale = containerHeight / contentHeight;
      float finalScale = contentHeight * widthScale < containerHeight ? heightScale : widthScale;
      return new Vec2f(contentWidth * finalScale, contentHeight * finalScale);
    }
  },
  CONTAIN {
    @Override
    public Vec2f getSize(float contentWidth, float contentHeight, float containerWidth,
        float containerHeight) {
      float widthScale = containerWidth / contentWidth;
      float heightScale = containerHeight / contentHeight;
      float finalScale = contentHeight * widthScale > containerHeight ? heightScale : widthScale;
      if (finalScale * contentWidth > containerWidth) {
        contentWidth = finalScale * contentWidth;
        contentHeight = finalScale * contentHeight;
        finalScale = containerWidth / (contentWidth);
      }
      return new Vec2f(contentWidth * finalScale, contentHeight * finalScale);
    }
  },
  NONE {
    @Override
    public Vec2f getSize(float contentWidth, float contentHeight, float containerWidth,
        float containerHeight) {
      return new Vec2f(contentWidth, contentHeight);
    }
  };

  public Vec2f getSize(float contentWidth, float contentHeight, float containerWidth,
      float containerHeight) {
    return new Vec2f(containerWidth, containerHeight);
  }
}
