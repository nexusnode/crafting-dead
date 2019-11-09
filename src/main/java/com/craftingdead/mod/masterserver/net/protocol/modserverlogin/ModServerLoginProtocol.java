package com.craftingdead.mod.masterserver.net.protocol.modserverlogin;

import com.craftingdead.mod.masterserver.net.protocol.modserverlogin.packet.LoginPacket;
import com.craftingdead.network.protocol.packet.PacketProtocol;

public class ModServerLoginProtocol extends PacketProtocol<ModServerLoginSession> {

  public static final ModServerLoginProtocol INSTANCE = new ModServerLoginProtocol();

  private ModServerLoginProtocol() {
    this.registerOutbound(LoginPacket.class, 0x00);
  }
}
