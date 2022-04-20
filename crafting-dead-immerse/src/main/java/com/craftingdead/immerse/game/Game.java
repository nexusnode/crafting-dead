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
