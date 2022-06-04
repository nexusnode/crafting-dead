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
