package com.craftingdead.mod.masterserver.net.protocol.playerlogin;

import com.craftingdead.mod.masterserver.net.protocol.playerlogin.message.EncryptionRequestMessage;
import com.craftingdead.mod.masterserver.net.protocol.playerlogin.message.EncryptionResponseMessage;
import com.craftingdead.mod.masterserver.net.protocol.playerlogin.message.LoginResponseMessage;
import com.craftingdead.mod.masterserver.net.protocol.playerlogin.message.PlayerLoginStartMessage;
import com.craftingdead.network.protocol.IndexedProtocol;

public class PlayerLoginProtocol extends IndexedProtocol<PlayerLoginSession> {

  public static final PlayerLoginProtocol INSTANCE = new PlayerLoginProtocol();

  private PlayerLoginProtocol() {
    this.registerMessage(PlayerLoginStartMessage.class,
        MessageEntry.<PlayerLoginSession, PlayerLoginStartMessage>builder()
            .id(0x00)
            .encoder(PlayerLoginStartMessage::encode)
            .decoder(PlayerLoginStartMessage::new)
            .build());

    this.registerMessage(EncryptionRequestMessage.class,
        MessageEntry.<PlayerLoginSession, EncryptionRequestMessage>builder()
            .id(0x01)
            .encoder(EncryptionRequestMessage::encode)
            .decoder(EncryptionRequestMessage::new)
            .handler(PlayerLoginSession::handleEncryptionRequest)
            .build());

    this.registerMessage(EncryptionResponseMessage.class,
        MessageEntry.<PlayerLoginSession, EncryptionResponseMessage>builder()
            .id(0x02)
            .encoder(EncryptionResponseMessage::encode)
            .decoder(EncryptionResponseMessage::new)
            .build());

    this.registerMessage(LoginResponseMessage.class,
        MessageEntry.<PlayerLoginSession, LoginResponseMessage>builder()
            .id(0x03)
            .encoder(LoginResponseMessage::encode)
            .decoder(LoginResponseMessage::new)
            .handler(PlayerLoginSession::handleLoginResponse)
            .build());
  }
}
