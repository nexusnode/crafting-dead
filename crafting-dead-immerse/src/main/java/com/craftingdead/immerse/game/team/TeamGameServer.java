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

package com.craftingdead.immerse.game.team;

import java.util.UUID;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.GameServer;

public interface TeamGameServer<K extends Enum<K> & Team> extends TeamGame<K>, GameServer {

  default void setPlayerTeam(PlayerExtension<?> player, @Nullable TeamInstance<K> team) {
    UUID playerId = player.getEntity().getUUID();
    TeamInstance<K> currentTeam =
        this.getPlayerTeam(player).map(this::getTeamInstance).orElse(null);
    if ((team == null || team != currentTeam) && this.switchTeam(player, currentTeam, team)) {
      if (currentTeam != null) {
        currentTeam.removeMember(playerId);
      }
      if (team != null) {
        team.addMember(playerId);
      }
    }
  }

  boolean switchTeam(PlayerExtension<?> player, @Nullable TeamInstance<K> oldTeam,
      @Nullable TeamInstance<K> newTeam);
}
