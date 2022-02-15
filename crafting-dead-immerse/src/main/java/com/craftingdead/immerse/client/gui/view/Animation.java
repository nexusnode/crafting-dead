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

import javax.annotation.Nullable;
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
