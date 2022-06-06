package sm0keysa1m0n.bliss.style;

import java.util.Set;
import sm0keysa1m0n.bliss.property.Transition;
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

  void defineState(String value, int specificity, Set<StyleNodeState> nodeStates);

  void refreshState();

  void reset();

  default int validate(String style) {
    return style.length();
  }

  String getName();
}
