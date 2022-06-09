package sm0keysa1m0n.bliss.style.parser.value;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.parser.StyleReader;

public class ArrayParser<T> implements ValueParser<T[]> {

  private final Class<T> type;
  private final ValueParser<T> elementParser;
  private final char seperator;

  public ArrayParser(Class<T> type, ValueParser<T> elementParser, char seperator) {
    this.type = type;
    this.elementParser = elementParser;
    this.seperator = seperator;
  }

  @SuppressWarnings("unchecked")
  @Nullable
  @Override
  public T[] parse(StyleReader reader) throws ParserException {
    List<T> elements = new ArrayList<>();
    while (reader.canRead()) {
      reader.skipWhitespace();
      var element = this.elementParser.parse(reader);
      if (element == null) {
        throw new ParserException("Expected element at index " + reader.getCursor());
      }
      elements.add(element);
      reader.skipWhitespace();
      if (reader.canRead()) {
        reader.expect(this.seperator);
      }
    }
    return elements.toArray(length -> (T[]) Array.newInstance(this.type, length));
  }
}
