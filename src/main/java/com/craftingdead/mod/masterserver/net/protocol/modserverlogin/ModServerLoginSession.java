package com.craftingdead.mod.masterserver.net.protocol.modserverlogin;

import com.craftingdead.mod.masterserver.net.protocol.modserverlogin.packet.LoginPacket;
import com.craftingdead.network.pipeline.NetworkManager;
import com.craftingdead.network.protocol.IProtocol;
import com.craftingdead.network.protocol.ISession;
import net.minecraft.server.dedicated.DedicatedServer;

public class ModServerLoginSession implements ISession {

  private final NetworkManager networkManager;

  private final DedicatedServer dedicatedServer;

  public ModServerLoginSession(NetworkManager networkManager, DedicatedServer dedicatedServer) {
    this.networkManager = networkManager;
    this.dedicatedServer = dedicatedServer;
  }

  @Override
  public void handleLoad() {
    this.networkManager.sendMessage(
        new LoginPacket(this.dedicatedServer.getHostname(), this.dedicatedServer.getPort()));
  }

  @Override
  public IProtocol<?, ?> getProtocol() {
    return ModServerLoginProtocol.INSTANCE;
  }
}
