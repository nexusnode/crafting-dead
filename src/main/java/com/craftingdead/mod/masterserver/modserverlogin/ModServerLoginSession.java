package com.craftingdead.mod.masterserver.modserverlogin;

import com.craftingdead.mod.masterserver.modserverlogin.packet.ModServerLoginPacket;
import com.craftingdead.network.pipeline.NetworkManager;
import com.craftingdead.network.protocol.IProtocol;
import com.craftingdead.network.protocol.ISession;
import net.minecraft.server.dedicated.DedicatedServer;

public class ModServerLoginSession implements ISession {

  private final NetworkManager networkManager;

  private final DedicatedServer dedicatedServer;

  public ModServerLoginSession(NetworkManager networkManager, DedicatedServer dedicatedServer) {
    this.dedicatedServer = dedicatedServer;
    this.networkManager = networkManager;
  }

  @Override
  public void handleLoad() {
    this.networkManager.sendMessage(new ModServerLoginPacket(this.dedicatedServer.getHostname(),
        this.dedicatedServer.getPort()));
  }

  @Override
  public IProtocol<?, ?> getProtocol() {
    return ModServerLoginProtocol.INSTANCE;
  }
}
