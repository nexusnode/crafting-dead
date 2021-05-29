package com.craftingdead.immerse.game;

import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.module.ServerModule;
import com.craftingdead.immerse.server.LogicalServer;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ServerGameWrapper extends GameWrapper<GameServer, ServerModule> {

  private final LogicalServer logicalServer;

  public ServerGameWrapper(GameServer game, LogicalServer logicalServer) {
    super(game);
    this.logicalServer = logicalServer;
  }

  @Override
  public void tick() {
    super.tick();
    if (this.requiresSync()) {
      this.logicalServer.getMinecraftServer().getPlayerList()
          .broadcastAll(this.buildSyncPacket(false));
    }
  }

  public void addPlayer(PlayerExtension<ServerPlayerEntity> player) {
    for (ServerModule module : this.modules.values()) {
      module.addPlayer(player);
    }
    this.getGame().addPlayer(player);
    player.getEntity().connection.send(this.buildSyncPacket(true));
  }

  public void removePlayer(PlayerExtension<ServerPlayerEntity> player) {
    this.getGame().removePlayer(player);
    for (ServerModule module : this.modules.values()) {
      module.removePlayer(player);
    }
  }
}
