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

package com.craftingdead.immerse.game.tdm.state;

import com.craftingdead.immerse.game.GameUtil;
import com.craftingdead.immerse.game.tdm.TdmServer;
import com.craftingdead.immerse.game.tdm.TdmTeam;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import com.craftingdead.immerse.util.state.State;
import com.craftingdead.immerse.util.state.TimedStateInstance;

public class PreGameStateInstance extends TimedStateInstance<TdmServer> {

  public PreGameStateInstance(State<?> state, TdmServer context) {
    super(state, context, context.getPreGameDuration());
  }

  @Override
  public boolean tick() {
    boolean redEmpty =
        this.getContext().getTeamModule().getTeamInstance(TdmTeam.RED).getMembers().isEmpty();
    boolean blueEmpty =
        this.getContext().getTeamModule().getTeamInstance(TdmTeam.BLUE).getMembers().isEmpty();
    if (redEmpty || blueEmpty) {
      return false;
    }

    if (super.tick()) {
      this.getContext().setMovementBlocked(false);
      this.getContext().resetBuyTimes();
      this.getContext().resetPlayerData();
      this.getContext().resetTeams();
      this.getContext().getLogicalServer()
          .respawnPlayers(playerEntity -> !playerEntity.isSpectator(), false);
      GameUtil.broadcastSound(ImmerseSoundEvents.START_MUSIC.get(),
          this.getContext().getMinecraftServer());
      return true;
    }

    if (this.getContext().getMinecraftServer().getPlayerCount() > 5
        && this.getTimeRemainingSeconds() > 16) {
      this.setTimeRemainingSeconds(15);
    }

    if (this.getTimeRemainingSeconds() <= 15 && this.hasSecondPast()) {
      GameUtil.broadcastSound(ImmerseSoundEvents.COUNTDOWN.get(),
          this.getContext().getMinecraftServer());
    }

    if (this.getTimeRemainingSeconds() <= 5) {
      this.getContext().setMovementBlocked(true);
    }

    return false;
  }
}
