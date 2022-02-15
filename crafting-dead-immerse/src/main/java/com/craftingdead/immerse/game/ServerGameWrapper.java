/*
 * Crafting Dead Copyright (C) 2021 NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.game;

import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.module.ServerModule;
import com.craftingdead.immerse.server.LogicalServer;
import net.minecraft.server.level.ServerPlayer;

public final class ServerGameWrapper extends GameWrapper<GameServer, ServerModule> {

  private final LogicalServer logicalServer;

  public ServerGameWrapper(GameServer game, LogicalServer logicalServer) {
    super(game);
    this.logicalServer = logicalServer;
  }

  @Override
  protected void buildModules(GameServer game, ModuleBuilder<ServerModule> builder) {
    game.registerServerModules(builder::registerModule);
  }

  @Override
  public void tick() {
    super.tick();
    if (this.requiresSync()) {
      this.logicalServer.getMinecraftServer().getPlayerList()
          .broadcastAll(this.buildSyncPacket(false));
    }
  }

  public void addPlayer(PlayerExtension<ServerPlayer> player) {
    for (var module : this.modules.values()) {
      module.addPlayer(player);
    }
    this.getGame().addPlayer(player);
    player.getEntity().connection.send(this.buildSyncPacket(true));
  }

  public void removePlayer(PlayerExtension<ServerPlayer> player) {
    this.getGame().removePlayer(player);
    for (var module : this.modules.values()) {
      module.removePlayer(player);
    }
  }
}
