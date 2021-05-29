package com.craftingdead.immerse.game;

import com.craftingdead.immerse.game.module.Module;

public class ClientGameWrapper extends GameWrapper<GameClient, Module> {

  public ClientGameWrapper(GameClient game) {
    super(game);
  }
}
