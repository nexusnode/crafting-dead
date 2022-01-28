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

package com.craftingdead.core.client.gui.widget.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SimpleImageButton extends Button {

  private ResourceLocation textureLocation;

  public SimpleImageButton(int x, int y, int width, int height,
      ResourceLocation textureLocation, Component text, Button.OnPress actionListener) {
    super(x, y, width, height, text, actionListener);
    this.textureLocation = textureLocation;
  }

  @Override
  public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
    RenderSystem.setShaderTexture(0, this.textureLocation);
    blit(matrixStack, this.x, this.y, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
    if (this.isHoveredOrFocused()) {
      final int opacity = Math.min((int) (this.alpha * 0.5F * 255.0F), 255);
      fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height,
          0xFFFFFF + (opacity << 24));
    }
  }
}
