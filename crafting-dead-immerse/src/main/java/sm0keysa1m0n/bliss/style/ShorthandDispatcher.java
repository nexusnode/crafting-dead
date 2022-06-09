package sm0keysa1m0n.bliss.style;

import java.util.Set;
import sm0keysa1m0n.bliss.property.Transition;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.parser.StyleReader;
import sm0keysa1m0n.bliss.style.parser.value.ArrayParser;
import sm0keysa1m0n.bliss.style.parser.value.ValueParserRegistry;
import sm0keysa1m0n.bliss.style.selector.StyleNodeState;

public class ShorthandDispatcher<T> implements PropertyDispatcher<T[]> {

  private final String name;
  private final ArrayParser<T> parser;
  private final ShorthandArgMapper argMapper;
  private final PropertyDispatcher<T>[] dispatchers;

  @SafeVarargs
  public ShorthandDispatcher(String name, Class<T> type, ArrayParser<T> parser,
      ShorthandArgMapper argMapper, PropertyDispatcher<T>... dispatchers) {
    this.name = name;
    this.parser = parser;
    this.argMapper = argMapper;
    this.dispatchers = dispatchers;
  }

  @Override
  public void defineState(String value, int specificity, Set<StyleNodeState> nodeStates)
      throws ParserException {
    var parsedValue = this.parser.parse(new StyleReader(value));
    if (parsedValue == null) {
      return;
    }
    this.defineState(parsedValue, specificity, nodeStates);
  }

  @Override
  public void defineState(T[] value, int specificity, Set<StyleNodeState> nodeStates) {
    for (int i = 0; i < value.length; i++) {
      for (int childIndex : this.argMapper.map(i, value.length)) {
        this.dispatchers[childIndex].defineState(value[i], specificity, nodeStates);
      }
    }
  }

  @Override
  public void refreshState() {}

  @Override
  public void reset() {}

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setTransition(Transition transition) {
    for (var dispatcher : this.dispatchers) {
      dispatcher.setTransition(transition);
    }
  }

  @SafeVarargs
  public static <T> ShorthandDispatcher<T> create(String name, Class<T> type,
      ShorthandArgMapper argMapper, PropertyDispatcher<T>... properties) {
    return new ShorthandDispatcher<>(name, type,
        new ArrayParser<>(type, ValueParserRegistry.getInstance().getParser(type), ' '),
        argMapper, properties);
  }
}
