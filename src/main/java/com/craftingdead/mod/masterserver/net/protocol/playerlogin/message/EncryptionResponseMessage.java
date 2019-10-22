package com.craftingdead.mod.masterserver.net.protocol.playerlogin.message;

import com.craftingdead.network.protocol.IMessage;
import com.craftingdead.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EncryptionResponseMessage implements IMessage {

  private final byte[] secretKeyEncrypted;
  private final byte[] verifyTokenEncrypted;

  public EncryptionResponseMessage(ByteBuf in) throws IOException {
    this.secretKeyEncrypted = new byte[ByteBufUtil.readVarInt(in)];
    in.readBytes(this.secretKeyEncrypted);
    this.verifyTokenEncrypted = new byte[ByteBufUtil.readVarInt(in)];
    in.readBytes(this.verifyTokenEncrypted);
  }

  public void encode(ByteBuf out) {
    ByteBufUtil.writeVarInt(out, this.secretKeyEncrypted.length);
    out.writeBytes(this.secretKeyEncrypted);
    ByteBufUtil.writeVarInt(out, this.verifyTokenEncrypted.length);
    out.writeBytes(this.verifyTokenEncrypted);
  }
}
