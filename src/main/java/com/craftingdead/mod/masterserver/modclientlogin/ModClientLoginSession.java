package com.craftingdead.mod.masterserver.modclientlogin;

import java.math.BigInteger;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.masterserver.common.LoginResponsePacket;
import com.craftingdead.mod.masterserver.modclient.ModClientSession;
import com.craftingdead.mod.masterserver.modclientlogin.packet.EncryptionRequestPacket;
import com.craftingdead.mod.masterserver.modclientlogin.packet.EncryptionResponsePacket;
import com.craftingdead.network.pipeline.NetworkManager;
import com.craftingdead.network.protocol.IProtocol;
import com.craftingdead.network.protocol.ISession;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HTTPUtil;

public class ModClientLoginSession implements ISession {

  private static final Logger logger = LogManager.getLogger();

  private final NetworkManager networkManager;

  public ModClientLoginSession(NetworkManager networkManager) {
    this.networkManager = networkManager;
  }

  public void handleEncryptionRequest(EncryptionRequestPacket packet) {
    SecretKey secretKey = CryptManager.createNewSharedKey();
    PublicKey publicKey = packet.getPublicKey();
    String serverId =
        new BigInteger(CryptManager.getServerIdHash("", publicKey, secretKey)).toString(16);
    EncryptionResponsePacket response =
        new EncryptionResponsePacket(CryptManager.encryptData(publicKey, secretKey.getEncoded()),
            CryptManager.encryptData(publicKey, packet.getVerifyToken()));
    HTTPUtil.DOWNLOADER_EXECUTOR.submit(() -> {
      Minecraft mc = Minecraft.getInstance();
      try {
        mc.getSessionService().joinServer(mc.getSession().getProfile(), mc.getSession().getToken(),
            serverId);
      } catch (AuthenticationException e) {
        this.networkManager.close();
        logger.warn(e);
      }
      this.networkManager.sendMessage(response, (future) -> {
        this.networkManager.enableEncryption(secretKey);
      });
    });
  }

  public void handleLoginResponse(LoginResponsePacket packet) {
    switch (packet.getResponseCode()) {
      case LoginResponsePacket.SUCCESS:
        logger.info("Successfully logged in to master server");
        this.networkManager.setSession(new ModClientSession());
        break;
      case LoginResponsePacket.BAD_LOGIN:
        logger.warn("Bad login");
        CraftingDead.getInstance().setRetryConnect(false);
        this.networkManager.close();
        break;
      case LoginResponsePacket.AUTHENTICATION_UNAVAILABLE:
        logger.info("Authentication unavailable");
        this.networkManager.close();
        break;
      default:
        break;
    }
  }

  @Override
  public IProtocol<?, ?> getProtocol() {
    return ModClientLoginProtocol.INSTANCE;
  }
}
