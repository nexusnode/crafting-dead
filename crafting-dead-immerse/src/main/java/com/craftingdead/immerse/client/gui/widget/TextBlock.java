package com.craftingdead.immerse.client.gui.widget;

import com.craftingdead.immerse.client.util.RenderUtil;
import net.minecraft.client.gui.IRenderable;

public class TextBlock implements IRenderable {

  private final int x;
  private final int y;
  private final int width;
  private final int height;

  public TextBlock(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    RenderUtil
        .fillGradient(this.x, this.y, this.x + this.width, this.y + this.height,
            0x80000000, 0x80000000);
    RenderUtil
        .fillGradient(this.x, this.y, this.x + 3, this.y + this.height, 0xFFD8001C,
            0xFFD8001C);
  }
}
