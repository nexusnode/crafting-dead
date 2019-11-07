package com.craftingdead.mod.masterserver.modclient;

import com.craftingdead.network.protocol.packet.PacketProtocol;

public class ModClientProtocol extends PacketProtocol<ModClientSession> {

  public static final ModClientProtocol INSTANCE = new ModClientProtocol();

}
