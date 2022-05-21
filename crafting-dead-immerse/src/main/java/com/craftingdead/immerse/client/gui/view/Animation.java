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

package com.craftingdead.immerse.client.gui.view;

import org.jetbrains.annotations.Nullable;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Animator.Direction;
import com.craftingdead.immerse.client.gui.view.property.AnimatedProperty;
import com.craftingdead.immerse.client.gui.view.property.StatefulProperty;
import org.jdesktop.core.animation.timing.KeyFrames;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;

public class Animation<T> extends TimingTargetAdapter {

  private final AnimatedProperty<T> property;
  private KeyFrames<T> keyFrames;

  private final boolean fromCurrent;

  private Animation(AnimatedProperty<T> property, KeyFrames<T> keyFrames, boolean fromCurrent) {
    this.property = property;
    this.keyFrames = keyFrames;
    this.fromCurrent = fromCurrent;
  }

  @Override
  public void timingEvent(Animator source, double fraction) {
    this.property.setAnimatedValue(source.isRunning()
        ? this.keyFrames.getInterpolatedValueAt(fraction)
        : null);
  }

  @Override
  public void begin(Animator source) {
    if (this.fromCurrent) {
      final var startValue = this.property.get();
      final var builder = new KeyFrames.Builder<T>(startValue);
      var first = true;
      for (var frame : this.keyFrames) {
        if (first) {
          first = false;
        } else {
          builder.addFrame(frame);
        }
      }
      this.keyFrames = builder.build();
    }

    final double fraction = source.getCurrentDirection() == Direction.FORWARD ? 0.0 : 1.0;
    this.timingEvent(source, fraction);
  }

  public static <T> Builder<T> forProperty(StatefulProperty<T> property) {
    return new Builder<>(property);
  }

  public static class Builder<T> {

    private final StatefulProperty<T> property;
    private KeyFrames<T> keyFrames;
    private boolean fromCurrent;

    private Builder(StatefulProperty<T> property) {
      this.property = property;
    }

    public Builder<T> to(T value) {
      return this.to(null, value);
    }

    public Builder<T> to(@Nullable T from, T value) {
      if (from == null) {
        this.fromCurrent = true;
      }
      this.keyFrames = new KeyFrames.Builder<T>()
          .addFrame(from == null ? value : from)
          .addFrame(value)
          .build();
      return this;
    }

    public Builder<T> keyFrames(KeyFrames<T> keyFrames) {
      this.keyFrames = keyFrames;
      return this;
    }

    public Animation<T> build() {
      return new Animation<>(this.property, this.keyFrames, this.fromCurrent);
    }
  }
}
