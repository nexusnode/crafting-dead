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
