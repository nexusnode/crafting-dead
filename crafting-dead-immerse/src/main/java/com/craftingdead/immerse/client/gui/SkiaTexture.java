package com.craftingdead.immerse.client.gui;

import java.nio.FloatBuffer;
import org.lwjgl.system.MemoryUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.math.Matrix4f;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ColorAlphaType;
import io.github.humbleui.skija.ColorType;
import io.github.humbleui.skija.ImageInfo;
import io.github.humbleui.skija.Matrix33;
import io.github.humbleui.skija.Surface;

public class SkiaTexture {

  private final int textureId = TextureUtil.generateTextureId();

  private NativeImage image;
  private Surface surface;
  private Canvas canvas;

  public void init(int width, int height) {
    if (this.surface != null) {
      this.surface.close();
    }
    if (this.image != null) {
      this.image.close();
    }

    var format = NativeImage.Format.RGBA;
    var size = width * height * format.components();
    var pixels = MemoryUtil.nmemAlloc(size);
    this.image = new NativeImage(format, width, height, false, pixels);
    TextureUtil.prepareImage(this.textureId, width, height);

    this.surface = Surface.makeRasterDirect(
        new ImageInfo(width, height, ColorType.RGBA_8888, ColorAlphaType.UNPREMUL), pixels,
        width * format.components());

    this.canvas = this.surface.getCanvas();
  }

  public Canvas canvas() {
    return this.canvas;
  }

  public void upload() {
    this.surface.flush();
    if (this.image == null) {
      throw new IllegalStateException("init has not been called.");
    }
    GlStateManager._bindTexture(this.textureId);
    this.image.upload(0, 0, 0, false);
  }

  public int getTextureId() {
    return this.textureId;
  }

  public static Matrix33 convert(Matrix4f matrix) {
    float[] _mat = new float[16];
    matrix.store(FloatBuffer.wrap(_mat));
    return new Matrix33(_mat[0], _mat[1], _mat[3], _mat[4], _mat[5], _mat[7], _mat[12], _mat[13],
        _mat[15]);
  }
}
