package com.craftingdead.immerse.game.module.team;

import java.util.UUID;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.module.ServerModule;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ServerTeamModule<T extends Enum<T> & Team> extends TeamModule<T>
    implements ServerModule {

  private final TeamHandler<T> teamHandler;

  public ServerTeamModule(Class<T> teamType, TeamHandler<T> teamHandler) {
    super(teamType);
    this.teamHandler = teamHandler;
  }

  public void setPlayerTeam(PlayerExtension<ServerPlayerEntity> player,
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

    boolean canChangeTeam(PlayerExtension<ServerPlayerEntity> player,
        @Nullable TeamInstance<T> oldTeam,
        @Nullable TeamInstance<T> newTeam);

    void teamChanged(PlayerExtension<ServerPlayerEntity> player,
        @Nullable TeamInstance<T> oldTeam,
        @Nullable TeamInstance<T> newTeam);
  }
}
