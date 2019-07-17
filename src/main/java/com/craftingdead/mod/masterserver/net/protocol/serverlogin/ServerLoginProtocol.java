package com.craftingdead.mod.masterserver.net.protocol.serverlogin;

import com.craftingdead.mod.masterserver.net.protocol.serverlogin.message.ServerLoginMessage;
import com.craftingdead.network.protocol.IndexedProtocol;
import com.craftingdead.network.util.ByteBufUtil;

public class ServerLoginProtocol extends IndexedProtocol<ServerLoginSession> {

  public static final ServerLoginProtocol INSTANCE = new ServerLoginProtocol();

  private ServerLoginProtocol() {
    this.registerMessage(ServerLoginMessage.class,
        MessageEntry.<ServerLoginSession, ServerLoginMessage>builder() //
            .id(0x00) //
            .encoder((msg, out) -> {
              ByteBufUtil.writeUtf8(out, msg.getMinecraftHost());
              out.writeInt(msg.getMinecraftPort());
            }) //
            .decoder((in) -> {
              String minecraftHost = ByteBufUtil.readUtf8(in);
              int minecraftPort = in.readInt();
              return new ServerLoginMessage(minecraftHost, minecraftPort);
            }) //
            .build());
  }
}
