package sm0keysa1m0n.bliss.style.parser.value;

import org.jetbrains.annotations.Nullable;
import sm0keysa1m0n.bliss.BoxShadow;
import sm0keysa1m0n.bliss.Color;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.parser.StyleReader;

public class BoxShadowParser {

  @Nullable
  private static Boolean parseInset(StyleReader reader) {
    var start = reader.getCursor();
    var keyword = reader.readUnquotedString();
    if (keyword == null) {
      return null;
    }

    if (keyword.equals("inset")) {
      return true;
    }

    reader.setCursor(start);
    return null;
  }

  @Nullable
  public static BoxShadow parse(StyleReader reader) throws ParserException {
    var color = ColorParser.parse(reader);
    if (color != null) {
      reader.skipWhitespace();
    }

    var inset = parseInset(reader);
    if (inset != null) {
      reader.skipWhitespace();
    }

    if (color == null) {
      color = ColorParser.parse(reader);
      if (color != null) {
        reader.skipWhitespace();
      }
    }

    Float xOffset;
    Float yOffset;
    Float blurRadius;
    Float spreadRadius;

    xOffset = reader.readFloat();
    if (xOffset == null) {
      if (color == null) {
        return null;
      }

      throw new ParserException("X-offset expected at index " + reader.getCursor());
    }
    var xOffsetUnit = reader.readUnquotedString();
    if (xOffset != 0 && xOffsetUnit == null) {
      throw new ParserException("X-offset unit expected at index " + reader.getCursor());
    }

    reader.skipWhitespace();

    yOffset = reader.readFloat();
    if (yOffset == null) {
      throw new ParserException("Y-offset expected at index " + reader.getCursor());
    }
    var yOffsetUnit = reader.readUnquotedString();
    if (yOffset != 0 && yOffsetUnit == null) {
      throw new ParserException("Y-offset unit expected at index " + reader.getCursor());
    }

    reader.skipWhitespace();

    blurRadius = reader.readFloat();
    if (blurRadius == null) {
      blurRadius = 0.0F;
    } else {
      var blurRadiusUnit = reader.readUnquotedString();
      if (blurRadius != 0 && blurRadiusUnit == null) {
        throw new ParserException("Blur radius unit expected at index " + reader.getCursor());
      }
      reader.skipWhitespace();
    }

    spreadRadius = reader.readFloat();
    if (spreadRadius == null) {
      spreadRadius = 0.0F;
    } else {
      var spreadRadiusUnit = reader.readUnquotedString();
      if (spreadRadius != 0 && spreadRadiusUnit == null) {
        throw new ParserException("Spread radius unit expected at index " + reader.getCursor());
      }
      reader.skipWhitespace();
    }

    if (inset == null) {
      inset = parseInset(reader);
      if (inset != null) {
        reader.skipWhitespace();
      }
    }

    if (color == null) {
      color = ColorParser.parse(reader);
      if (color == null) {
        color = Color.BLACK;
      } else {
        reader.skipWhitespace();
      }
    }

    if (inset == null) {
      inset = parseInset(reader);
      if (inset == null) {
        inset = false;
      }
    }

    return new BoxShadow(inset, xOffset, yOffset, blurRadius, spreadRadius, color);
  }
}
