/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view;

import java.util.ArrayDeque;
import java.util.Deque;
import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.Rect2i;

public class ScissorStack {

  private static final Deque<Rect2i> regionStack = new ArrayDeque<>();

  public static void push(int x, int y, int width, int height) {
    push(new Rect2i(x, y, width, height));
  }

  public static void push(Rect2i rect) {
    var parentRect = peek();
    regionStack.push(rect);
    if (parentRect == null) {
      apply(rect);
      return;
    }

    int x = Math.min(rect.getX(), parentRect.getX());
    int y = Math.max(rect.getY(), parentRect.getY());
    int x2 = Math.min(rect.getX() + rect.getWidth(),
        parentRect.getX() + parentRect.getWidth());
    int y2 = Math.min(rect.getY() + rect.getHeight(),
        parentRect.getY() + parentRect.getHeight());
    RenderSystem.enableScissor(x, y, Math.max(x2 - x, 0), Math.max(y2 - y, 0));
  }

  public static void pop() {
    if (!regionStack.isEmpty()) {
      regionStack.pop();
      if (apply()) {
        return;
      }
    }
    RenderSystem.disableScissor();
  }

  private static void apply(Rect2i rect) {
    RenderSystem.enableScissor(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
  }

  public static boolean apply() {
    var rect = peek();
    if (rect == null) {
      return false;
    }
    apply(rect);
    return true;
  }

  @Nullable
  public static Rect2i peek() {
    return regionStack.peek();
  }

  public static boolean isEmpty() {
    return regionStack.isEmpty();
  }
}
