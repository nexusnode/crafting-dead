package sm0keysa1m0n.bliss.style.parser.value;

import java.util.Locale;

public class EnumParser<T extends Enum<T>> implements ValueParser<T> {

  private final Class<T> type;

  public EnumParser(Class<T> type) {
    this.type = type;
  }

  @Override
  public int validate(String style) {
    return style.length();
  }

  @Override
  public T parse(String style) {
    return Enum.valueOf(this.type, style.toUpperCase(Locale.ROOT).replace('-', '_'));
  }
}
