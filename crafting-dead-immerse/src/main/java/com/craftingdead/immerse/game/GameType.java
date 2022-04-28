/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.game;

import java.util.function.Supplier;
import com.craftingdead.immerse.game.network.NetworkProtocol;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeCallable;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class GameType extends ForgeRegistryEntry<GameType> {

  public static final Codec<GameType> CODEC =
      ExtraCodecs.lazyInitializedCodec(() -> GameTypes.registry.get().getCodec());

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
