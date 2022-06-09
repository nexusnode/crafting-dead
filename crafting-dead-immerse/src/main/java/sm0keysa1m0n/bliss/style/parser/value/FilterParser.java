package sm0keysa1m0n.bliss.style.parser.value;

import org.jetbrains.annotations.Nullable;
import sm0keysa1m0n.bliss.Filter;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.parser.StyleReader;

public class FilterParser {

  @Nullable
  public static Filter parse(StyleReader reader) throws ParserException {
    var func = reader.readFunction();
    if (func == null) {
      return null;
    }

    var filter = switch (func.name()) {
      case "blur" -> Filter.blur(Float.valueOf(func.arguments().strip().replace("px", "")));
      default -> throw new IllegalArgumentException("Unknown filter: " + func.name());
    };

    return filter;
  }
}
