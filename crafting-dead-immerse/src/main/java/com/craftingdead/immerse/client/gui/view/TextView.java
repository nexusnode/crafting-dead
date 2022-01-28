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
import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.MeasureMode;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class TextView<L extends Layout> extends View<TextView<L>, L> {

  private static final Component ELLIPSIS = new TextComponent("...");

  private final ValueStyleProperty<Color> colorProperty =
      Util.make(ValueStyleProperty.create("color", Color.class, Color.WHITE),
          this::registerValueProperty);

  private Component text = TextComponent.EMPTY;
  private Font font;
  private boolean shadow;
  private boolean centered;
  private boolean wrap = true;

  private List<FormattedCharSequence> lines = new ArrayList<>();

  public TextView(L layout) {
    super(layout);
    this.font = this.minecraft.font;
    this.shadow = true;
    this.centered = false;

    this.layout.setMeasureFunction(this::measure);
  }

  public ValueStyleProperty<Color> getColorProperty() {
    return this.colorProperty;
  }

  public TextView<L> setWrap(boolean wrap) {
    this.wrap = wrap;
    if (this.isAdded()) {
      this.layout.markDirty();
      this.parent.layout();
    }
    return this;
  }

  public TextView<L> setFontRenderer(Font fontRenderer) {
    this.font = fontRenderer;
    if (this.isAdded()) {
      this.layout.markDirty();
      this.parent.layout();
    }
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

  public TextView<L> setText(@Nullable String text) {
    return this.setText(Component.nullToEmpty(text));
  }

  public TextView<L> setText(Component text) {
    this.text = text;
    if (this.isAdded()) {
      this.layout.markDirty();
      this.parent.layout();
    }
    return this;
  }

  @Override
  public void layout() {
    super.layout();
    this.generateLines(this.getContentWidth());
  }

  private Vec2 measure(MeasureMode widthMode, float width, MeasureMode heightMode, float height) {
    var actualWidth = this.font.width(this.text);
    switch (widthMode) {
      case UNDEFINED:
        width = actualWidth;
        break;
      case AT_MOST:
        width = Math.min(actualWidth, width);
        break;
      default:
        break;
    }
    this.generateLines(width);
    return new Vec2(width, this.lines.size() * this.font.lineHeight);
  }

  private void generateLines(float width) {
    int ceilWidth = Mth.floor(width);
    if (this.wrap) {
      this.lines = this.font.split(this.text, ceilWidth);
    } else {
      int textWidth = this.font.width(this.text);
      FormattedText finalText;
      if (textWidth > ceilWidth) {
        int ellipsisWidth = this.font.width(ELLIPSIS);
        finalText = FormattedText.composite(
            this.font.substrByWidth(this.text, ceilWidth - ellipsisWidth), ELLIPSIS);
      } else {
        finalText = this.font.substrByWidth(this.text, ceilWidth);
      }
      this.lines = List.of(ClientLanguage.getInstance().getVisualOrder(finalText));
    }
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
  public void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(poseStack, mouseX, mouseY, partialTicks);

    final var color4i = this.colorProperty.get().getValue4i();

    final int opacity = Mth.ceil(this.getAlpha() * color4i[3]) << 24;
    if ((opacity & 0xFC000000) == 0) {
      return;
    }

    int color = opacity
        | ((color4i[0] & 0xFF) << 16)
        | ((color4i[1] & 0xFF) << 8)
        | ((color4i[2] & 0xFF) << 0);


    poseStack.pushPose();
    {
      poseStack.translate(this.getScaledContentX(),
          this.getScaledContentY() + (this.centered
              ? (this.getContentHeight() - this.font.lineHeight * this.lines.size()) / 2.0D + 0.5D
              : 0.0D),
          51.0D);
      poseStack.scale(this.getXScale(), this.getYScale(), 1.0F);
      var bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
      for (int i = 0; i < this.lines.size(); i++) {
        var line = this.lines.get(i);
        poseStack.pushPose();
        {
          poseStack.translate(0.0D, i * this.font.lineHeight, 0.0D);
          float x = this.centered
              ? (this.getContentWidth() - this.font.width(line)) / 2.0F
              : 0;
          this.font.drawInBatch(line, x, 0.0F, color, this.shadow, poseStack.last().pose(),
              bufferSource, false, 0, com.craftingdead.core.client.util.RenderUtil.FULL_LIGHT);
        }
        poseStack.popPose();
      }

      bufferSource.endBatch();
    }
    poseStack.popPose();
  }

  public Optional<Style> componentStyleAtWidth(double mouseX, double mouseY) {
    final var offsetMouseX = Mth.floor((mouseX - this.getContentX()) / this.getXScale());
    final var offsetMouseY = Mth.floor((mouseY - this.getContentY()) / this.getYScale());
    final var lines = this.lines.size();
    if (offsetMouseX >= 0 && offsetMouseY >= 0 && offsetMouseX <= this.getContentWidth()
        && offsetMouseY < (this.font.lineHeight * lines + lines)) {
      int maxLines = offsetMouseY / this.font.lineHeight;
      if (maxLines >= 0 && maxLines < this.lines.size()) {
        var line = this.lines.get(maxLines);
        return Optional.ofNullable(
            this.font.getSplitter().componentStyleAtWidth(line, offsetMouseX));
      }
    }
    return Optional.empty();
  }
}
