package com.craftingdead.immerse.client.gui.view;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Evaluator;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.evaluators.KnownEvaluators;

public interface Transition<T> {

  Transition<?> INSTANT = ValueStyleProperty::set;

  void transition(ValueStyleProperty<T> property, T newValue);

  @SuppressWarnings("unchecked")
  static <T> Transition<T> instant() {
    return (Transition<T>) INSTANT;
  }

  static <T> Transition<T> linear(long durationMs) {
    return linear(0L, durationMs, null);
  }

  static <T> Transition<T> linear(long delayMs, long durationMs,
      @Nullable Evaluator<T> evaluator) {
    return create(evaluator, builder -> builder
        .setStartDelay(delayMs, TimeUnit.MILLISECONDS)
        .setDuration(durationMs, TimeUnit.MILLISECONDS));
  }

  static <T> Transition<T> create(@Nullable Evaluator<T> evaluatorIn,
      Consumer<Animator.Builder> configurer) {
    return (property, newValue) -> {
      Animator.Builder builder = new Animator.Builder()
          .addTarget(new TimingTargetAdapter() {

            private final T startValue = property.get();

            private final Evaluator<T> evaluator = evaluatorIn == null
                ? KnownEvaluators.getInstance().getEvaluatorFor(property.getValueType())
                : evaluatorIn;

            @Override
            public void timingEvent(Animator source, double fraction) {
              property.set(this.evaluator.evaluate(this.startValue, newValue, fraction));
            }
          });
      configurer.accept(builder);
      builder.build().start();
    };
  }
}
