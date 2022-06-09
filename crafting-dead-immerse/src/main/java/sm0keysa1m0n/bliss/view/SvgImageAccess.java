package sm0keysa1m0n.bliss.view;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Picture;
import io.github.humbleui.skija.PictureRecorder;
import io.github.humbleui.skija.svg.SVGDOM;
import io.github.humbleui.skija.svg.SVGLengthContext;
import io.github.humbleui.types.Point;
import io.github.humbleui.types.Rect;
import sm0keysa1m0n.bliss.ImageRendering;

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
  public void draw(Canvas canvas, Paint paint, ImageRendering imageRendering) {
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
