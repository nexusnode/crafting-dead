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

package com.craftingdead.immerse.game.tdm.state;

import java.util.function.BiFunction;
import com.craftingdead.immerse.game.tdm.TdmServer;
import com.craftingdead.immerse.util.state.State;
import com.craftingdead.immerse.util.state.StateInstance;

public enum TdmState implements State<TdmServer> {

  IDLE(StateInstance::new),
  PRE_GAME(PreGameStateInstance::new),
  GAME(GameStateInstance::new),
  POST_GAME(PostGameStateInstance::new);

  private final BiFunction<TdmState, TdmServer, ? extends StateInstance<TdmServer>> stateFactory;

  private TdmState(
      BiFunction<TdmState, TdmServer, ? extends StateInstance<TdmServer>> stateFactory) {
    this.stateFactory = stateFactory;
  }

  @Override
  public StateInstance<TdmServer> newInstance(TdmServer context) {
    return this.stateFactory.apply(this, context);
  }
}
