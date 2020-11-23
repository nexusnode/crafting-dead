/**
 * Crafting Dead Copyright (C) 2020 Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.immerse.client.gui.component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.RenderComponentsUtil;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeHooks;

public class TextBlockComponent extends Component<TextBlockComponent> {

  private final FontRenderer fontRenderer;

  private final ITextComponent text;

  private final boolean shadow;

  private List<ITextComponent> lines;

  public TextBlockComponent(FontRenderer fontRenderer, ITextComponent text, boolean shadow) {
    this.fontRenderer = fontRenderer;
    this.text = text;
    this.shadow = shadow;
  }

  @Override
  public void layout() {
    super.layout();
    if (this.lines == null) {
      this.generateLines((int) this.getContentWidth());
    }
  }

  @Override
  protected Vec2f measure(MeasureMode widthMode, float width, MeasureMode heightMode,
      float height) {
    if (widthMode == MeasureMode.UNDEFINED) {
      width = this.fontRenderer.getStringWidth(this.text.getString());
    }

    this.generateLines((int) width);
    return new Vec2f(width, this.lines.size() * this.fontRenderer.FONT_HEIGHT);
  }

  private void generateLines(int width) {
    this.lines = RenderComponentsUtil.splitText(this.text, width, this.fontRenderer, true, false)
        .stream()
        .map(line -> ForgeHooks.newChatWithLinks(line.getString()).setStyle(line.getStyle()))
        .collect(Collectors.toList());
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return this.getMouseOverText(mouseX, mouseY).map(this.getScreen()::handleComponentClicked)
        .orElse(false) || super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    MatrixStack matrixStack = new MatrixStack();
    matrixStack.translate(this.getScaledContentX(), this.getScaledContentY(), 0.0D);
    matrixStack.scale(this.getXScale(), this.getYScale(), 1.0F);
    RenderSystem.pushMatrix();
    {
      RenderSystem.enableAlphaTest();
      IRenderTypeBuffer.Impl renderTypeBuffer =
          IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
      for (int i = 0; i < this.lines.size(); i++) {
        ITextComponent line = this.lines.get(i);
        matrixStack.push();
        {
          matrixStack.translate(0.0D, i * this.fontRenderer.FONT_HEIGHT, 0.0D);
          this.fontRenderer.renderString(line.getFormattedText(), 0, 0, 0xFFFFFFFF,
              this.shadow, matrixStack.getLast().getMatrix(), renderTypeBuffer, false, 0,
              0xF000F0);
        }
        matrixStack.pop();
      }
      renderTypeBuffer.finish();
      RenderSystem.disableAlphaTest();
    }
    RenderSystem.popMatrix();
  }

  public Optional<ITextComponent> getMouseOverText(double mouseX, double mouseY) {
    int offsetMouseX = MathHelper.floor(mouseX - this.getScaledContentX());
    int offsetMouseY = MathHelper.floor(mouseY - this.getScaledContentY());
    if (offsetMouseX >= 0 && offsetMouseY >= 0) {
      final int lines = this.lines.size();
      if (offsetMouseX <= this.getScaledContentWidth()
          && offsetMouseY < this.fontRenderer.FONT_HEIGHT * lines + lines) {
        int maxLines = offsetMouseY / this.fontRenderer.FONT_HEIGHT;
        if (maxLines >= 0 && maxLines < this.lines.size()) {
          ITextComponent line = this.lines.get(maxLines);
          int widthTotal = 0;
          for (ITextComponent child : line) {
            if (child instanceof StringTextComponent) {
              widthTotal += this.minecraft.fontRenderer.getStringWidth(child.getFormattedText());
              if (widthTotal > offsetMouseX) {
                return Optional.of(child);
              }
            }
          }
        }
      }
    }
    return Optional.empty();
  }
}
