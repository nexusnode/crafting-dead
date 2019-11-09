package com.craftingdead.mod.masterserver.net.protocol.modclientlogin.packet;

import java.util.UUID;
import com.craftingdead.network.protocol.packet.IPacket;
import com.craftingdead.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class LoginStartPacket implements IPacket {

  private UUID playerId;
  private String username;
  private String version;

  public LoginStartPacket(UUID playerId, String username, String version) {
    this.playerId = playerId;
    this.username = username;
    this.version = version;
  }

  @Override
  public void encode(ByteBuf out) {
    ByteBufUtil.writeUniqueId(out, this.playerId);
    ByteBufUtil.writeUtf8(out, this.username);
    ByteBufUtil.writeUtf8(out, this.version);
  }

  @Override
  public void decode(ByteBuf in) {
    this.playerId = ByteBufUtil.readUniqueId(in);
    this.username = ByteBufUtil.readUtf8(in);
    this.version = ByteBufUtil.readUtf8(in);
  }
}
