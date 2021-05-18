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
import com.craftingdead.core.network.BufferSerializable;
import com.craftingdead.immerse.game.network.NetworkProtocol;
import com.craftingdead.immerse.game.shop.Shop;

public interface Game extends BufferSerializable {

  /**
   * A {@link NetworkProtocol} which allows games to send and receive custom packets.
   * 
   * @return the {@link NetworkProtocol}
   * @see com.craftingdead.immerse.game.network.SimpleNetworkProtocol
   */
  default NetworkProtocol getNetworkProtocol() {
    return NetworkProtocol.EMPTY;
  }

  default Optional<Shop> getShop() {
    return Optional.empty();
  }

  default Shop getExpectedShop() {
    return this.getShop().orElseThrow(() -> new IllegalStateException("Expecting shop"));
  }

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
  GameType getGameType();
}
