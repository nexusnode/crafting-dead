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

import java.util.Optional;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.module.ServerModule;
import com.mojang.serialization.Codec;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface GameServer extends Game<ServerModule> {

  Codec<GameServer> CODEC =
      GameType.CODEC.dispatch(GameServer::getType, GameType::getGameServerCodec);

  default boolean disableBlockBurning() {
    return false;
  }

  default void addPlayer(PlayerExtension<ServerPlayerEntity> player) {}

  default void removePlayer(PlayerExtension<ServerPlayerEntity> player) {}

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
  default Optional<SpawnPoint> getSpawnPoint(PlayerExtension<ServerPlayerEntity> player) {
    return Optional.empty();
  }

  /**
   * Whether this game should be saved.
   * 
   * @return true to save
   */
  boolean save();
}
