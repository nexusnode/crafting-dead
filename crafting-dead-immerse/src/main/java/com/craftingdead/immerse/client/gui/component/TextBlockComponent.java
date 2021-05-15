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

package com.craftingdead.immerse.client.gui.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.client.gui.component.type.MeasureMode;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;

public class TextBlockComponent extends Component<TextBlockComponent> {

  private ITextComponent text;
  private FontRenderer font;
  private boolean shadow;
  private boolean centered;
  private boolean customWidth = false;
  private List<IReorderingProcessor> lines = new ArrayList<>();

  public TextBlockComponent(ITextComponent text) {
    this.text = text;
    this.font = super.minecraft.font;
    this.shadow = true;
    this.centered = false;
    // Auto adjust width to size of text
    super.setWidth(this.font.width(text));
  }

  public TextBlockComponent(String text) {
    this(Text.of(text));
  }

  public TextBlockComponent setFontRenderer(FontRenderer fontRenderer) {
    this.font = fontRenderer;
    return this;
  }

  public TextBlockComponent setShadow(boolean shadow) {
    this.shadow = shadow;
    return this;
  }

  public TextBlockComponent setCentered(boolean centered) {
    this.centered = centered;
    return this;
  }

  @Override
  public TextBlockComponent setWidth(float width) {
    this.customWidth = true;
    return super.setWidth(width);
  }

  public void changeText(ITextComponent text) {
    this.lines = null;
    this.text = text;
    if (!customWidth) {
      super.setWidth(this.font.width(text));
    }
    this.layout();
  }

  @Override
  public void layout() {
    this.generateLines((int) this.getContentWidth());
    super.layout();
  }

  @Override
  protected Vector2f measure(MeasureMode widthMode, float width, MeasureMode heightMode,
      float height) {
    if (widthMode == MeasureMode.UNDEFINED) {
      width = this.font.width(this.text.getString());
    }

    this.generateLines((int) width);
    return new Vector2f(width, this.lines.size() * this.font.lineHeight);
  }

  private void generateLines(int width) {
    this.lines = this.font.split(this.text, width);
  }

  @Override
  public float computeFullHeight() {
    return this.lines.size() * this.font.lineHeight;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return super.mouseClicked(mouseX, mouseY, button)
        || this.componentStyleAtWidth(mouseX, mouseY)
            .map(this.minecraft.screen::handleComponentClicked)
            .orElse(false);
  }

  @Override
  public void renderContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);

    matrixStack.pushPose();
    {
      matrixStack.translate(this.getScaledContentX(), this.getScaledContentY(), 0.0D);
      matrixStack.scale(this.getXScale(), this.getYScale(), 1.0F);
      IRenderTypeBuffer.Impl renderTypeBuffer =
          IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
      for (int i = 0; i < this.lines.size(); i++) {
        IReorderingProcessor line = this.lines.get(i);
        matrixStack.pushPose();
        {
          matrixStack.translate(0.0D, i * this.font.lineHeight, 0.0D);
          float x = this.centered
              ? (this.getContentWidth() - this.font.width(line)) / 2.0F
              : 0;
          this.font.drawInBatch(line, x, 0, 0xFFFFFF + ((int) (this.getAlpha() * 255.0F) << 24),
              this.shadow, matrixStack.last().pose(), renderTypeBuffer, false, 0,
              RenderUtil.FULL_LIGHT);
        }
        matrixStack.popPose();
      }
      renderTypeBuffer.endBatch();
    }
    matrixStack.popPose();
  }

  public Optional<Style> componentStyleAtWidth(double mouseX, double mouseY) {
    int offsetMouseX = MathHelper.floor((mouseX - this.getContentX()) / this.getXScale());
    int offsetMouseY = MathHelper.floor((mouseY - this.getContentY()) / this.getYScale());
    final int lines = this.lines.size();
    if (offsetMouseX >= 0 && offsetMouseY >= 0 && offsetMouseX <= this.getContentWidth()
        && offsetMouseY < (this.font.lineHeight * lines + lines)) {
      int maxLines = offsetMouseY / this.font.lineHeight;
      if (maxLines >= 0 && maxLines < this.lines.size()) {
        IReorderingProcessor line = this.lines.get(maxLines);
        return Optional.ofNullable(
            this.font.getSplitter().componentStyleAtWidth(line, offsetMouseX));
      }
    }
    return Optional.empty();
  }
}
