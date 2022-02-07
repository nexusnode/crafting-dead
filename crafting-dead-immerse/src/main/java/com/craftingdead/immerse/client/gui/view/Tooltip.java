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
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class Tooltip {

  private final Component text;
  private final ValueProperty<Integer> opacity =
      StyleableProperty.create("alpha", Integer.class, 0);
  private final ValueProperty<Integer> textOpacity =
      StyleableProperty.create("text-alpha", Integer.class, 0);

  public Tooltip(Component text) {
    this.text = text;
  }

  public void render(Font font, PoseStack poseStack, float x, float y) {
    final float width = 10.0F + font.width(this.text);
    final float height = 14;

    RenderSystem.enableBlend();
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fill(poseStack, x, y, x + width, y + height, 0x222222 | (this.opacity.get() << 24));

    int shiftedOpacity = this.textOpacity.get() << 24;
    if ((shiftedOpacity & 0xFC000000) != 0) {
      font.draw(poseStack, this.text,
          (x + (width - font.width(this.text)) / 2.0F), y + 3.5F,
          0xFFFFFF | shiftedOpacity);
    }
    RenderSystem.disableBlend();

  }

  public ValueProperty<Integer> getOpacityProperty() {
    return this.opacity;
  }

  public ValueProperty<Integer> getTextOpacityProperty() {
    return this.textOpacity;
  }
}
