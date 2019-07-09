package com.craftingdead.mod.server;

import com.craftingdead.mod.IModDist;
import com.craftingdead.network.protocol.IMessage;

public class ServerDist implements IModDist {

  @Override
  public IMessage getLoginMessage() {
    return null;
  }
}
