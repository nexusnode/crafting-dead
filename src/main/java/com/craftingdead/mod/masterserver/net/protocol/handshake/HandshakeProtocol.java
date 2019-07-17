package com.craftingdead.mod.masterserver.net.protocol.handshake;

import com.craftingdead.mod.masterserver.net.protocol.handshake.message.HandshakeMessage;
import com.craftingdead.network.protocol.IndexedProtocol;

public class HandshakeProtocol extends IndexedProtocol<HandshakeSession> {

  public static final HandshakeProtocol INSTANCE = new HandshakeProtocol();

  private HandshakeProtocol() {
    this.registerMessage(HandshakeMessage.class,
        MessageEntry.<HandshakeSession, HandshakeMessage>builder() //
            .id(0x00) //
            .encoder((msg, out) -> out.writeByte(msg.getOpcode())) //
            .decoder((in) -> new HandshakeMessage(in.readByte())) //
            .build());
  }
}
