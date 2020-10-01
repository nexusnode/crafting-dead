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
package com.craftingdead.core.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;

public class SimpleButton extends Button {

  private int backgroundColour = 0x333333;

  public SimpleButton(int x, int y, int width, int height, String text, IPressable action) {
    super(x, y, width, height, text, action);
    this.setFGColor(0xE3BE2B);
  }

  public void setBackgroundColour(int backgroundColour) {
    this.backgroundColour = backgroundColour;
  }

  @Override
  public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
    final Minecraft minecraft = Minecraft.getInstance();
    fill(this.x, this.y, this.x + this.width + 1, this.y + this.height + 1, 0x33000000);
    fill(this.x, this.y, this.x + this.width, this.y + this.height,
        this.backgroundColour | MathHelper.ceil(this.alpha * 255.0F) << 24);
    this
        .drawCenteredString(minecraft.fontRenderer, this.getMessage(), this.x + this.width / 2,
            this.y + (this.height - 8) / 2,
            (this.active ? this.isHovered ? this.packedFGColor : 0xFFFFFF : 0xA0A0A0)
                | MathHelper.ceil(this.alpha * 255.0F) << 24);
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
  }

}
