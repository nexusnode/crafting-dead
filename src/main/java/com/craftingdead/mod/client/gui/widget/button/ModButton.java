package com.craftingdead.mod.client.gui.widget.button;

import java.awt.Color;
import com.craftingdead.mod.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class ModButton extends Button {

  private static final int HOVER_COLOUR = 0xc2151c;
  private static final int COLOUR = 0xE51C23;
  private static final int SHADOW_COLOR = 0xA01318;

  private long fadeStartTime;

  private boolean wasHovered;

  public ModButton(int x, int y, int width, int height, String text, IPressable onPress) {
    super(x, y, width, height, text, onPress);
  }

  @Override
  public void renderButton(int mouseX, int mouseY, float partialTicks) {
    if (this.fadeStartTime == 0L || (this.wasHovered != this.isHovered)) {
      this.fadeStartTime = Util.milliTime();
    }
    this.wasHovered = this.isHovered;

    float fadePct =
        MathHelper.clamp(((Util.milliTime() - this.fadeStartTime) / 1000.0F) * 3F, 0.0F, 1.0F);

    int i = this.isHovered ? COLOUR : HOVER_COLOUR;
    Color colour1 = new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF,
        MathHelper.ceil(this.alpha * 255.0F));
    i = this.isHovered ? HOVER_COLOUR : COLOUR;
    Color colour2 = new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF,
        MathHelper.ceil(this.alpha * 255.0F));
    Color colour = RenderUtil.lerp(colour1, colour2, fadePct);

    int borderHeight = 2;
    RenderUtil
        .drawGradientRectangle(this.x, this.y, this.x + this.width,
            this.y + this.height - borderHeight, colour.getRGB(), colour.getRGB());
    RenderUtil
        .drawGradientRectangle(this.x, this.y + this.height - borderHeight, this.x + this.width,
            this.y + this.height, SHADOW_COLOR | MathHelper.ceil(this.alpha * 255.0F) << 24,
            SHADOW_COLOR | MathHelper.ceil(this.alpha * 255.0F) << 24);


    RenderSystem.enableBlend();

    this
        .drawCenteredString(Minecraft.getInstance().fontRenderer, this.getMessage(),
            this.x + this.width / 2, this.y + (this.height - 8) / 2,
            0xFFFFFF | MathHelper.ceil(this.alpha * 255.0F) << 24);
    RenderSystem.disableBlend();
  }
}
