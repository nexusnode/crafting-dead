package sm0keysa1m0n.bliss.view;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import io.github.humbleui.skija.Path;
import io.github.humbleui.types.Point;
import io.github.humbleui.types.Rect;

public class GeometryUtil {

  public static Point getTopLeft(Rect rect) {
    return new Point(rect.getLeft(), rect.getTop());
  }

  public static Point getTopRight(Rect rect) {
    return new Point(rect.getRight(), rect.getTop());
  }

  public static Point getBottomLeft(Rect rect) {
    return new Point(rect.getLeft(), rect.getBottom());
  }

  public static Point getBottomRight(Rect rect) {
    return new Point(rect.getRight(), rect.getBottom());
  }

  public static Path polygonPathFromPoints(Point[] points) {
    var path = new Path();
    if (points.length < 2) {
      return path;
    }

    path.moveTo(points[0]);
    for (int i = 1; i < points.length; i++) {
      path.lineTo(points[i]);
    }

    path.closePath();
    return path;
  }

  public record Slope(float gradient, float intersect) {}

  @Nullable
  public static Slope findSlope(Point p1, Point p2) {
    if (p2.getX() == p1.getX()) {
      return null;
    }

    // y = mx + c
    float slope = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
    return new Slope(slope, p1.getY() - slope * p1.getX());
  }

  public static Optional<Point> findIntersection(Point p1, Point p2, Point d1, Point d2) {
    var pSlope = findSlope(p1, p2);
    var dSlope = findSlope(d1, d2);

    if (dSlope == pSlope) {
      return Optional.empty();
    }

    if (pSlope == null) {
      var x = p1.getX();
      var y = dSlope.gradient() * x + dSlope.intersect();
      return Optional.of(new Point(x, y));
    }

    if (dSlope == null) {
      var x = d1.getX();
      var y = pSlope.gradient() * x + pSlope.intersect();
      return Optional.of(new Point(x, y));
    }

    // Find x at intersection, where ys overlap; x = (c' - c) / (m - m')
    var x = (dSlope.intersect() - pSlope.intersect()) / (pSlope.gradient() - dSlope.gradient());
    var y = pSlope.gradient() * x + pSlope.intersect();
    return Optional.of(new Point(x, y));
  }
}
