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
package com.craftingdead.immerse.game;

import java.util.function.Supplier;
import com.craftingdead.immerse.server.LogicalServer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeCallable;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class GameType extends ForgeRegistryEntry<GameType> {

  private final IGameServerFactory gameServerFactory;
  private final Supplier<SafeCallable<IGameClient<?>>> gameClientFactory;

  public GameType(IGameServerFactory gameServerFactory,
      Supplier<SafeCallable<IGameClient<?>>> gameClientFactory) {
    this.gameServerFactory = gameServerFactory;
    this.gameClientFactory = gameClientFactory;
  }

  public IGameServer<?> createGameServer(LogicalServer logicalServer,
      JsonDeserializationContext deserializationContext, JsonObject json) {
    return this.gameServerFactory.create(logicalServer, deserializationContext, json);
  }

  public IGameClient<?> createGameClient() {
    IGameClient<?> gameClient = DistExecutor.safeCallWhenOn(Dist.CLIENT, this.gameClientFactory);
    if (gameClient == null) {
      throw new IllegalStateException("Attempting to create game client on wrong dist");
    }
    return gameClient;
  }

  @FunctionalInterface
  public static interface IGameServerFactory {
    IGameServer<?> create(LogicalServer logicalServer,
        JsonDeserializationContext deserializationContext, JsonObject json);
  }
}
