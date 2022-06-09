package sm0keysa1m0n.bliss.style.parser;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.jdesktop.core.animation.timing.Interpolator;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import sm0keysa1m0n.bliss.style.StyleTransition;
import sm0keysa1m0n.bliss.style.parser.value.InterpolatorParser;
import sm0keysa1m0n.bliss.style.parser.value.ValueParser;
import sm0keysa1m0n.bliss.style.parser.value.ValueParserRegistry;

public class TransitionParser {

  private static final Logger logger = LogUtils.getLogger();

  private final ValueParser<Interpolator> interpolatorParser;

  @Nullable
  private String property;
  @Nullable
  private String delay;
  @Nullable
  private String duration;
  @Nullable
  private String interpolator;

  public TransitionParser() {
    this(ValueParserRegistry.getInstance().getParser(Interpolator.class));
  }

  public TransitionParser(ValueParser<Interpolator> interpolatorParser) {
    this.interpolatorParser = interpolatorParser;
  }

  public boolean tryParse(String name, String value) throws ParserException {
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
          if (this.interpolatorParser.parse(new StyleReader(value)) != null) {
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

    if (name.equals("transition-timing-function")) {
      this.interpolator = value;
      return true;
    }

    return false;
  }

  public Optional<StyleTransition> build() throws ParserException {
    StyleTransition.TargetSelector[] selectors = {StyleTransition.TargetSelector.ALL};
    long[] delays = {0L};
    long[] durations = {0L};
    Interpolator[] interpolators = {InterpolatorParser.EASE};

    if (this.property != null) {
      var properties = this.property.split(" ");
      selectors = new StyleTransition.TargetSelector[properties.length];
      for (int i = 0; i < properties.length; i++) {
        var property = properties[i];
        selectors[i] = property.equals("all")
            ? StyleTransition.TargetSelector.ALL
            : StyleTransition.TargetSelector.forProperty(property);
      }
    }

    if (this.delay != null) {
      var delaysStr = this.delay.split(" ");
      delays = new long[delaysStr.length];
      for (int i = 0; i < delaysStr.length; i++) {
        var delay = delaysStr[i];
        if (delay.endsWith("ms")) {
          try {
            delays[i] = Long.parseLong(delay.substring(0, delay.indexOf("ms")));
          } catch (NumberFormatException e) {
            logger.error("Invalid delay: " + delay);
          }
        } else if (delay.endsWith("s")) {
          try {
            delays[i] = TimeUnit.SECONDS.toMillis(
                Long.parseLong(delay.substring(0, delay.indexOf('s'))));
          } catch (NumberFormatException e) {
            logger.error("Invalid delay: " + delay);
          }

        }
      }
    }

    if (this.duration != null) {
      var durationsStr = this.duration.split(" ");
      durations = new long[durationsStr.length];
      for (int i = 0; i < durationsStr.length; i++) {
        var duration = durationsStr[i];
        if (duration.endsWith("ms")) {
          try {
            durations[i] = Long.parseLong(duration.substring(0, duration.indexOf("ms")));
          } catch (NumberFormatException e) {
            logger.error("Invalid duration: " + duration);
          }
        } else if (duration.endsWith("s")) {
          try {
            durations[i] = TimeUnit.SECONDS.toMillis(
                Long.parseLong(duration.substring(0, duration.indexOf('s'))));
          } catch (NumberFormatException e) {
            logger.error("Invalid duration: " + duration);
          }
        }
      }
    }

    if (this.interpolator != null) {
      var interpolatorsStr = this.interpolator.split(" ");
      interpolators = new Interpolator[interpolatorsStr.length];
      for (int i = 0; i < interpolatorsStr.length; i++) {
        interpolators[i] = this.interpolatorParser.parse(new StyleReader(interpolatorsStr[i]));
      }
    }

    return durations[0] == 0L
        ? Optional.empty()
        : Optional.of(new StyleTransition(selectors, delays, durations, interpolators));
  }

  public static boolean isTransitionProperty(String name) {
    return name.equals("transition")
        || name.equals("transition-property")
        || name.equals("transition-delay")
        || name.equals("transition-duration")
        || name.equals("transition-interpolator");
  }
}
