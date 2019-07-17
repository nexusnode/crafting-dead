package com.craftingdead.mod;

import com.craftingdead.network.pipeline.NetworkManager;

public interface IModDist {

  boolean isUsingNativeTransport();

  void handleConnect(NetworkManager networkManager);
}
