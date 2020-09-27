package com.craftingdead.core;

import com.craftingdead.core.server.LogicalServer;
import net.minecraft.server.MinecraftServer;

public interface IModDist {

  LogicalServer createLogicalServer(MinecraftServer minecraftServer);
}
