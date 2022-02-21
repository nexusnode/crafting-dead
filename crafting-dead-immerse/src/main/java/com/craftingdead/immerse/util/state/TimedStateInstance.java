/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.util.state;

import java.time.Duration;

public class TimedStateInstance<CTX> extends StateInstance<CTX> {

  private final long durationTicks;
  private long timeRemainingTicks;

  public TimedStateInstance(State<?> state, CTX context, Duration duration) {
    this(state, context, duration.getSeconds() * 20);
  }

  public TimedStateInstance(State<?> state, CTX context, long durationTicks) {
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
