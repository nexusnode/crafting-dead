package com.craftingdead.mod.masterserver.net.protocol.handshake.packet;

import com.craftingdead.network.protocol.packet.IPacket;
import io.netty.buffer.ByteBuf;

public class HandshakePacket implements IPacket {

  public static final byte PLAYER_LOGIN = 0x00;
  public static final byte SERVER_LOGIN = 0x01;

  private byte opcode;

  public HandshakePacket(byte opcode) {
    this.opcode = opcode;
  }

  public void encode(ByteBuf out) {
    out.writeByte(this.opcode);
  }

  @Override
  public void decode(ByteBuf in) {
    this.opcode = in.readByte();
  }
}
