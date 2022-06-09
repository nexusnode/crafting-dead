package sm0keysa1m0n.bliss.style.parser.value;

import org.jetbrains.annotations.Nullable;
import sm0keysa1m0n.bliss.style.Percentage;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.parser.StyleReader;

public class PercentageParser {

  @Nullable
  public static Percentage parse(StyleReader reader) throws ParserException {
    var val = reader.readFloat();
    if (val == null) {
      return null;
    }

    if (reader.peek() == '%') {
      reader.skip();
      return new Percentage(val / 100.0F);
    }

    return new Percentage(val);
  }
}
