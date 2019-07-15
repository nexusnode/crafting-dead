package com.craftingdead.mod.masterserver.net.protocol.login;

import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.masterserver.net.protocol.login.message.LoginResultMessage;
import com.craftingdead.mod.masterserver.net.protocol.login.message.PlayerLoginMessage;
import com.craftingdead.mod.masterserver.net.protocol.player.PlayerSession;
import com.craftingdead.network.pipeline.NetworkManager;
import com.craftingdead.network.protocol.ISession;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

@RequiredArgsConstructor
public class LoginSession implements ISession {

  private static final Logger logger = LogManager.getLogger();

  private static final Minecraft minecraft = Minecraft.getInstance();

  private final NetworkManager networkManager;

  private final ClientDist client;

  @Override
  public void handleConnect() {
    Session session = minecraft.getSession();
    UUID id = session.getProfile().getId();
    String username = session.getUsername();
    String clientToken = ((YggdrasilMinecraftSessionService) minecraft.getSessionService())
        .getAuthenticationService().getClientToken();
    String accessToken = session.getToken();
    this.networkManager.sendMessage(
        new PlayerLoginMessage(id, username, clientToken, accessToken, CraftingDead.MOD_VERSION));
  }

  public void handleLoginResult(LoginResultMessage msg) {
    if (msg.isSuccess()) {
      logger.info("Successfully logged in to master server");
      this.networkManager.setProtocol(new PlayerSession(), this.client.getPlayerProtocol());
    } else {
      logger.info("Failed to log in to master server, disconnecting");
      this.networkManager.closeChannel();
    }
  }
}
