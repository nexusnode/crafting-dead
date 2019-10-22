package com.craftingdead.mod.masterserver.net.protocol.handshake;

import java.util.function.Consumer;
import com.craftingdead.network.pipeline.NetworkManager;
import com.craftingdead.network.protocol.IProtocol;
import com.craftingdead.network.protocol.ISession;

public class HandshakeSession implements ISession {

  private final NetworkManager networkManager;
  private final Consumer<NetworkManager> handler;

  public HandshakeSession(NetworkManager networkManager, Consumer<NetworkManager> handler) {
    this.networkManager = networkManager;
    this.handler = handler;
  }

  @Override
  public void handleConnect() {
    this.handler.accept(this.networkManager);
  }

  @Override
  public IProtocol<?, ?> getProtocol() {
    return HandshakeProtocol.INSTANCE;
  }
}
