package sm0keysa1m0n.bliss.style.parser.value;

import org.jdesktop.core.animation.timing.Interpolator;
import org.jdesktop.core.animation.timing.interpolators.LinearInterpolator;
import org.jetbrains.annotations.Nullable;
import sm0keysa1m0n.bliss.animation.CubicBezierInterpolator;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.parser.StyleReader;

public class InterpolatorParser {

  public static final Interpolator EASE =
      new CubicBezierInterpolator(0.25D, 0.1D, 0.25D, 1.0D);
  public static final Interpolator EASE_IN =
      new CubicBezierInterpolator(0.42D, 0.0D, 1.0D, 1.0D);
  public static final Interpolator EASE_OUT =
      new CubicBezierInterpolator(0.0D, 0.0D, 0.58D, 1.0D);
  public static final Interpolator EASE_IN_OUT =
      new CubicBezierInterpolator(0.42D, 0.0D, 0.58D, 1.0D);

  @Nullable
  public static Interpolator parse(StyleReader reader) throws ParserException {
    var func = reader.readFunction();
    if (func != null) {
      return switch (func.name()) {
        case "cubic-bezier" -> {
          var points = func.arguments().split(",");
          var pointsArray = new double[4];
          for (int i = 0; i < pointsArray.length; i++) {
            pointsArray[i] = Double.parseDouble(points[i].strip());
          }

          yield new CubicBezierInterpolator(
              pointsArray[0], pointsArray[1], pointsArray[2], pointsArray[3]);
        }
        default -> throw new ParserException("Unknown interpolator: " + func.name());
      };
    }

    var keyword = reader.readUnquotedString();
    if (keyword == null) {
      return null;
    }

    return switch (keyword) {
      case "linear" -> LinearInterpolator.getInstance();
      case "ease" -> EASE;
      case "ease-in" -> EASE_IN;
      case "ease-out" -> EASE_OUT;
      default -> throw new ParserException("Unknown interpolator: " + keyword);
    };
  }
}
