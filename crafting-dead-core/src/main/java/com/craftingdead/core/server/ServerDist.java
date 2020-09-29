package com.craftingdead.core.server;

import com.craftingdead.core.IModDist;
import net.minecraft.server.MinecraftServer;

public class ServerDist implements IModDist {

  @Override
  public LogicalServer createLogicalServer(MinecraftServer minecraftServer) {
    return new LogicalServer(minecraftServer);
  }
}
