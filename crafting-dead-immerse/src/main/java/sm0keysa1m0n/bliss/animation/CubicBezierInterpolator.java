package sm0keysa1m0n.bliss.animation;

import org.jdesktop.core.animation.timing.Interpolator;

/**
 * Derived from: https://github.com/rdallasgray/bez
 */
public class CubicBezierInterpolator implements Interpolator {

  private double startX, startY;
  private double endX, endY;

  private double aX, aY;
  private double bX, bY;
  private double cX, cY;

  public CubicBezierInterpolator(double startX, double startY, double endX, double endY) {
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
  }

  @Override
  public double interpolate(double time) {
    return this.getBezierCoordinateY(this.getXForTime(time));
  }

  private double getBezierCoordinateY(double time) {
    this.cY = 3.0D * this.startY;
    this.bY = 3.0D * (this.endY - this.startY) - this.cY;
    this.aY = 1.0D - this.cY - this.bY;
    return time * (this.cY + time * (this.bY + time * this.aY));
  }

  private double getXForTime(double time) {
    double x = time;
    double z;
    for (int i = 1; i < 14; i++) {
      z = this.getBezierCoordinateX(x) - time;
      if (Math.abs(z) < 1e-3) {
        break;
      }
      x -= z / this.getXDerivate(x);
    }
    return x;
  }

  private double getXDerivate(double t) {
    return this.cX + t * (2.0D * this.bX + 3.0D * this.aX * t);
  }

  private double getBezierCoordinateX(double time) {
    this.cX = 3.0D * this.startX;
    this.bX = 3.0D * (this.endX - this.startX) - this.cX;
    this.aX = 1.0D - this.cX - this.bX;
    return time * (this.cX + time * (this.bX + time * this.aX));
  }
}
