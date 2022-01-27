package com.craftingdead.core.client.animation;

public abstract class TimedAnimation implements Animation {

  private final int lifetimeTicks;
  private int ageTicks;
  private boolean removed;

  public TimedAnimation(int lifetimeTicks) {
    this.lifetimeTicks = lifetimeTicks;
  }

  @Override
  public void tick() {
    if (this.ageTicks++ >= this.lifetimeTicks) {
      this.remove();
    }
  }

  @Override
  public void remove() {
    this.removed = true;
  }

  public int getAgeTicks() {
    return this.ageTicks;
  }

  public float lerpProgress(float partialTicks) {
    return Math.min((float) (this.ageTicks + partialTicks) / (float) this.lifetimeTicks, 1.0F);
  }

  @Override
  public boolean isAlive() {
    return !this.removed;
  }
}
