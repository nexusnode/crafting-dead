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
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Picture;
import io.github.humbleui.skija.PictureRecorder;
import io.github.humbleui.skija.svg.SVGDOM;
import io.github.humbleui.skija.svg.SVGLengthContext;
import io.github.humbleui.types.Point;
import io.github.humbleui.types.Rect;

public class SvgImageAccess implements ImageAccess {

  private final SVGDOM svg;

  private Picture picture;

  public SvgImageAccess(SVGDOM svg) {
    this.svg = svg;
  }

  @Override
  public void prepare(float width, float height) {
    if (this.picture != null) {
      this.picture.close();
    }
    this.picture = createPicture(this.svg, width, height);
  }

  @Override
  public void draw(Canvas canvas, Paint paint) {
    canvas.drawPicture(this.picture, null, paint);
  }

  @Override
  public void close() {
    this.svg.close();
    if (this.picture != null) {
      this.picture.close();
    }
  }

  private static Picture createPicture(SVGDOM svg, float width, float height) {
    try (var recorder = new PictureRecorder()) {
      var canvas = recorder.beginRecording(Rect.makeWH(width, height));
      canvas.scale(width / svg.getRoot().getViewBox().getWidth(),
          height / svg.getRoot().getViewBox().getHeight());
      svg.render(canvas);
      return recorder.finishRecordingAsPicture();
    }
  }

  @Override
  public Point getIntrinsicSize(float width, float height) {
    return this.svg.getRoot().getIntrinsicSize(new SVGLengthContext(width, height));
  }
}
