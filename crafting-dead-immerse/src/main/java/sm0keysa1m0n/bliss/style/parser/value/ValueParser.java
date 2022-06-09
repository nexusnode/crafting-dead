package sm0keysa1m0n.bliss.style.parser.value;

import org.jetbrains.annotations.Nullable;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.parser.StyleReader;

@FunctionalInterface
public interface ValueParser<T> {

  /**
   * Parse a css string and return the value.
   *
   * @param reader - the reader
   * @return the decoded value
   */
  @Nullable
  T parse(StyleReader reader) throws ParserException;

  @Nullable
  default T parseAndRollback(StyleReader reader) throws ParserException {
    var start = reader.getCursor();
    try {
      return this.parse(reader);
    } catch (ParserException e) {
      reader.setCursor(start);
      throw e;
    }
  }
}
