/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.immerse.game.module.team;

import java.util.UUID;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.module.ServerModule;
import net.minecraft.server.level.ServerPlayer;

public class ServerTeamModule<T extends Enum<T> & Team> extends TeamModule<T>
    implements ServerModule {

  private final TeamHandler<T> teamHandler;

  public ServerTeamModule(Class<T> teamType, TeamHandler<T> teamHandler) {
    super(teamType);
    this.teamHandler = teamHandler;
  }

  public void setPlayerTeam(PlayerExtension<ServerPlayer> player,
      @Nullable TeamInstance<T> teamInstance) {
    UUID playerId = player.getEntity().getUUID();
    TeamInstance<T> oldTeamInstance =
        this.getPlayerTeam(playerId).map(this::getTeamInstance).orElse(null);
    if ((teamInstance == null || teamInstance != oldTeamInstance)
        && this.teamHandler.canChangeTeam(player, oldTeamInstance, teamInstance)) {
      if (oldTeamInstance != null) {
        oldTeamInstance.removeMember(playerId);
      }

      if (teamInstance == null) {
        this.playerTeams.remove(playerId);
      } else {
        this.playerTeams.put(playerId, teamInstance.getTeam());
        teamInstance.addMember(playerId);
      }

      this.dirtyPlayerTeams.put(playerId, teamInstance == null ? null : teamInstance.getTeam());
      this.teamHandler.teamChanged(player, oldTeamInstance, teamInstance);
    }
  }

  public interface TeamHandler<T extends Team> {

    boolean canChangeTeam(PlayerExtension<ServerPlayer> player,
        @Nullable TeamInstance<T> oldTeam,
        @Nullable TeamInstance<T> newTeam);

    void teamChanged(PlayerExtension<ServerPlayer> player,
        @Nullable TeamInstance<T> oldTeam,
        @Nullable TeamInstance<T> newTeam);
  }
}
