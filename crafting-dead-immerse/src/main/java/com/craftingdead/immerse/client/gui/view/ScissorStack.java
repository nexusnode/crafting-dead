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

package com.craftingdead.immerse.client.gui.view;

import java.util.ArrayDeque;
import java.util.Deque;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.Rect2i;

public class ScissorStack {

  private static final Deque<Rect2i> regionStack = new ArrayDeque<>();

  public static void push(int x, int y, int width, int height) {
    push(new Rect2i(x, y, width, height));
  }

  public static void push(Rect2i region) {
    Rect2i parentRegion = peek();
    regionStack.push(region);
    if (parentRegion == null) {
      RenderSystem.enableScissor(region.getX(), region.getY(), region.getWidth(),
          region.getHeight());
    } else {
      int x = Math.min(region.getX(), parentRegion.getX());
      int y = Math.max(region.getY(), parentRegion.getY());
      int x2 = Math.min(region.getX() + region.getWidth(),
          parentRegion.getX() + parentRegion.getWidth());
      int y2 = Math.min(region.getY() + region.getHeight(),
          parentRegion.getY() + parentRegion.getHeight());
      RenderSystem.enableScissor(x, y, Math.max(x2 - x, 0), Math.max(y2 - y, 0));
    }
  }

  public static void pop() {
    if (!regionStack.isEmpty()) {
      regionStack.pop();
      Rect2i region = regionStack.peek();
      if (region != null) {
        RenderSystem.enableScissor(region.getX(), region.getY(), region.getWidth(),
            region.getHeight());
        return;
      }
    }
    RenderSystem.disableScissor();
  }

  public static Rect2i peek() {
    return regionStack.peek();
  }

  public static boolean isEmpty() {
    return regionStack.isEmpty();
  }
}
