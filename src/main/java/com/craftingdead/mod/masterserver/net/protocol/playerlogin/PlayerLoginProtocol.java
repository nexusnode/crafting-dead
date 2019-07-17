package com.craftingdead.mod.masterserver.net.protocol.playerlogin;

import java.util.UUID;
import com.craftingdead.mod.masterserver.net.protocol.playerlogin.message.PlayerLoginMessage;
import com.craftingdead.mod.masterserver.net.protocol.playerlogin.message.PlayerLoginResultMessage;
import com.craftingdead.network.protocol.IndexedProtocol;
import com.craftingdead.network.util.ByteBufUtil;

public class PlayerLoginProtocol extends IndexedProtocol<PlayerLoginSession> {

  public static final PlayerLoginProtocol INSTANCE = new PlayerLoginProtocol();

  private PlayerLoginProtocol() {
    this.registerMessage(PlayerLoginMessage.class,
        MessageEntry.<PlayerLoginSession, PlayerLoginMessage>builder() //
            .id(0x00) //
            .encoder((msg, out) -> {
              ByteBufUtil.writeId(out, msg.getId());
              ByteBufUtil.writeUtf8(out, msg.getUsername());
              ByteBufUtil.writeUtf8(out, msg.getClientToken());
              ByteBufUtil.writeUtf8(out, msg.getAccessToken());
              ByteBufUtil.writeUtf8(out, msg.getVersion());
            }) //
            .decoder((in) -> {
              UUID id = ByteBufUtil.readId(in);
              String username = ByteBufUtil.readUtf8(in);
              String clientToken = ByteBufUtil.readUtf8(in);
              String accessToken = ByteBufUtil.readUtf8(in);
              String version = ByteBufUtil.readUtf8(in);
              return new PlayerLoginMessage(id, username, clientToken, accessToken, version);
            }) //
            .build());

    this.registerMessage(PlayerLoginResultMessage.class,
        MessageEntry.<PlayerLoginSession, PlayerLoginResultMessage>builder() //
            .id(0x01) //
            .encoder((msg, out) -> out.writeBoolean(msg.isSuccess())) //
            .decoder((in) -> new PlayerLoginResultMessage(in.readBoolean())) //
            .handler(PlayerLoginSession::handleLoginResult) //
            .build());
  }
}
