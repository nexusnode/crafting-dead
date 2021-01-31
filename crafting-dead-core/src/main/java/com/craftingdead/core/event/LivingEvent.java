package com.craftingdead.core.event;

import com.craftingdead.core.capability.living.ILiving;
import net.minecraftforge.eventbus.api.Event;

public abstract class LivingEvent extends Event {

  private final ILiving<?, ?> living;

  public LivingEvent(ILiving<?, ?> living) {
    this.living = living;
  }

  public ILiving<?, ?> getLiving() {
    return this.living;
  }

  public static class Load extends LivingEvent {

    public Load(ILiving<?, ?> living) {
      super(living);
    }
  }
}
