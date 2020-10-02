/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.immerse.client.gui.component;

import java.util.Optional;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.util.text.ITextComponent;

public class LabelComponent extends Component<LabelComponent> {

  private final FontRenderer fontRenderer;

  private final ITextComponent text;

  private final boolean shadow;

  private Colour colour;

  public LabelComponent(FontRenderer fontRenderer, ITextComponent text, Colour colour,
      boolean shadow) {
    this.fontRenderer = fontRenderer;
    this.text = text;
    this.colour = colour;
    this.shadow = shadow;
  }

  @Override
  public Optional<Double> getBestWidth() {
    return Optional.of((double) this.fontRenderer.getStringWidth(this.text.getFormattedText()));
  }

  @Override
  public Optional<Double> getBestHeight() {
    return Optional.of((double) this.fontRenderer.FONT_HEIGHT);
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    RenderSystem.pushMatrix();
    {
      RenderSystem.translated(this.getX(), this.getY(), 0.0D);
      RenderSystem.scalef(this.getXScale(), this.getYScale(), 1.0F);
      RenderSystem.enableAlphaTest();
      IRenderTypeBuffer.Impl renderTypeBuffer =
          IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
      this.fontRenderer
          .renderString(this.text.getFormattedText(), 0, 0, this.colour.getHexColour(), this.shadow,
              TransformationMatrix.identity().getMatrix(), renderTypeBuffer, false, 0, 0xF000F0);
      renderTypeBuffer.finish();
      RenderSystem.disableAlphaTest();
    }
    RenderSystem.popMatrix();
  }
  
  @Override
  protected boolean overrideSize() {
    return true;
  }
}
