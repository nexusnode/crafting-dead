package com.craftingdead.immerse.client.gui.view.style;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.core.animation.timing.Interpolator;
import com.craftingdead.immerse.client.gui.view.Transition;
import com.craftingdead.immerse.client.gui.view.style.adapter.InterpolatorTranslator;
import com.craftingdead.immerse.client.gui.view.style.adapter.StyleDecoder;
import com.craftingdead.immerse.client.gui.view.style.adapter.StyleTranslatorRegistry;
import com.craftingdead.immerse.client.gui.view.style.adapter.StyleValidator;

public class TransitionBuilder {

  private static final Logger logger = LogManager.getLogger();

  private static final TransitionFilter ALL = Map::values;

  private final StyleValidator<Interpolator> interpolatorValidator;
  private final StyleDecoder<Interpolator> interpolatorDecoder;

  @Nullable
  private String property;
  @Nullable
  private String delay;
  @Nullable
  private String duration;
  @Nullable
  private String interpolator;

  private TransitionFilter[] filters = {ALL};
  private long[] delays = {0L};
  private long[] durations = {0L};
  private Interpolator[] interpolators = {InterpolatorTranslator.EASE};

  public TransitionBuilder() {
    this(StyleTranslatorRegistry.getInstance().getValidator(Interpolator.class),
        StyleTranslatorRegistry.getInstance().getDecoder(Interpolator.class));
  }

  public TransitionBuilder(StyleValidator<Interpolator> interpolatorValidator,
      StyleDecoder<Interpolator> interpolatorDecoder) {
    this.interpolatorValidator = interpolatorValidator;
    this.interpolatorDecoder = interpolatorDecoder;
  }

  public static boolean isTransitionProperty(String name) {
    return name.equals("transition")
        || name.equals("transition-property")
        || name.equals("transition-delay")
        || name.equals("transition-duration")
        || name.equals("transition-interpolator");
  }

  public boolean tryParse(String name, String value) {
    if (name.equals("transition")) {
      var transitions = value.split(",");
      for (var transition : transitions) {
        var values = transition.trim().split(" ");

        this.property = values[0];
        this.duration = values[1];
        if (values.length == 2) {
          continue;
        }

        if (values.length == 3) {
          var temp = values[2];
          if (this.interpolatorValidator.validate(temp) > 0) {
            this.interpolator = temp;
          } else {
            this.delay = temp;
          }
          continue;
        }

        this.interpolator = values[2];
        this.delay = values[3];
      }
      return true;
    }

    if (name.equals("transition-property")) {
      this.property = value;
      return true;
    }

    if (name.equals("transition-delay")) {
      this.delay = value;
      return true;
    }

    if (name.equals("transition-duration")) {
      this.duration = value;
      return true;
    }

    if (name.equals("transition-interpolator")) {
      this.interpolator = value;
      return true;
    }

    return false;
  }

  public void build() {
    if (this.property != null) {
      var properties = this.property.split(" ");
      this.filters = new TransitionFilter[properties.length];
      for (int i = 0; i < properties.length; i++) {
        var property = properties[i];
        this.filters[i] = property.equals("all") ? ALL : TransitionFilter.forProperty(property);
      }
    }

    if (this.delay != null) {
      var delays = this.delay.split(" ");
      this.delays = new long[delays.length];
      for (int i = 0; i < delays.length; i++) {
        var delay = delays[i];
        if (delay.endsWith("ms")) {
          try {
            this.delays[i] = Long.parseLong(delay.substring(0, delay.indexOf("ms")));
          } catch (NumberFormatException e) {
            logger.error("Invalid delay: " + delay);
          }
        } else if (delay.endsWith("s")) {
          try {
            this.delays[i] = TimeUnit.SECONDS.toMillis(
                Long.parseLong(delay.substring(0, delay.indexOf('s'))));
          } catch (NumberFormatException e) {
            logger.error("Invalid delay: " + delay);
          }

        }
      }
    }

    if (this.duration != null) {
      var durations = this.duration.split(" ");
      this.durations = new long[durations.length];
      for (int i = 0; i < durations.length; i++) {
        var duration = durations[i];
        if (duration.endsWith("ms")) {
          try {
            this.durations[i] = Long.parseLong(duration.substring(0, duration.indexOf("ms")));
          } catch (NumberFormatException e) {
            logger.error("Invalid duration: " + duration);
          }
        } else if (duration.endsWith("s")) {
          try {
            this.durations[i] = TimeUnit.SECONDS.toMillis(
                Long.parseLong(duration.substring(0, duration.indexOf('s'))));
          } catch (NumberFormatException e) {
            logger.error("Invalid duration: " + duration);
          }
        }
      }
    }

    if (this.interpolator != null) {
      var interpolators = this.interpolator.split(" ");
      this.interpolators = new Interpolator[interpolators.length];
      for (int i = 0; i < interpolators.length; i++) {
        this.interpolators[i] = this.interpolatorDecoder.decode(interpolators[i]);
      }
    }
  }

  public void apply(Map<String, PropertyDispatcher<?>> dispatchers) {
    if (this.durations == null) {
      return;
    }

    if (this.filters == null) {
      return;
    }

    for (int i = 0; i < this.filters.length; i++) {
      var filter = this.filters[i];

      var delay = this.delays[i % this.delays.length];
      var duration = this.durations[i % this.durations.length];
      var interpolator = this.interpolators[i % this.interpolators.length];

      var transition = Transition.create(delay, duration, interpolator);

      for (var dispatcher : filter.filter(dispatchers)) {
        dispatcher.setTransition(transition);
      }
    }
  }

  @FunctionalInterface
  private interface TransitionFilter {

    Iterable<PropertyDispatcher<?>> filter(Map<String, PropertyDispatcher<?>> dispatchers);

    static TransitionFilter forProperty(String property) {
      return dispatchers -> Collections.singleton(dispatchers.get(property));
    }
  }
}
