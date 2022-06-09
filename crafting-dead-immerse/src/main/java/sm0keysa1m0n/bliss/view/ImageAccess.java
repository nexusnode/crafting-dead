package sm0keysa1m0n.bliss.view;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.types.Point;
import sm0keysa1m0n.bliss.ImageRendering;

public interface ImageAccess extends AutoCloseable {

  Point getIntrinsicSize(float width, float height);

  void prepare(float width, float height);

  void draw(Canvas canvas, Paint paint, ImageRendering imageRendering);

  @Override
  void close();

  static ImageAccess forImage(Image image) {
    return new SimpleImageAccess(image);
  }
}
