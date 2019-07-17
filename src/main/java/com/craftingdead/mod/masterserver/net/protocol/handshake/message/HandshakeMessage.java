package com.craftingdead.mod.masterserver.net.protocol.handshake.message;

import com.craftingdead.network.protocol.IMessage;
import lombok.Data;

@Data
public class HandshakeMessage implements IMessage {

  public static final byte PLAYER_LOGIN = 0x00;
  public static final byte SERVER_LOGIN = 0x01;

  private final byte opcode;
}
