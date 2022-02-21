/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

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
package com.craftingdead.immerse.client.util;

import net.minecraft.world.phys.Vec2;

public enum FitType {
  FILL, COVER {
    @Override
    public Vec2 getSize(float contentWidth, float contentHeight, float containerWidth,
        float containerHeight) {
      float widthScale = containerWidth / contentWidth;
      float heightScale = containerHeight / contentHeight;
      float finalScale = contentHeight * widthScale < containerHeight ? heightScale : widthScale;
      return new Vec2(contentWidth * finalScale, contentHeight * finalScale);
    }
  },
  CONTAIN {
    @Override
    public Vec2 getSize(float contentWidth, float contentHeight, float containerWidth,
        float containerHeight) {
      float widthScale = containerWidth / contentWidth;
      float heightScale = containerHeight / contentHeight;
      float finalScale = contentHeight * widthScale > containerHeight ? heightScale : widthScale;
      if (finalScale * contentWidth > containerWidth) {
        contentWidth = finalScale * contentWidth;
        contentHeight = finalScale * contentHeight;
        finalScale = containerWidth / (contentWidth);
      }
      return new Vec2(contentWidth * finalScale, contentHeight * finalScale);
    }
  },
  NONE {
    @Override
    public Vec2 getSize(float contentWidth, float contentHeight, float containerWidth,
        float containerHeight) {
      return new Vec2(contentWidth, contentHeight);
    }
  };

  public Vec2 getSize(float contentWidth, float contentHeight, float containerWidth,
      float containerHeight) {
    return new Vec2(containerWidth, containerHeight);
  }
}
