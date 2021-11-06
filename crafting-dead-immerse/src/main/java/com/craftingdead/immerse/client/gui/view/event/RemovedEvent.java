package com.craftingdead.immerse.client.gui.view.event;

import net.minecraftforge.eventbus.api.Event;

public abstract class RemovedEvent extends Event {

  @HasResult
  public static class Pre extends RemovedEvent {

    private final Runnable remove;

    public Pre(Runnable remove) {
      this.remove = remove;
    }

    public Runnable getRemove() {
      return this.remove;
    }
  }

  public static class Post extends RemovedEvent {}
}
