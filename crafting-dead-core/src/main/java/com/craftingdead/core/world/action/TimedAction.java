/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.core.world.action;

import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;

public abstract class TimedAction<T extends ActionType> extends AbstractAction<T> {

  private int durationTicks;

  public TimedAction(T type, LivingExtension<?, ?> performer,
      @Nullable LivingExtension<?, ?> target) {
    super(type, performer, target);
  }

  protected abstract int getTotalDurationTicks();

  @Override
  public boolean start() {
    if (this.getTotalDurationTicks() <= 0) {
      this.finish();
      return false;
    } else {
      return true;
    }
  }

  @Override
  public boolean tick() {
    if (++this.durationTicks >= this.getTotalDurationTicks()) {
      this.finish();
      return true;
    }
    return false;
  }

  protected abstract void finish();

  @Override
  public void cancel() {
    this.durationTicks = 0;
  }

  @Override
  public float getProgress(float partialTicks) {
    return (float) (this.durationTicks + partialTicks) / this.getTotalDurationTicks();
  }
}
