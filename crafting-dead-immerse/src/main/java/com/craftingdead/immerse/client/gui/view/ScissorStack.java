package com.craftingdead.immerse.client.gui.view;

import java.util.ArrayDeque;
import java.util.Deque;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.Rectangle2d;

public class ScissorStack {

  private static final Deque<Rectangle2d> regionStack = new ArrayDeque<>();

  public static void push(int x, int y, int width, int height) {
    push(new Rectangle2d(x, y, width, height));
  }

  public static void push(Rectangle2d region) {
    Rectangle2d parentRegion = peek();
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
      Rectangle2d region = regionStack.peek();
      if (region != null) {
        RenderSystem.enableScissor(region.getX(), region.getY(), region.getWidth(),
            region.getHeight());
        return;
      }
    }
    RenderSystem.disableScissor();
  }

  public static Rectangle2d peek() {
    return regionStack.peek();
  }

  public static boolean isEmpty() {
    return regionStack.isEmpty();
  }
}
