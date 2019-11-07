package com.craftingdead.mod.masterserver.handshake.packet;

import com.craftingdead.network.protocol.packet.IPacket;
import com.craftingdead.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class HandshakePacket implements IPacket {

  public static final byte MOD_CLIENT_LOGIN = 0x00;
  public static final byte MOD_SERVER_LOGIN = 0x01;

  private String version;
  private byte opcode;

  public HandshakePacket(String version, byte opcode) {
    this.version = version;
    this.opcode = opcode;
  }

  @Override
  public void encode(ByteBuf out) {
    ByteBufUtil.writeUtf8(out, this.version);
    out.writeByte(this.opcode);
  }

  @Override
  public void decode(ByteBuf in) {
    this.version = ByteBufUtil.readUtf8(in);
    this.opcode = in.readByte();
  }
}
