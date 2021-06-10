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

import java.util.function.Consumer;
import com.craftingdead.core.network.BufferSerializable;
import com.craftingdead.immerse.game.module.Module;
import net.minecraftforge.fml.network.NetworkEvent;

public interface Game<M extends Module> extends BufferSerializable {

  default void registerModules(Consumer<M> registar) {};

  /**
   * Handle messages sent to this game.
   * 
   * @param <MSG> - type of message
   * @param message - the message
   * @param context - sender context
   */
  default <MSG> void handleMessage(MSG message, NetworkEvent.Context context) {}

  /**
   * Prepare the game to be played.
   */
  default void load() {}

  /**
   * Unload and cleanup after the game finishes.
   */
  default void unload() {}

  /**
   * Perform any updates.
   */
  void tick();

  /**
   * Get the {@link GameType} associated with this {@link Game} instance.
   * 
   * @return the {@link GameType}
   */
  GameType getType();
}
