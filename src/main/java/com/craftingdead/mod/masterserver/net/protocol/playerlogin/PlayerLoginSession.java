package com.craftingdead.mod.masterserver.net.protocol.playerlogin;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.masterserver.net.protocol.player.PlayerProtocol;
import com.craftingdead.mod.masterserver.net.protocol.player.PlayerSession;
import com.craftingdead.mod.masterserver.net.protocol.playerlogin.message.EncryptionRequestMessage;
import com.craftingdead.mod.masterserver.net.protocol.playerlogin.message.EncryptionResponseMessage;
import com.craftingdead.mod.masterserver.net.protocol.playerlogin.message.LoginResponseMessage;
import com.craftingdead.network.pipeline.NetworkManager;
import com.craftingdead.network.protocol.ISession;
import com.mojang.authlib.exceptions.AuthenticationException;
import java.math.BigInteger;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HTTPUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public class PlayerLoginSession implements ISession {

  private static final Logger logger = LogManager.getLogger();

  private final Minecraft minecraft;

  private final NetworkManager networkManager;

  public void handleEncryptionRequest(EncryptionRequestMessage msg) {
    SecretKey secretkey = CryptManager.createNewSharedKey();
    PublicKey publickey = msg.getPublicKey();
    String serverId =
        new BigInteger(CryptManager.getServerIdHash("", publickey, secretkey)).toString(16);

    EncryptionResponseMessage response =
        new EncryptionResponseMessage(CryptManager.encryptData(publickey, secretkey.getEncoded()),
            CryptManager.encryptData(publickey, msg.getVerifyToken()));
    HTTPUtil.DOWNLOADER_EXECUTOR.submit(() -> {
      try {
        this.minecraft.getSessionService().joinServer(this.minecraft.getSession().getProfile(),
            this.minecraft.getSession().getToken(), serverId);
      } catch (AuthenticationException e) {
        this.networkManager.closeChannel();
        logger.warn(e);
      }

      this.networkManager.sendMessage(response, (future) -> {
        this.networkManager.enableEncryption(secretkey);
      });
    });
  }

  public void handleLoginResponse(LoginResponseMessage msg) {
    switch (msg.getResponseCode()) {
      case LoginResponseMessage.SUCCESS:
        logger.info("Successfully logged in to master server");
        this.networkManager.setProtocol(new PlayerSession(), PlayerProtocol.INSTANCE);
        break;
      case LoginResponseMessage.BAD_LOGIN:
        logger.warn("Bad login");
        CraftingDead.getInstance().setRetryConnect(false);
        this.networkManager.closeChannel();
        break;
      case LoginResponseMessage.AUTHENTICATION_UNAVAILABLE:
        logger.info("Authentication unavailable");
        this.networkManager.closeChannel();
        break;
      default:
        break;
    }
  }
}
