package sm0keysa1m0n.bliss.style;

import java.util.Collections;
import java.util.Map;
import org.jdesktop.core.animation.timing.Interpolator;
import sm0keysa1m0n.bliss.property.Transition;

public record StyleTransition(TargetSelector[] selectors, long[] delays, long[] durations,
    Interpolator[] interpolators) {

  public void apply(Map<String, PropertyDispatcher<?>> dispatchers) {
    for (int i = 0; i < this.selectors.length; i++) {
      var selector = this.selectors[i];

      var delay = this.delays[i % this.delays.length];
      var duration = this.durations[i % this.durations.length];
      var interpolator = this.interpolators[i % this.interpolators.length];

      var transition = Transition.create(delay, duration, interpolator);

      for (var dispatcher : selector.select(dispatchers)) {
        dispatcher.setTransition(transition);
      }
    }
  }

  @FunctionalInterface
  public interface TargetSelector {

    TargetSelector ALL = Map::values;

    Iterable<PropertyDispatcher<?>> select(Map<String, PropertyDispatcher<?>> dispatchers);

    static TargetSelector forProperty(String property) {
      return dispatchers -> Collections.singleton(dispatchers.get(property));
    }
  }
}
