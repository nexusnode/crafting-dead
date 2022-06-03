/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view;

import java.io.IOException;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.MeasureMode;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import io.github.humbleui.skija.BlendMode;
import io.github.humbleui.skija.ColorFilter;
import io.github.humbleui.skija.Data;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PaintMode;
import io.github.humbleui.skija.svg.SVGDOM;
import io.github.humbleui.types.Rect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

public class ImageView extends View {

  private static final Logger logger = LogUtils.getLogger();

  @Nullable
  private ImageAccess image;

  private Vec2 fittedImageSize;

  public ImageView(Properties properties) {
    super(properties);
  }

  @Override
  protected void setLayout(Layout layout) {
    super.setLayout(layout);
    layout.setMeasureFunction(this::measure);
  }

  private void setImage(ImageAccess image) {
    if (this.image != null) {
      this.image.close();
    }

    this.image = image;

    if (this.hasLayout()) {
      this.getLayout().markDirty();
    }
  }

  public final ImageView setImage(Image image) {
    this.setImage(new SimpleImageAccess(image));
    return this;
  }

  public final ImageView setImage(ResourceLocation imageLocation) {
    try (var inputStream =
        this.minecraft.getResourceManager().getResource(imageLocation).getInputStream()) {
      var bytes = inputStream.readAllBytes();
      if (imageLocation.getPath().endsWith(".svg")) {
        this.setImage(new SvgImageAccess(new SVGDOM(Data.makeFromBytes(bytes))));
      } else {
        this.setImage(new SimpleImageAccess(Image.makeFromEncoded(bytes)));
      }
    } catch (IOException e) {
      logger.warn("Failed to load image: {}", imageLocation, e);
    }

    return this;
  }

  private Optional<Vec2> getFittedImageSize() {
    return this.getFittedImageSize(this.getContentWidth(), this.getContentHeight());
  }

  private Optional<Vec2> getFittedImageSize(float containerWidth, float containerHeight) {
    if (this.image == null) {
      return Optional.empty();
    }

    var size = this.image.getIntrinsicSize(containerWidth, containerHeight);
    return Optional.of(this.getStyle().objectFit.get().getSize(
        size.getX(), size.getY(), containerWidth, containerHeight));
  }

  @Override
  public void layout() {
    super.layout();
    this.fittedImageSize = this.getFittedImageSize().orElse(null);
    if (this.fittedImageSize != null) {
      this.image.prepare(this.fittedImageSize.x * (float) this.window.getGuiScale(),
          this.fittedImageSize.y * (float) this.window.getGuiScale());
    }
  }

  private Vec2 measure(MeasureMode widthMode, float width, MeasureMode heightMode, float height) {
    return this
        .getFittedImageSize(widthMode == MeasureMode.UNDEFINED ? Integer.MAX_VALUE : width,
            heightMode == MeasureMode.UNDEFINED ? Integer.MAX_VALUE : height)
        .orElse(new Vec2(width, height));
  }

  @Override
  public void renderContent(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);

    if (this.image == null) {
      this.skia.begin();
      {
        try (var paint = new Paint()) {
          paint.setAlphaf(this.getAlpha()).setColor(0xFFFFFF).setMode(PaintMode.FILL);
          var scale = (float) this.window.getGuiScale();
          this.skia.canvas().drawRect(Rect.makeXYWH(
              this.getScaledContentX() * scale,
              this.getScaledContentY() * scale,
              this.getScaledContentWidth() * scale,
              this.getScaledContentHeight() * scale), paint);
        }
      }
      this.skia.end();
      return;
    }

    this.skia.begin();
    {
      var canvas = this.skia.canvas();

      canvas.translate(
          this.getScaledContentX() * (float) this.window.getGuiScale(),
          this.getScaledContentY() * (float) this.window.getGuiScale());
      canvas.scale(this.getXScale(), this.getYScale());

      try (var paint = new Paint()) {
        paint.setColorFilter(ColorFilter.makeBlend(
            RenderUtil.multiplyAlpha(this.getStyle().color.get().valueHex(), this.getAlpha()),
            BlendMode.MODULATE));
        this.image.draw(canvas, paint);
      }
      canvas.resetMatrix();

    }
    this.skia.end();
  }

  @Override
  public void close() {
    super.close();
    if (this.image != null) {
      this.image.close();
      this.image = null;
    }
  }
}
