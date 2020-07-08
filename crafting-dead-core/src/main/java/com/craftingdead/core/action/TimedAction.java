package com.craftingdead.core.action;

import javax.annotation.Nullable;
import com.craftingdead.core.capability.living.ILiving;

public abstract class TimedAction extends AbstractAction {

  private int durationTicks;

  public TimedAction(ActionType<?> actionType, ILiving<?> performer, @Nullable ILiving<?> target) {
    super(actionType, performer, target);
  }

  protected abstract int getTotalDurationTicks();

  @Override
  public boolean tick() {
    if (++this.durationTicks >= this.getTotalDurationTicks()) {
      return true;
    }
    return false;
  }

  @Override
  public void cancel() {
    this.durationTicks = 0;
  }

  @Override
  public float getProgress(float partialTicks) {
    return (float) (this.durationTicks + partialTicks) / this.getTotalDurationTicks();
  }
}
