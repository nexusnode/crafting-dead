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
package com.craftingdead.core.server;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.capability.living.Player;
import com.craftingdead.core.game.IGameServer;
import com.craftingdead.core.game.ITeam;
import com.craftingdead.core.network.message.login.SetupGameMessage;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;

public class GameManager<T extends ITeam, P extends Player<? extends ServerPlayerEntity>> {

  private final IGameServer<T, P> gameServer;
  private Set<ServerPlayerEntity> waitingForTeam = new ReferenceOpenHashSet<>();

  public GameManager(IGameServer<T, P> gameServer) {
    this.gameServer = gameServer;
  }

  public boolean initializeConnectionToPlayer(NetworkManager networkManager,
      ServerPlayerEntity playerEntity) {
    T team = this.gameServer.getDefaultTeam().orElse(null);
    if (team == null) {
      // NetworkChannel.PLAY.getSimpleChannel().sendTo(new SelectTeamMessage(), networkManager,
      // NetworkDirection.PLAY_TO_CLIENT);
      this.waitingForTeam.add(playerEntity);
      return true;
    } else {
      this.gameServer.setTeam(Player.get(playerEntity), team);
    }
    return false;
  }

  public List<Pair<String, SetupGameMessage>> generateSetupGameMessage(boolean isLocal) {
    return Collections.singletonList(Pair.of(SetupGameMessage.class.getName(),
        new SetupGameMessage(this.gameServer.getGameType())));
  }

  public IGameServer<T, P> getGameServer() {
    return this.gameServer;
  }
}
