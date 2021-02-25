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

package com.craftingdead.immerse.game.state;

import java.time.Duration;

public class TimedStateInstance<CTX> extends StateInstance<CTX> {

  private final long durationTicks;
  private long timeRemainingTicks;

  public TimedStateInstance(IState<?> state, CTX context, Duration duration) {
    this(state, context, duration.getSeconds() * 20);
  }

  public TimedStateInstance(IState<?> state, CTX context, long durationTicks) {
    super(state, context);
    this.durationTicks = durationTicks;
    this.timeRemainingTicks = durationTicks;
  }

  @Override
  protected boolean tick() {
    if (super.tick()) {
      return true;
    }
    boolean finished = this.timeRemainingTicks <= 0;
    if (finished) {
      return true;
    }
    this.timeRemainingTicks--;
    return false;
  }

  public boolean hasSecondPast() {
    return this.getTimeRemainingTicks() % 20.0F == 0.0F;
  }

  public long getTimeRemainingTicks() {
    return this.timeRemainingTicks;
  }

  public void setTimeRemainingTicks(long timeRemainingTicks) {
    this.timeRemainingTicks = timeRemainingTicks;
  }

  public long getTimeRemainingSeconds() {
    return this.getTimeRemainingTicks() / 20L;
  }

  public void setTimeRemainingSeconds(long timeRemainingSeconds) {
    this.setTimeRemainingTicks(timeRemainingSeconds * 20L);
  }

  public long getTimeElapsedTicks() {
    return this.durationTicks - this.timeRemainingTicks;
  }

  public long getTimeElapsedSeconds() {
    return this.getTimeElapsedTicks() / 20L;
  }
}
