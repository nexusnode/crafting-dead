package sm0keysa1m0n.bliss.style;

import java.util.ArrayList;
import java.util.Set;
import sm0keysa1m0n.bliss.property.Transition;
import sm0keysa1m0n.bliss.style.selector.StyleNodeState;

public class GenericShorthandDispatcher implements PropertyDispatcher<String> {

  private final String name;
  private final PropertyDispatcher<?>[] dispatchers;

  public GenericShorthandDispatcher(String name, PropertyDispatcher<?>... dispatchers) {
    this.name = name;
    this.dispatchers = dispatchers;
  }

  @Override
  public void defineState(String value, int specificity, Set<StyleNodeState> nodeStates) {
    var current = value;
    var alreadySet = new ArrayList<PropertyDispatcher<?>>();

    while (!current.isEmpty()) {
      var anySet = false;
      for (var child : this.dispatchers) {
        if (alreadySet.contains(child)) {
          continue;
        }

        var validated = child.validate(current);

        if (validated != 0) {
          child.defineState(current.substring(0, validated), specificity, nodeStates);
          current = current.substring(validated).trim();

          alreadySet.add(child);
          anySet = true;
          break;
        }
      }

      if (!anySet) {
        return;
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
}
