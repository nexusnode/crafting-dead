package com.craftingdead.immerse.client.gui.screen.menu.play.list.server;

import net.minecraft.network.chat.Component;

public class PingError extends Exception {

  private static final long serialVersionUID = -2943174397828675088L;

  private final Component description;

  public PingError(Component description) {
    super(description.getString());
    this.description = description;
  }

  public Component getDescription() {
    return this.description;
  }
}
