/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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
