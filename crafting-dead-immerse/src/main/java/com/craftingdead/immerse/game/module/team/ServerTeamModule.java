/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
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
