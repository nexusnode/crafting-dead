package com.craftingdead.immerse.client.gui.view;

import java.util.ArrayDeque;
import java.util.Deque;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.Rectangle2d;

public class ScissorStack {

  private final Deque<Rectangle2d> regionStack = new ArrayDeque<>();

  public void push(int x, int y, int width, int height) {
    this.push(new Rectangle2d(x, y, width, height));
  }

  public void push(Rectangle2d region) {
    Rectangle2d parentRegion = this.peek();
    this.regionStack.push(region);
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

  public void pop() {
    if (!this.regionStack.isEmpty()) {
      this.regionStack.pop();
      Rectangle2d region = this.regionStack.peek();
      if (region != null) {
        RenderSystem.enableScissor(region.getX(), region.getY(), region.getWidth(),
            region.getHeight());
        return;
      }
    }
    RenderSystem.disableScissor();
  }

  public Rectangle2d peek() {
    return this.regionStack.peek();
  }

  public boolean isEmpty() {
    return this.regionStack.isEmpty();
  }
}
