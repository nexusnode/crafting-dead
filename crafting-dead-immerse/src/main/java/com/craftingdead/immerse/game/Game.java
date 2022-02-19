/*
 * Crafting Dead Copyright (C) 2021 NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.game;

import com.craftingdead.core.network.Synched;
import net.minecraftforge.network.NetworkEvent;

public interface Game extends Synched {

  /**
   * Handle messages sent to this game.
   * 
   * @param <MSG> - type of message
   * @param message - the message
   * @param context - sender context
   */
  default <MSG> void handleMessage(MSG message, NetworkEvent.Context context) {}

  /**
   * Called once when the game is created to perform initial loading.
   */
  default void load() {}

  /**
   * Called once after the game is changed or server is shutdown.
   */
  default void unload() {}

  /**
   * Called before each 'round' therefore <i>can</i> be called multiple times.
   */
  default void started() {}

  /**
   * Called after each 'round' therefore <i>can</i> be called multiple times.
   */
  default void ended() {}

  /**
   * Perform any updates.
   */
  default void tick() {}

  default boolean disableBlockBurning() {
    return false;
  }

  /**
   * Get the {@link GameType} associated with this {@link Game} instance.
   * 
   * @return the {@link GameType}
   */
  GameType getType();
}
