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
