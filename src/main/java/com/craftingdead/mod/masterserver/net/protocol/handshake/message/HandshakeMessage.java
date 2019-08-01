package com.craftingdead.mod.masterserver.net.protocol.handshake.message;

import com.craftingdead.network.protocol.IMessage;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class HandshakeMessage implements IMessage {

  public static final byte PLAYER_LOGIN = 0x00;
  public static final byte SERVER_LOGIN = 0x01;

  private final byte opcode;

  public HandshakeMessage(ByteBuf in) {
    this.opcode = in.readByte();
  }

  public void encode(ByteBuf out) {
    out.writeByte(this.opcode);
  }
}
