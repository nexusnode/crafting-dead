package sm0keysa1m0n.bliss.property;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Evaluator;
import org.jdesktop.core.animation.timing.Interpolator;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.evaluators.KnownEvaluators;
import org.jetbrains.annotations.Nullable;

public interface Transition {

  Transition INSTANT = new Transition() {

    @Override
    public <T> Runnable transition(StyleableProperty<T> property, T newValue) {
      property.set(newValue);
      return () -> {};
    }
  };

  <T> Runnable transition(StyleableProperty<T> property, T newValue);

  static Transition linear(long durationMs) {
    return linear(0L, durationMs);
  }

  static Transition linear(long delayMs, long durationMs) {
    return create(delayMs, durationMs, null);
  }

  static Transition create(long delayMs, long durationMs, @Nullable Interpolator interpolator) {
    return new Transition() {

      @Override
      public <T> Runnable transition(StyleableProperty<T> property, T newValue) {
        var stopped = new AtomicBoolean();
        var animator = new Animator.Builder()
            .setStartDelay(delayMs, TimeUnit.MILLISECONDS)
            .setDuration(durationMs, TimeUnit.MILLISECONDS)
            .setInterpolator(interpolator)
            .addTarget(new TimingTargetAdapter() {

              private final T startValue = property.get();
              private final Evaluator<T> evaluator =
                  KnownEvaluators.getInstance().getEvaluatorFor(property.getType());

              @Override
              public void timingEvent(Animator source, double fraction) {
                property.set(this.evaluator.evaluate(this.startValue, newValue, fraction));
              }
            })
            .build();
        animator.start();
        return () -> {
          stopped.set(true);
          animator.stop();
        };
      }
    };
  }
}
