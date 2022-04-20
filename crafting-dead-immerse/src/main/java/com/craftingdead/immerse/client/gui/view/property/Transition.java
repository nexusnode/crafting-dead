/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view.property;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Evaluator;
import org.jdesktop.core.animation.timing.Interpolator;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.evaluators.KnownEvaluators;

public interface Transition {

  Transition INSTANT = new Transition() {

    @Override
    public <T> Runnable transition(StatefulProperty<T> property, T newValue) {
      property.set(newValue);
      return () -> {};
    }
  };

  <T> Runnable transition(StatefulProperty<T> property, T newValue);

  static Transition linear(long durationMs) {
    return linear(0L, durationMs);
  }

  static Transition linear(long delayMs, long durationMs) {
    return create(delayMs, durationMs, null);
  }

  static Transition create(long delayMs, long durationMs, @Nullable Interpolator interpolator) {
    return new Transition() {

      @Override
      public <T> Runnable transition(StatefulProperty<T> property, T newValue) {
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
