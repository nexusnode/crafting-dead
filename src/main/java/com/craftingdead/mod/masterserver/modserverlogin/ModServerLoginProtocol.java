package com.craftingdead.mod.masterserver.modserverlogin;

import com.craftingdead.mod.masterserver.modserverlogin.packet.ModServerLoginPacket;
import com.craftingdead.network.protocol.packet.PacketProtocol;

public class ModServerLoginProtocol extends PacketProtocol<ModServerLoginSession> {

  public static final ModServerLoginProtocol INSTANCE = new ModServerLoginProtocol();

  private ModServerLoginProtocol() {
    this.registerOutbound(ModServerLoginPacket.class, 0x00);
  }
}
