package sm0keysa1m0n.bliss.style.parser.value;

import org.jetbrains.annotations.Nullable;
import sm0keysa1m0n.bliss.Color;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.parser.StyleReader;

public class ColorParser {

  private static String doubleChars(String input) {
    var builder = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      var ch = input.charAt(i);
      builder.append(ch);
      builder.append(ch);
    }
    return builder.toString();
  }

  private static ParserException invalidHexColor(int index) {
    return new ParserException("Invalid hex color at index " + index);
  }

  @Nullable
  public static Color parse(StyleReader reader) throws ParserException {
    var start = reader.getCursor();

    // Hex color eg: #FF0011
    if (reader.peek() == '#') {
      reader.skip();
      var hexValue = reader.readUnquotedString();
      if (hexValue == null) {
        throw invalidHexColor(start);
      }

      try {
        return Color.create(switch (hexValue.length()) {
          case 3 -> Integer.parseUnsignedInt(doubleChars(hexValue), 16) + Color.FULL_ALPHA;
          case 4 -> Integer.parseUnsignedInt(doubleChars(hexValue), 16);
          case 6 -> Integer.parseUnsignedInt(hexValue, 16) + Color.FULL_ALPHA;
          case 8 -> Integer.parseUnsignedInt(hexValue, 16);
          default -> throw invalidHexColor(start);
        });
      } catch (NumberFormatException e) {
        throw invalidHexColor(start);
      }
    }

    var func = reader.readFunction();

    // RGB or RGBA Color ex: rgba(255, 255, 255, 255)
    if (func != null) {
      var alpha = func.name().equals("rgba");

      var colorNames = func.arguments().split(",");

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

    var keyword = reader.readUnquotedString();
    if (keyword == null) {
      return null;
    }

    var color = NamedColors.getColor(keyword);
    if (color != null) {
      return color;
    }

    reader.setCursor(start);
    return null;
  }
}
