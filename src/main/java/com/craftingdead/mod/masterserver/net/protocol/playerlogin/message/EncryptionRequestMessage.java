package com.craftingdead.mod.masterserver.net.protocol.playerlogin.message;

import com.craftingdead.network.protocol.IMessage;
import com.craftingdead.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.security.PublicKey;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.CryptManager;

@Data
@RequiredArgsConstructor
public class EncryptionRequestMessage implements IMessage {

  private final PublicKey publicKey;
  private final byte[] verifyToken;

  public EncryptionRequestMessage(ByteBuf in) throws IOException {
    byte[] publicKeyBytes = new byte[ByteBufUtil.readVarInt(in)];
    in.readBytes(publicKeyBytes);
    this.publicKey = CryptManager.decodePublicKey(publicKeyBytes);
    this.verifyToken = new byte[ByteBufUtil.readVarInt(in)];
    in.readBytes(this.verifyToken);
  }

  public void encode(ByteBuf out) {
    byte[] publicKeyBytes = this.publicKey.getEncoded();
    ByteBufUtil.writeVarInt(out, publicKeyBytes.length);
    out.writeBytes(publicKeyBytes);
    ByteBufUtil.writeVarInt(out, this.verifyToken.length);
    out.writeBytes(this.verifyToken);
  }
}
