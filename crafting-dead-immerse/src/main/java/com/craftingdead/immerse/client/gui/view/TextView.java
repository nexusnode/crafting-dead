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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.MeasureMode;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Style;

public class TextView<L extends Layout> extends View<TextView<L>, L> {

  private Component text;
  private Font font;
  private boolean shadow;
  private boolean centered;

  private List<FormattedCharSequence> lines = new ArrayList<>();

  public TextView(L layout, Component text) {
    super(layout);
    this.text = text;
    this.font = super.minecraft.font;
    this.shadow = true;
    this.centered = false;
  }

  public TextView(L layout, String text) {
    this(layout, new TextComponent(text));
  }

  public TextView<L> setFontRenderer(Font fontRenderer) {
    this.font = fontRenderer;
    return this;
  }

  public TextView<L> setShadow(boolean shadow) {
    this.shadow = shadow;
    return this;
  }

  public TextView<L> setCentered(boolean centered) {
    this.centered = centered;
    return this;
  }

  public TextView<L> setText(Component text) {
    this.text = text;
    this.generateLines(this.getContentWidth());
    return this;
  }

  @Override
  public void layout() {
    this.generateLines(this.getContentWidth());
    super.layout();
  }

  @Override
  protected Vec2 measure(MeasureMode widthMode, float width, MeasureMode heightMode,
      float height) {
    if (widthMode == MeasureMode.UNDEFINED) {
      width = this.font.width(this.text.getString());
    }

    this.generateLines(width);
    return new Vec2(width, this.lines.size() * this.font.lineHeight);
  }

  private void generateLines(float width) {
    this.lines = this.font.split(this.text, Mth.ceil(width));
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
  public void renderContent(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    int opacity = Mth.ceil(this.getAlpha() * 255.0F) << 24;
    if ((opacity & 0xFC000000) == 0) {
      return;
    }

    matrixStack.pushPose();
    {
      matrixStack.translate(this.getScaledContentX(),
          this.getScaledContentY() + (this.centered
              ? (this.getContentHeight() - this.font.lineHeight * this.lines.size()) / 2.0D + 0.5D
              : 0.0D),
          51.0D);
      matrixStack.scale(this.getXScale(), this.getYScale(), 1.0F);
      MultiBufferSource.BufferSource renderTypeBuffer =
          MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
      for (int i = 0; i < this.lines.size(); i++) {
        FormattedCharSequence line = this.lines.get(i);
        matrixStack.pushPose();
        {
          matrixStack.translate(0.0D, i * this.font.lineHeight, 0.0D);
          float x = this.centered
              ? (this.getContentWidth() - this.font.width(line)) / 2.0F
              : 0;
          this.font.drawInBatch(line, x, 0.0F,
              0xFFFFFF | opacity,
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
    int offsetMouseX = Mth.floor((mouseX - this.getContentX()) / this.getXScale());
    int offsetMouseY = Mth.floor((mouseY - this.getContentY()) / this.getYScale());
    final int lines = this.lines.size();
    if (offsetMouseX >= 0 && offsetMouseY >= 0 && offsetMouseX <= this.getContentWidth()
        && offsetMouseY < (this.font.lineHeight * lines + lines)) {
      int maxLines = offsetMouseY / this.font.lineHeight;
      if (maxLines >= 0 && maxLines < this.lines.size()) {
        FormattedCharSequence line = this.lines.get(maxLines);
        return Optional.ofNullable(
            this.font.getSplitter().componentStyleAtWidth(line, offsetMouseX));
      }
    }
    return Optional.empty();
  }
}
