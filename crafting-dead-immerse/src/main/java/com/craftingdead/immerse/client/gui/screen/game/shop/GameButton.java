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

package com.craftingdead.immerse.client.gui.screen.game.shop;

import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class GameButton extends Button {

  protected final Font font = Minecraft.getInstance().font;

  public GameButton(int x, int y, int width, int height, Component title,
      OnPress pressedAction) {
    super(x, y, width, height, title, pressedAction);
  }

  @Override
  public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fillWidthHeight(matrixStack, this.x, this.y, this.width, this.height,
        ChatFormatting.DARK_RED.getColor() + ((this.isHoveredOrFocused() ? 255 : 128) << 24));

    this.font.drawShadow(matrixStack, this.getMessage(),
        this.x + this.getWidth() / 2 - this.font.width(getMessage()) / 2,
        this.y + this.getHeight() / 2 - this.font.lineHeight / 2,
        0xFFFFFFFF);

    if (this.isHoveredOrFocused()) {
      final int size = 1;
      final int color = 0xFFFFBA00;
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      RenderUtil.fillWidthHeight(matrixStack, this.x - size, this.y, size, this.height, color);
      RenderUtil.fillWidthHeight(matrixStack, this.x + this.width, this.y, size, this.height,
          color);
      RenderUtil.fillWidthHeight(matrixStack, this.x - size, this.y - size, this.width + (size * 2),
          size, color);
      RenderUtil.fillWidthHeight(matrixStack, this.x - size, this.y + this.height,
          this.width + (size * 2), size, color);
    }
  }
}
