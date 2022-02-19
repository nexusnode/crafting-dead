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

import com.craftingdead.immerse.game.tdm.TdmServer;
import com.craftingdead.immerse.util.state.State;
import com.craftingdead.immerse.util.state.TimedStateInstance;

public class PostGameStateInstance extends TimedStateInstance<TdmServer> {

  public PostGameStateInstance(State<?> state, TdmServer context) {
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
