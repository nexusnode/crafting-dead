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

import java.util.EnumMap;
import java.util.Map;
import com.craftingdead.core.util.IBufferSerializable;
import net.minecraft.network.PacketBuffer;

public abstract class AbstractTeamGame<K extends Enum<K> & ITeam> implements ITeamGame<K> {

  private final Class<K> teamType;
  private final Map<K, TeamInstance<K>> teams;

  public AbstractTeamGame(Class<K> teamType) {
    this.teamType = teamType;
    this.teams = new EnumMap<>(teamType);
  }

  public void registerTeam(K team) {
    this.teams.put(team, new TeamInstance<>(team));
  }

  public TeamInstance<K> getSmallestTeam() {
    TeamInstance<K> smallestTeam = null;
    for (TeamInstance<K> team : this.teams.values()) {
      if (smallestTeam == null || team.getMembers().size() < smallestTeam.getMembers().size()) {
        smallestTeam = team;
      }
    }
    return smallestTeam;
  }

  @Override
  public TeamInstance<K> getTeamInstance(K team) {
    return this.teams.get(team);
  }

  @Override
  public void encode(PacketBuffer packetBuffer, boolean writeAll) {
    for (Map.Entry<K, TeamInstance<K>> entry : this.teams.entrySet()) {
      if (writeAll || entry.getValue().requiresSync()) {
        packetBuffer.writeVarInt(entry.getKey().ordinal());
        entry.getValue().encode(packetBuffer, writeAll);
      }
    }
    packetBuffer.writeVarInt(-1);
  }

  @Override
  public void decode(PacketBuffer packetBuffer) {
    int i;
    while ((i = packetBuffer.readVarInt()) != -1) {
      K key = this.teamType.getEnumConstants()[i];
      TeamInstance<K> team = this.teams.get(key);
      if (team == null) {
        throw new IllegalStateException("Team '" + key.toString() + "' missing on client");
      }
      team.decode(packetBuffer);
    }
  }

  @Override
  public boolean requiresSync() {
    return this.teams.values().stream().anyMatch(IBufferSerializable::requiresSync);
  }
}
