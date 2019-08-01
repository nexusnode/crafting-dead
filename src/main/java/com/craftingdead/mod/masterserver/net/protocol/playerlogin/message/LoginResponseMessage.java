package com.craftingdead.mod.masterserver.net.protocol.playerlogin.message;

import java.io.IOException;
import com.craftingdead.network.protocol.IMessage;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResponseMessage implements IMessage {

  private final Response response;

  public LoginResponseMessage(ByteBuf in) throws IOException {
    this.response = Response.fromCode(in.readByte());
  }

  public void encode(ByteBuf out) {
    out.writeByte(response.code);
  }

  @RequiredArgsConstructor
  public enum Response {
    SUCCESS((byte) 0x00), BAD_LOGIN((byte) 0x01), AUTHENTICATION_UNAVAILABLE(
        (byte) 0x02);

    private final byte code;

    public static Response fromCode(byte code) {
      for (Response response : values()) {
        if (response.code == code) {
          return response;
        }
      }
      return null;
    }
  }
}
