package com.craftingdead.mod.masterserver.net.protocol.handshake;

import com.craftingdead.mod.masterserver.net.protocol.handshake.packet.HandshakePacket;
import com.craftingdead.network.protocol.packet.PacketProtocol;

public class HandshakeProtocol extends PacketProtocol<HandshakeSession> {

  public static final HandshakeProtocol INSTANCE = new HandshakeProtocol();

  private HandshakeProtocol() {
    this.registerOutbound(HandshakePacket.class, 0x00);
  }
}
