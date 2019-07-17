package com.craftingdead.mod.masterserver.net.protocol.handshake;

import java.util.function.Consumer;
import com.craftingdead.network.pipeline.NetworkManager;
import com.craftingdead.network.protocol.ISession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HandshakeSession implements ISession {

  private final NetworkManager networkManager;
  private final Consumer<NetworkManager> handler;

  @Override
  public void handleConnect() {
    this.handler.accept(this.networkManager);
  }
}
