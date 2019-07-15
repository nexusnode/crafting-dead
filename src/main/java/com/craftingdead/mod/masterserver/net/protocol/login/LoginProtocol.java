package com.craftingdead.mod.masterserver.net.protocol.login;

import java.util.UUID;
import com.craftingdead.mod.masterserver.net.protocol.login.message.LoginResultMessage;
import com.craftingdead.mod.masterserver.net.protocol.login.message.PlayerLoginMessage;
import com.craftingdead.network.protocol.IndexedProtocol;
import com.craftingdead.network.util.ByteBufUtil;

public class LoginProtocol extends IndexedProtocol<LoginSession> {

  public LoginProtocol() {
    this.registerMessage(0x00, PlayerLoginMessage.class, (msg, out) -> {
      ByteBufUtil.writeId(out, msg.getId());
      ByteBufUtil.writeUtf8(out, msg.getUsername());
      ByteBufUtil.writeUtf8(out, msg.getClientToken());
      ByteBufUtil.writeUtf8(out, msg.getAccessToken());
      ByteBufUtil.writeUtf8(out, msg.getVersion());
    }, (in) -> {
      UUID id = ByteBufUtil.readId(in);
      String username = ByteBufUtil.readUtf8(in);
      String clientToken = ByteBufUtil.readUtf8(in);
      String accessToken = ByteBufUtil.readUtf8(in);
      String version = ByteBufUtil.readUtf8(in);
      return new PlayerLoginMessage(id, username, clientToken, accessToken, version);
    }, null);

    this.registerMessage(0x01, LoginResultMessage.class,
        (msg, out) -> out.writeBoolean(msg.isSuccess()),
        (in) -> new LoginResultMessage(in.readBoolean()), LoginSession::handleLoginResult);
  }
}
