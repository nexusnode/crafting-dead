package sm0keysa1m0n.bliss.view;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.types.Point;

public interface ImageAccess extends AutoCloseable {

  Point getIntrinsicSize(float width, float height);

  void prepare(float width, float height);

  void draw(Canvas canvas, Paint paint);

  @Override
  void close();
}
