package com.craftingdead.core.game;

import java.util.function.Function;
import java.util.function.Supplier;
import com.craftingdead.core.server.LogicalServer;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class GameType extends ForgeRegistryEntry<GameType> {

  private final Function<LogicalServer, IGameServer<?, ?>> gameServerFactory;
  private final Supplier<IGameClient<?, ?>> gameClientFactory;

  public GameType(Function<LogicalServer, IGameServer<?, ?>> gameServerFactory,
      Supplier<IGameClient<?, ?>> gameClientFactory) {
    this.gameServerFactory = gameServerFactory;
    this.gameClientFactory = gameClientFactory;
  }

  public IGameServer<?, ?> createGameServer(LogicalServer logicalServer) {
    return this.gameServerFactory.apply(logicalServer);
  }

  public IGameClient<?, ?> createGameClient() {
    return this.gameClientFactory.get();
  }
}
