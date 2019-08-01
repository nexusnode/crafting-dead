package com.craftingdead.mod.masterserver.net.protocol.playerlogin.message;

import com.craftingdead.network.protocol.IMessage;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResponseMessage implements IMessage {

  public static final byte SUCCESS = 0x00;
  public static final byte BAD_LOGIN = 0x01;
  public static final byte AUTHENTICATION_UNAVAILABLE = 0x02;

  private final byte responseCode;

  public LoginResponseMessage(ByteBuf in) {
    this.responseCode = in.readByte();
  }

  public void encode(ByteBuf out) {
    out.writeByte(this.responseCode);
  }
}
