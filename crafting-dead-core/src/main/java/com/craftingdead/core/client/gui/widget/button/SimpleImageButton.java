/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core.client.gui.widget.button;

import com.craftingdead.core.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;

public class SimpleImageButton extends Button {

  private ResourceLocation textureLocation;

  public SimpleImageButton(int x, int y, int width, int height,
      ResourceLocation textureLocation, String text, Button.IPressable actionListener) {
    super(x, y, width, height, text, actionListener);
    this.textureLocation = textureLocation;
  }

  @Override
  public void renderButton(int mouseX, int mouseY, float partialTicks) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
    RenderUtil.bind(this.textureLocation);
    blit(this.x, this.y, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
    if (this.isHovered()) {
      final int opacity = Math.min((int) (this.alpha * 0.5F * 255.0F), 255);
      fill(this.x, this.y, this.x + this.width, this.y + this.height, 0xFFFFFF + (opacity << 24));
    }
  }
}
