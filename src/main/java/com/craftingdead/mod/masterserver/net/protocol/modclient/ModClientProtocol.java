package com.craftingdead.mod.masterserver.net.protocol.modclient;

import com.craftingdead.network.protocol.packet.PacketProtocol;

public class ModClientProtocol extends PacketProtocol<ModClientSession> {

  public static final ModClientProtocol INSTANCE = new ModClientProtocol();

  private ModClientProtocol() {

  }
}
