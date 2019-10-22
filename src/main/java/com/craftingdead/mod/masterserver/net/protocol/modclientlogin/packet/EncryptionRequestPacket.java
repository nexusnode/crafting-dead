package com.craftingdead.mod.masterserver.net.protocol.modclientlogin.packet;

import java.security.PublicKey;
import com.craftingdead.network.protocol.packet.IPacket;
import com.craftingdead.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.CryptManager;

public class EncryptionRequestPacket implements IPacket {

  private PublicKey publicKey;
  private byte[] verifyToken;

  public EncryptionRequestPacket() {}

  @Override
  public void encode(ByteBuf out) {
    byte[] publicKeyBytes = this.publicKey.getEncoded();
    ByteBufUtil.writeVarInt(out, publicKeyBytes.length);
    out.writeBytes(publicKeyBytes);
    ByteBufUtil.writeVarInt(out, this.verifyToken.length);
    out.writeBytes(this.verifyToken);
  }

  @Override
  public void decode(ByteBuf in) {
    byte[] publicKeyBytes = new byte[ByteBufUtil.readVarInt(in)];
    in.readBytes(publicKeyBytes);
    this.publicKey = CryptManager.decodePublicKey(publicKeyBytes);
    this.verifyToken = new byte[ByteBufUtil.readVarInt(in)];
    in.readBytes(this.verifyToken);
  }

  public PublicKey getPublicKey() {
    return this.publicKey;
  }

  public byte[] getVerifyToken() {
    return this.verifyToken;
  }
}
