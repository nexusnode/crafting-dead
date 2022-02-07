/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.client.gui.view;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Evaluator;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.evaluators.KnownEvaluators;

public interface Transition<T> {

  Transition<?> INSTANT = (property, newValue) -> {
    property.set(newValue);
    return () -> {
    };
  };

  Runnable transition(ValueProperty<T> property, T newValue);

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
      var stopped = new AtomicBoolean();
      var builder = new Animator.Builder()
          .addTarget(new TimingTargetAdapter() {

            private final T startValue = property.get();

            private final Evaluator<T> evaluator = evaluatorIn == null
                ? KnownEvaluators.getInstance().getEvaluatorFor(property.getType())
                : evaluatorIn;

            @Override
            public void timingEvent(Animator source, double fraction) {
              property.set(this.evaluator.evaluate(this.startValue, newValue, fraction));
            }
          });
      configurer.accept(builder);
      var animator = builder.build();
      animator.start();
      return () -> {
        stopped.set(true);
        animator.stop();
      };
    };
  }
}
