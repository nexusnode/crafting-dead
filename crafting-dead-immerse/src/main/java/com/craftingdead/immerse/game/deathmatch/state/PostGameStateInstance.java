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

package com.craftingdead.immerse.game.deathmatch.state;

import com.craftingdead.immerse.game.deathmatch.DeathmatchServer;
import com.craftingdead.immerse.game.state.State;
import com.craftingdead.immerse.game.state.TimedStateInstance;

public class PostGameStateInstance extends TimedStateInstance<DeathmatchServer> {

  public PostGameStateInstance(State<?> state, DeathmatchServer context) {
    super(state, context, context.getPostGameDuration());
  }

  @Override
  public boolean tick() {
    if (super.tick()) {
      this.getContext().setMovementBlocked(false);
      return true;
    }

    if (this.getContext().getMinecraftServer().getPlayerCount() <= 1
        && this.getTimeRemainingSeconds() > 6) {
      this.setTimeRemainingSeconds(5);
    }

    return false;
  }
}
