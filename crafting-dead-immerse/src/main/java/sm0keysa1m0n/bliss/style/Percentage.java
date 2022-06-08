package sm0keysa1m0n.bliss.style;

import org.jdesktop.core.animation.timing.Evaluator;
import org.jdesktop.core.animation.timing.evaluators.KnownEvaluators;

/**
 * Value which can be parsed as either 0.0 to 1.0 or 0% to 100%.
 */
public record Percentage(float value) {

  public static final Percentage ZERO = new Percentage(0.0F);
  public static final Percentage ONE_HUNDRED = new Percentage(1.0F);

  static {
    KnownEvaluators.getInstance().register(new Evaluator<Percentage>() {

      @Override
      public Percentage evaluate(Percentage v0, Percentage v1, double fraction) {
        return new Percentage(v0.value + ((v1.value - v0.value) * (float) fraction));
      }

      @Override
      public Class<Percentage> getEvaluatorClass() {
        return Percentage.class;
      }
    });
  }
}
