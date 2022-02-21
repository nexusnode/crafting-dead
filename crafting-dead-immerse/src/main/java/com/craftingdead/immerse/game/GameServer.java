/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

package com.craftingdead.immerse.game;

import java.util.Optional;
import java.util.function.Consumer;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.module.ServerModule;
import com.mojang.serialization.Codec;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerPlayer;

public interface GameServer extends Game {

  Codec<GameServer> CODEC =
      GameType.CODEC.dispatch(GameServer::getType, GameType::getGameServerCodec);

  default void registerServerModules(Consumer<ServerModule> consumer) {};

  default void addPlayer(PlayerExtension<ServerPlayer> player) {}

  default void removePlayer(PlayerExtension<ServerPlayer> player) {}

  /**
   * Determine if the game has finished.
   * 
   * @return true if the game has finished
   */
  boolean isFinished();

  /**
   * Whether persisted player data should be loaded/saved for this game. <br>
   * Returning false will result in clean player data being loaded for all players on the server and
   * any changes to the players will not be saved once the game finishes.<br>
   * Useful for round based games which don't save any data.
   * 
   * @return true if player data should be loaded/saved.
   */
  boolean persistPlayerData();

  /**
   * Get the spawn point for the specified {@link PlayerExtension}.
   * 
   * @param player - the player to get the spawn point for
   * @return an optional spawn point. If no spawn point is returned, the player's default spawn
   *         point will be used.
   */
  default Optional<GlobalPos> getSpawnPoint(PlayerExtension<ServerPlayer> player) {
    return Optional.empty();
  }

  /**
   * Whether this game should be saved.
   * 
   * @return true to save
   */
  boolean persistGameData();
}
