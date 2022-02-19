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

package com.craftingdead.immerse.game;

import java.util.function.Supplier;
import com.craftingdead.immerse.game.network.NetworkProtocol;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeCallable;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class GameType extends ForgeRegistryEntry<GameType> {

  public static final Codec<GameType> CODEC =
      ResourceLocation.CODEC.flatXmap(registryName -> {
        var gameType = GameTypes.registry.get().getValue(registryName);
        return gameType == null
            ? DataResult.error("Unknown registry key: " + registryName.toString())
            : DataResult.success(gameType);
      }, gameType -> DataResult.success(gameType.getRegistryName()));

  private final Codec<? extends GameServer> gameServerCodec;
  private final Supplier<SafeCallable<GameClient>> gameClientFactory;

  private final NetworkProtocol networkProtocol;

  public GameType(Codec<? extends GameServer> gameServerCodec,
      Supplier<SafeCallable<GameClient>> gameClientFactory,
      NetworkProtocol networkProtocol) {
    this.gameServerCodec = gameServerCodec;
    this.gameClientFactory = gameClientFactory;
    this.networkProtocol = networkProtocol;
  }

  public Codec<? extends GameServer> getGameServerCodec() {
    return this.gameServerCodec;
  }

  public GameClient createGameClient() {
    GameClient gameClient = DistExecutor.safeCallWhenOn(Dist.CLIENT, this.gameClientFactory);
    if (gameClient == null) {
      throw new IllegalStateException("Attempting to create game client on a server!");
    }
    return gameClient;
  }

  public NetworkProtocol getNetworkProtocol() {
    return this.networkProtocol;
  }

  public Component getDisplayName() {
    return new TranslatableComponent(Util.makeDescriptionId("game", this.getRegistryName()));
  }
}
