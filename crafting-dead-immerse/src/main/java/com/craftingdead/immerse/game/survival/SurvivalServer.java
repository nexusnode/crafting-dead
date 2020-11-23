/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
