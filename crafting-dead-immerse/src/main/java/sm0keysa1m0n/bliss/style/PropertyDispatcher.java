package sm0keysa1m0n.bliss.style;

import java.util.Set;
import sm0keysa1m0n.bliss.property.Transition;
import sm0keysa1m0n.bliss.style.parser.ParserException;
import sm0keysa1m0n.bliss.style.selector.StyleNodeState;

/**
 * An abstract style property which may dispatch to multiple properties or just a single property.
 * 
 * @author Sm0keySa1m0n
 *
 * @param <T> - proeprty type
 */
public interface PropertyDispatcher<T> {

  void setTransition(Transition transition);

  void defineState(String value, int specificity, Set<StyleNodeState> nodeStates)
      throws ParserException;

  /**
   * Varargs version of {@link #defineState(Object, Set)}.
   */
  default void defineState(T value, StyleNodeState... states) {
    this.defineState(value, Set.of(states));
  }

  /**
   * User-facing method to define a state.
   * 
   * @param value - the value to associate the state with
   * @param states - the state
   */
  default void defineState(T value, Set<StyleNodeState> states) {
    this.defineState(value, 1000, states);
  }

  void defineState(T value, int specificity, Set<StyleNodeState> nodeStates);

  void refreshState();

  void reset();

  String getName();
}
