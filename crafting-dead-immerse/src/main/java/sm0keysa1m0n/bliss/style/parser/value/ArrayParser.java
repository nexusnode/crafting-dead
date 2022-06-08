package sm0keysa1m0n.bliss.style.parser.value;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayParser<T> implements ValueParser<T[]> {

  private final Class<T> type;
  private final ValueParser<T> elementParser;

  public ArrayParser(Class<T> type, ValueParser<T> elementParser) {
    this.type = type;
    this.elementParser = elementParser;
  }

  @Override
  public int validate(String style) {
    return style.length();
  }

  @SuppressWarnings("unchecked")
  @Override
  public T[] parse(String style) {
    List<T> elements = new ArrayList<>();
    int index = 0;
    while (index < style.length()) {
      var elementLength = this.elementParser.validate(style);
      var endIndex = index + elementLength;
      elements.add(this.elementParser.parse(style.substring(index, endIndex)));
      index += elementLength;
    }
    return elements.toArray(length -> (T[]) Array.newInstance(this.type, length));
  }
}
