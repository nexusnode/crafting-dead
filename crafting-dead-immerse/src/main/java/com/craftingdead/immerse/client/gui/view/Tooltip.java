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

import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;

public class Tooltip {

  private final ITextComponent text;
  private final ValueStyleProperty<Integer> opacityProperty =
      ValueStyleProperty.create("alpha", Integer.class, 0);
  private final ValueStyleProperty<Integer> textOpacityProperty =
      ValueStyleProperty.create("text-alpha", Integer.class, 0);

  public Tooltip(ITextComponent text) {
    this.text = text;
  }

  public void render(FontRenderer fontRenderer, MatrixStack matrixStack, float x, float y) {
    final float width = 10.0F + fontRenderer.width(this.text);
    final float height = 14;

    RenderSystem.enableBlend();
    RenderUtil.enableRoundedRectShader(x, y, x + width, y + height, 2);
    RenderUtil.fill(matrixStack,
        x, y, x + width, y + height, 0x111111 | (this.opacityProperty.get() << 24));
    RenderUtil.resetShader();

    int shiftedOpacity = this.textOpacityProperty.get() << 24;
    if ((shiftedOpacity & 0xFC000000) != 0) {
      matrixStack.pushPose();
      {
        matrixStack.translate(0.0D, 0.0D, 400.0D);
        fontRenderer.draw(matrixStack, this.text,
            (x + (width - fontRenderer.width(this.text)) / 2.0F), y + 3.5F,
            0xFFFFFF | shiftedOpacity);
      }
      matrixStack.popPose();
    }
    RenderSystem.disableBlend();
  }

  public ValueStyleProperty<Integer> getOpacityProperty() {
    return this.opacityProperty;
  }

  public ValueStyleProperty<Integer> getTextOpacityProperty() {
    return this.textOpacityProperty;
  }
}
