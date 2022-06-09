package sm0keysa1m0n.bliss.style.parser.value;

import org.jetbrains.annotations.Nullable;
import sm0keysa1m0n.bliss.Length;
import sm0keysa1m0n.bliss.calc.CalcParser;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.parser.StyleReader;

public class LengthParser {

  @Nullable
  public static Length parse(StyleReader reader) throws ParserException {
    var func = reader.readFunction();
    if (func != null) {
      return switch (func.name()) {
        case "calc" -> Length.calculated(CalcParser.parse(func.arguments()));
        default -> throw new ParserException("Unknown length function: " + func.name());
      };
    }

    var val = reader.readFloat();
    if (val != null) {
      if (val == 0) {
        return Length.fixed(0);
      }

      var unit = reader.readUnquotedString();
      if (unit == null) {
        throw new ParserException("Expected unit at index " + reader.getCursor());
      }

      if (unit.equals("%")) {
        return Length.percentage(val);
      } else {
        // TODO add units
        return Length.fixed(val);
      }
    }

    var keyword = reader.readUnquotedString();
    if (keyword != null && keyword.equals("auto")) {
      return Length.AUTO;
    }

    return null;
  }
}
