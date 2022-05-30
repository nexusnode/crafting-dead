package com.craftingdead.immerse.client.gui.view;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.SamplingMode;
import io.github.humbleui.types.Point;
import io.github.humbleui.types.Rect;

public class SimpleImageAccess implements ImageAccess {

  private final Image image;

  private float width;
  private float height;

  public SimpleImageAccess(Image image) {
    this.image = image;
  }

  @Override
  public Point getIntrinsicSize(float width, float height) {
    return new Point(this.image.getWidth(), this.image.getHeight());
  }

  @Override
  public void prepare(float width, float height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public void draw(Canvas canvas, Paint paint) {
    canvas.drawImageRect(this.image, Rect.makeWH(this.image.getWidth(), this.image.getHeight()),
        Rect.makeWH(this.width, this.height), SamplingMode.LINEAR, paint, true);
  }

  @Override
  public void close() {
    this.image.close();
  }
}
