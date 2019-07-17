package com.craftingdead.mod.masterserver.net.protocol.player;

import com.craftingdead.network.protocol.IndexedProtocol;

public class PlayerProtocol extends IndexedProtocol<PlayerSession> {

  public static final PlayerProtocol INSTANCE = new PlayerProtocol();

  private PlayerProtocol() {

  }
}
