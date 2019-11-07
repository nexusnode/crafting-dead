package com.craftingdead.mod.masterserver.modserverlogin.packet;

import com.craftingdead.network.protocol.packet.IPacket;
import com.craftingdead.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class ModServerLoginPacket implements IPacket {

  private String minecraftHost;
  private int minecraftPort;

  public ModServerLoginPacket(String minecraftHost, int minecraftPort) {
    this.minecraftHost = minecraftHost;
    this.minecraftPort = minecraftPort;
  }

  @Override
  public void encode(ByteBuf out) {
    ByteBufUtil.writeUtf8(out, this.minecraftHost);
    ByteBufUtil.writeVarInt(out, this.minecraftPort);
  }

  @Override
  public void decode(ByteBuf in) {
    this.minecraftHost = ByteBufUtil.readUtf8(in);
    this.minecraftPort = ByteBufUtil.readVarInt(in);
  }
}
