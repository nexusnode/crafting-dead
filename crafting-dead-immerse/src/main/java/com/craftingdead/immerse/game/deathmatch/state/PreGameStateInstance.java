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

import com.craftingdead.immerse.game.GameUtil;
import com.craftingdead.immerse.game.deathmatch.DeathmatchServer;
import com.craftingdead.immerse.game.deathmatch.DeathmatchTeam;
import com.craftingdead.immerse.game.state.IState;
import com.craftingdead.immerse.game.state.TimedStateInstance;
import com.craftingdead.immerse.util.ModSoundEvents;

public class PreGameStateInstance extends TimedStateInstance<DeathmatchServer> {

  public PreGameStateInstance(IState<?> state, DeathmatchServer context) {
    super(state, context, context.getPreGameDuration());
  }

  @Override
  public boolean tick() {
    boolean redEmpty =
        this.getContext().getTeamInstance(DeathmatchTeam.RED).getMembers().isEmpty();
    boolean blueEmpty =
        this.getContext().getTeamInstance(DeathmatchTeam.BLUE).getMembers().isEmpty();
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
      GameUtil.broadcastSound(ModSoundEvents.START_MUSIC.get(),
          this.getContext().getMinecraftServer());
      return true;
    }

    if (this.getContext().getMinecraftServer().getPlayerCount() > 5
        && this.getTimeRemainingSeconds() > 16) {
      this.setTimeRemainingSeconds(15);
    }

    if (this.getTimeRemainingSeconds() <= 15 && this.hasSecondPast()) {
      GameUtil.broadcastSound(ModSoundEvents.COUNTDOWN.get(),
          this.getContext().getMinecraftServer());
    }

    if (this.getTimeRemainingSeconds() <= 5) {
      this.getContext().setMovementBlocked(true);
    }

    return false;
  }
}
