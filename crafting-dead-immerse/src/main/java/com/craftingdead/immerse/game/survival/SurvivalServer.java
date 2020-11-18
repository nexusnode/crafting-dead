package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.capability.living.Player;
import com.craftingdead.immerse.game.IGameServer;
import com.craftingdead.immerse.server.LogicalServer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;

public class SurvivalServer extends SurvivalGame implements IGameServer<SurvivorsTeam> {

  public SurvivalServer(LogicalServer logicalServer) {}

  @Override
  public void initializeConnectionToPlayer(NetworkManager networkManager,
      Player<ServerPlayerEntity> player) {}

}
