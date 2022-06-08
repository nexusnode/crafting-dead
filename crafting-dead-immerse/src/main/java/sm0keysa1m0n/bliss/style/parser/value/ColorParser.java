package sm0keysa1m0n.bliss.style.parser.value;

import java.text.NumberFormat;
import java.util.regex.Pattern;
import sm0keysa1m0n.bliss.Color;

public class ColorParser implements ValueParser<Color> {

  public static final ColorParser INSTANCE = new ColorParser();

  private final NumberFormat colorFormat;

  private final Pattern hexColorPattern = Pattern.compile("^#\\d{6}");
  private final Pattern hexAlphaColorPattern = Pattern.compile("^#\\d{6}\\s+\\d{2}%");
  private final Pattern rgbColorPattern =
      Pattern.compile("^rgb\\((\\s?\\d+\\.?\\d+%?\\s?,){2}(\\s?\\d+\\.?\\d+%?\\s?)\\)");
  private final Pattern rgbaColorPattern =
      Pattern.compile("^rgba\\((\\s?\\d+\\.?\\d+%?\\s?,){2,3}(\\s?\\d+\\.?\\d+%?\\s?)\\)");

  private ColorParser() {
    this.colorFormat = NumberFormat.getInstance();
    this.colorFormat.setMinimumFractionDigits(0);
    this.colorFormat.setMaximumFractionDigits(1);
  }

  @Override
  public int validate(String style) {
    if (this.hexColorPattern.matcher(style).matches()) {
      if (this.hexAlphaColorPattern.matcher(style).matches()) {
        return style.substring(0, style.indexOf("%") + 1).length();
      }
      return 7;
    }
    if (!this.rgbColorPattern.matcher(style).matches()
        && !this.rgbaColorPattern.matcher(style).matches()) {
      return 0;
    }
    return style.substring(0, style.indexOf(")") + 1).length();
  }

  @Override
  public Color parse(String style) {
    // Hexa Color ex: #FF0011 20%
    if (style.startsWith("#")) {
      if (!style.contains(" ")) {
        return Color.parseWithFullAlpha(style);
      } else {
        var split = style.split(" ");
        var rgb = split[0];
        var alpha = Float.parseFloat(split[1].substring(0, split[1].length() - 1)) / 100;
        return Color.parseWithAlpha(rgb, alpha);
      }
    }
    // RGB or RGBA Color ex: rgba(255, 255, 255, 255)
    if (style.startsWith("rgb")) {
      var alpha = style.startsWith("rgba");

      var colorNames = style.substring(alpha ? 5 : 4, style.length() - 1).split(",");

      var redValue = 0.0F;
      var greenValue = 0.0F;
      var blueValue = 0.0F;
      var alphaValue = 1.0F;
      for (int i = 0; i < colorNames.length; i++) {
        var value = colorNames[i].trim();
        var percentage = value.endsWith("%");

        var floatValue = percentage
            ? Float.parseFloat(value.substring(0, value.length() - 1)) / 100.0F
            : Float.parseFloat(value);

        if (alpha && i == 3) {
          alphaValue = floatValue;
          continue;
        }

        var colorValue = percentage ? floatValue : floatValue / 255.0F;
        if (i == 0) {
          redValue = colorValue;
        } else if (i == 1) {
          greenValue = colorValue;
        } else if (i == 2) {
          blueValue = colorValue;
        }
      }
      return Color.create(redValue, greenValue, blueValue, alphaValue);
    }

    var color = NamedColors.getColor(style);
    if (color != null) {
      return color;
    }

    throw new RuntimeException("Invalid color: " + style);
  }
}
