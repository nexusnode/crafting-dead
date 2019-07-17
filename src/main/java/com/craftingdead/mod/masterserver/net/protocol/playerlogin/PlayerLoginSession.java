package com.craftingdead.mod.masterserver.net.protocol.playerlogin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.mod.masterserver.net.protocol.player.PlayerProtocol;
import com.craftingdead.mod.masterserver.net.protocol.player.PlayerSession;
import com.craftingdead.mod.masterserver.net.protocol.playerlogin.message.PlayerLoginResultMessage;
import com.craftingdead.network.pipeline.NetworkManager;
import com.craftingdead.network.protocol.ISession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerLoginSession implements ISession {

  private static final Logger logger = LogManager.getLogger();

  private final NetworkManager networkManager;

  public void handleLoginResult(PlayerLoginResultMessage msg) {
    if (msg.isSuccess()) {
      logger.info("Successfully logged in to master server");
      this.networkManager.setProtocol(new PlayerSession(), PlayerProtocol.INSTANCE);
    } else {
      logger.info("Failed to log in to master server, disconnecting");
      this.networkManager.closeChannel();
    }
  }
}
