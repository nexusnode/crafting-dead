package com.craftingdead.mod.masterserver.common;

import com.craftingdead.network.protocol.packet.IPacket;
import io.netty.buffer.ByteBuf;

public class LoginResponsePacket implements IPacket {

  public static final byte SUCCESS = 0x00;
  public static final byte BAD_LOGIN = 0x01;
  public static final byte AUTHENTICATION_UNAVAILABLE = 0x02;

  private byte responseCode;

  @Override
  public void encode(ByteBuf out) {
    out.writeByte(this.responseCode);
  }

  @Override
  public void decode(ByteBuf in) {
    this.responseCode = in.readByte();
  }

  public byte getResponseCode() {
    return this.responseCode;
  }
}
