package com.craftingdead.mod.masterserver.net.protocol.serverlogin;

import com.craftingdead.mod.masterserver.net.protocol.serverlogin.message.ServerLoginMessage;
import com.craftingdead.network.pipeline.NetworkManager;
import com.craftingdead.network.protocol.ISession;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.dedicated.DedicatedServer;

@RequiredArgsConstructor
public class ServerLoginSession implements ISession {

  private final DedicatedServer dedicatedServer;

  private final NetworkManager networkManager;

  @Override
  public void handleConnect() {
    this.networkManager.sendMessage(
        new ServerLoginMessage(this.dedicatedServer.getHostname(), this.dedicatedServer.getPort()));
  }
}
