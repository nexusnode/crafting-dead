package sm0keysa1m0n.bliss.style.parser.value;

import org.jdesktop.core.animation.timing.Interpolator;
import org.jdesktop.core.animation.timing.interpolators.LinearInterpolator;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import sm0keysa1m0n.bliss.animation.CubicBezierInterpolator;

public class InterpolatorParser implements ValueParser<Interpolator> {

  public static final Interpolator EASE =
      new CubicBezierInterpolator(0.25D, 0.1D, 0.25D, 1.0D);
  public static final Interpolator EASE_IN =
      new CubicBezierInterpolator(0.42D, 0.0D, 1.0D, 1.0D);
  public static final Interpolator EASE_OUT =
      new CubicBezierInterpolator(0.0D, 0.0D, 0.58D, 1.0D);
  public static final Interpolator EASE_IN_OUT =
      new CubicBezierInterpolator(0.42D, 0.0D, 0.58D, 1.0D);

  public static final InterpolatorParser INSTANCE = new InterpolatorParser();

  private InterpolatorParser() {}

  @Override
  public int validate(String style) {
    if (style.startsWith("ease")) {
      return "ease".length();
    } else if (style.startsWith("linear")) {
      return "linear".length();
    } else if (style.startsWith("ease-in")) {
      return "ease-in".length();
    } else if (style.startsWith("ease-out")) {
      return "ease-out".length();
    } else if (style.startsWith("ease-in-out")) {
      return "ease-in-out".length();
    }

    if (style.startsWith("cubic-bezier")) {
      return style.indexOf(')') + 1;
    }

    throw new IllegalStateException("Unsupported timing function: " + style);
  }

  @Override
  public Interpolator parse(String style) {
    if (style.equals("ease")) {
      return EASE;
    } else if (style.equals("linear")) {
      return LinearInterpolator.getInstance();
    } else if (style.equals("ease-in")) {
      return EASE_IN;
    } else if (style.equals("ease-out")) {
      return EASE_OUT;
    } else if (style.equals("ease-in-out")) {
      return EASE_IN_OUT;
    }

    if (style.startsWith("cubic-bezier")) {
      var points =
          style.substring(style.indexOf('('), style.indexOf(')')).replace(" ", "").split(",");

      var pointsArray = new double[4];
      for (int i = 0; i < pointsArray.length; i++) {
        pointsArray[i] = Double.parseDouble(points[i]);
      }

      return new SplineInterpolator(pointsArray[0], pointsArray[1], pointsArray[2], pointsArray[3]);
    }

    throw new IllegalStateException("Unsupported timing function: " + style);
  }
}
