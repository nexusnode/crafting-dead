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

package com.craftingdead.immerse.client.gui.screen.game;

import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class GameButton extends Button {

  protected final FontRenderer font = Minecraft.getInstance().font;

  public GameButton(int x, int y, int width, int height, ITextComponent title,
      IPressable pressedAction) {
    super(x, y, width, height, title, pressedAction);
  }

  @Override
  public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    RenderUtil.fill2(this.x, this.y, this.width, this.height,
        TextFormatting.DARK_RED.getColor() + ((this.isHovered() ? 255 : 128) << 24));

    this.font.drawShadow(matrixStack, this.getMessage(),
        this.x + this.getWidth() / 2 - this.font.width(getMessage()) / 2,
        this.y + this.getHeight() / 2 - this.font.lineHeight / 2,
        0xFFFFFFFF);

    if (this.isHovered()) {
      final int size = 1;
      final int color = 0xFFFFBA00;
      RenderUtil.fill2(this.x - size, this.y, size, this.height, color);
      RenderUtil.fill2(this.x + this.width, this.y, size, this.height, color);
      RenderUtil.fill2(this.x - size, this.y - size, this.width + (size * 2), size, color);
      RenderUtil.fill2(this.x - size, this.y + this.height, this.width + (size * 2), size, color);
    }
  }
}
