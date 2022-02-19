/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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
