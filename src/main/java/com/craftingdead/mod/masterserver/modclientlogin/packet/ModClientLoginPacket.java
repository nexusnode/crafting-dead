package com.craftingdead.mod.masterserver.modclientlogin.packet;

import java.util.UUID;
import com.craftingdead.network.protocol.packet.IPacket;
import com.craftingdead.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class ModClientLoginPacket implements IPacket {

  private UUID playerId;
  private String playerName;
  private String modVersion;

  public ModClientLoginPacket(UUID playerId, String playerName, String modVersion) {
    this.playerId = playerId;
    this.playerName = playerName;
    this.modVersion = modVersion;
  }

  @Override
  public void encode(ByteBuf out) {
    ByteBufUtil.writeUniqueId(out, this.playerId);
    ByteBufUtil.writeUtf8(out, this.playerName);
    ByteBufUtil.writeUtf8(out, this.modVersion);
  }

  @Override
  public void decode(ByteBuf in) {
    this.playerId = ByteBufUtil.readUniqueId(in);
    this.playerName = ByteBufUtil.readUtf8(in);
    this.modVersion = ByteBufUtil.readUtf8(in);
  }
}
