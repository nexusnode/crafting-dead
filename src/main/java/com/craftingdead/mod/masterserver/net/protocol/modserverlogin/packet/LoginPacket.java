package com.craftingdead.mod.masterserver.net.protocol.modserverlogin.packet;

import java.io.IOException;
import com.craftingdead.network.protocol.packet.IPacket;
import com.craftingdead.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class LoginPacket implements IPacket {

  private String gameHost;
  private int gamePort;

  public LoginPacket(String gameHost, int gamePort) {
    this.gameHost = gameHost;
    this.gamePort = gamePort;
  }

  @Override
  public void encode(ByteBuf out) throws IOException {
    ByteBufUtil.writeUtf8(out, this.gameHost);
    ByteBufUtil.writeVarInt(out, this.gamePort);
  }

  @Override
  public void decode(ByteBuf in) throws IOException {
    this.gameHost = ByteBufUtil.readUtf8(in);
    this.gamePort = ByteBufUtil.readVarInt(in);
  }
}
