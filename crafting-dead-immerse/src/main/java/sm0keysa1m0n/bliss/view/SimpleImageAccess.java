package sm0keysa1m0n.bliss.view;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.types.Point;
import io.github.humbleui.types.Rect;
import sm0keysa1m0n.bliss.ImageRendering;

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
  public void draw(Canvas canvas, Paint paint, ImageRendering imageRendering) {
    canvas.drawImageRect(this.image, Rect.makeWH(this.image.getWidth(), this.image.getHeight()),
        Rect.makeWH(this.width, this.height), imageRendering.getSamplingMode(), paint, true);
  }

  @Override
  public void close() {
    this.image.close();
  }
}
