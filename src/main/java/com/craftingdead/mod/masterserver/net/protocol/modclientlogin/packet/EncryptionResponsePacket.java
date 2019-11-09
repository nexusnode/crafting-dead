package com.craftingdead.mod.masterserver.net.protocol.modclientlogin.packet;

import com.craftingdead.network.protocol.packet.IPacket;
import com.craftingdead.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class EncryptionResponsePacket implements IPacket {

  private byte[] secretKeyEncrypted;
  private byte[] verifyTokenEncrypted;

  public EncryptionResponsePacket(byte[] secretKeyEncrypted, byte[] verifyTokenEncrypted) {
    this.secretKeyEncrypted = secretKeyEncrypted;
    this.verifyTokenEncrypted = verifyTokenEncrypted;
  }

  @Override
  public void encode(ByteBuf out) {
    ByteBufUtil.writeVarInt(out, this.secretKeyEncrypted.length);
    out.writeBytes(this.secretKeyEncrypted);
    ByteBufUtil.writeVarInt(out, this.verifyTokenEncrypted.length);
    out.writeBytes(this.verifyTokenEncrypted);
  }

  @Override
  public void decode(ByteBuf in) {
    this.secretKeyEncrypted = new byte[ByteBufUtil.readVarInt(in)];
    in.readBytes(this.secretKeyEncrypted);
    this.verifyTokenEncrypted = new byte[ByteBufUtil.readVarInt(in)];
    in.readBytes(this.verifyTokenEncrypted);
  }
}
