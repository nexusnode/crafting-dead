package sm0keysa1m0n.bliss.style.parser.value;

import java.util.Locale;
import org.jetbrains.annotations.Nullable;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.parser.StyleReader;

public class EnumParser<T extends Enum<T>> implements ValueParser<T> {

  private final Class<T> type;

  public EnumParser(Class<T> type) {
    this.type = type;
  }

  @Nullable
  @Override
  public T parse(StyleReader reader) throws ParserException {
    var value = reader.readUnquotedString();
    if (value == null) {
      return null;
    }
    var name = value.toUpperCase(Locale.ROOT).replace('-', '_');
    try {
      return Enum.valueOf(this.type, name);
    } catch (IllegalArgumentException e) {
      throw new ParserException("Unknown value: " + value);
    }
  }
}
