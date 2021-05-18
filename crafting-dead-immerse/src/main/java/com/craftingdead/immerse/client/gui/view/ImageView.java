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

import java.util.Optional;
import com.craftingdead.immerse.client.gui.tween.FloatArrayTweenType;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.MeasureMode;
import org.lwjgl.opengl.GL11;
import com.craftingdead.immerse.client.util.FitType;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.noties.tumbleweed.TweenType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;

public class ImageView<L extends Layout> extends View<ImageView<L>, L> {

  public static final TweenType<ImageView<?>> COLOUR =
      new FloatArrayTweenType<>(4, t -> t.colour.getColour4f(), (t, v) -> t.colour.setColour4f(v));

  private ResourceLocation image;
  private FitType fitType = FitType.FILL;
  private boolean depthTest = false;
  private boolean bilinearFiltering = false;
  private Colour colour = new Colour();

  private Vector2f fittedImageSize;

  public ImageView(L layout) {
    super(layout);
  }

  public ImageView<L> setImage(ResourceLocation image) {
    this.image = image;
    return this;
  }

  public ImageView<L> setFitType(FitType fitType) {
    this.fitType = fitType;
    return this;
  }

  public ImageView<L> setColour(Colour colour) {
    this.colour = colour;
    return this;
  }

  public ImageView<L> setDepthTest(boolean depthTest) {
    this.depthTest = depthTest;
    return this;
  }

  public ImageView<L> setBilinearFiltering(boolean bilinearFiltering) {
    this.bilinearFiltering = bilinearFiltering;
    return this;
  }

  private Optional<Vector2f> getImageSize() {
    if (this.bind()) {
      return Optional.of(new Vector2f(
          GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH),
          GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)));
    }
    return Optional.empty();
  }

  private Optional<Vector2f> getFittedImageSize() {
    return this.getFittedImageSize(this.getContentWidth(), this.getContentHeight());
  }

  private Optional<Vector2f> getFittedImageSize(float containerWidth, float containerHeight) {
    return this.getImageSize().map(imageSize -> this.fitType.getSize(imageSize.x,
        imageSize.y, containerWidth, containerHeight));
  }

  @Override
  public void layout() {
    super.layout();
    this.fittedImageSize = this.getFittedImageSize().orElse(null);
  }

  @Override
  public Vector2f measure(MeasureMode widthMode, float width, MeasureMode heightMode,
      float height) {
    return this.getFittedImageSize(widthMode == MeasureMode.UNDEFINED ? Integer.MAX_VALUE : width,
        heightMode == MeasureMode.UNDEFINED ? Integer.MAX_VALUE : height)
        .orElse(new Vector2f(width, height));
  }

  public Colour getColour() {
    return this.colour;
  }

  private boolean bind() {
    if (this.image != null) {
      RenderUtil.bind(this.image);
      return true;
    }
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void renderContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    if (this.depthTest) {
      RenderSystem.enableDepthTest();
    }
    final float[] colour = this.colour.getColour4f();
    RenderSystem.color4f(colour[0], colour[1], colour[2], colour[3]);
    if (this.bind()) {
      if (this.bilinearFiltering) {
        this.minecraft.getTextureManager().getTexture(image).setFilter(true, true);
      }

      RenderUtil.blit(matrixStack, this.getScaledContentX(), this.getScaledContentY(),
          this.fittedImageSize.x * this.getXScale(), this.fittedImageSize.y * this.getYScale());
    } else {
      RenderUtil.fill(this.getScaledContentX(), this.getScaledContentY(),
          this.getScaledX() + this.getScaledContentWidth(),
          this.getScaledContentY() + this.getScaledContentHeight(),
          0xFFFFFFFF);
    }
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    if (this.depthTest) {
      RenderSystem.disableDepthTest();
    }
    RenderSystem.disableBlend();
  }
}
