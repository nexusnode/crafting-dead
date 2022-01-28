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

package com.craftingdead.immerse.game.tdm.state;

import com.craftingdead.immerse.game.GameUtil;
import com.craftingdead.immerse.game.module.team.TeamInstance;
import com.craftingdead.immerse.game.tdm.TdmServer;
import com.craftingdead.immerse.game.tdm.TdmTeam;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import com.craftingdead.immerse.util.state.State;
import com.craftingdead.immerse.util.state.TimedStateInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;

public class GameStateInstance extends TimedStateInstance<TdmServer> {

  private static final Component TIE_GAME = new TextComponent("Tie Game!");
  private static final Component SPACE = new TextComponent(" ");
  private static final Component WON = new TextComponent("Won!");
  private static final Component DEATHMATCH = new TextComponent("Deathmatch");

  public GameStateInstance(State<?> state, TdmServer context) {
    super(state, context, context.getGameDuration());
  }

  @Override
  public boolean tick() {
    if (this.getTimeRemainingSeconds() <= 20 && this.hasSecondPast()) {
      GameUtil.broadcastSound(ImmerseSoundEvents.COUNTDOWN.get(),
          this.getContext().getMinecraftServer());
    }

    TeamInstance<TdmTeam> redTeam =
        this.getContext().getTeamModule().getTeamInstance(TdmTeam.RED);
    TeamInstance<TdmTeam> blueTeam =
        this.getContext().getTeamModule().getTeamInstance(TdmTeam.BLUE);

    if (TdmTeam.getScore(redTeam) >= this.getContext().getMaxScore()
        ^ TdmTeam.getScore(blueTeam) >= this.getContext().getMaxScore()) {
      this.finishGame(redTeam, blueTeam);
      return true;
    }

    if (super.tick()) {
      this.finishGame(redTeam, blueTeam);
      return true;
    }

    return false;
  }

  private void finishGame(TeamInstance<?> redTeam, TeamInstance<?> blueTeam) {
    TdmTeam winningTeam = this.getContext().getWinningTeam().orElse(null);

    this.getContext().setMovementBlocked(true);

    Component winnerText;

    if (winningTeam == null) {
      winnerText = TIE_GAME.copy().withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD);
      redTeam.broadcastVictorySounds(ImmerseSoundEvents.RED_VICTORY.get(),
          this.getContext().getMinecraftServer());
      blueTeam.broadcastVictorySounds(ImmerseSoundEvents.BLUE_VICTORY.get(),
          this.getContext().getMinecraftServer());
    } else {
      winnerText =
          winningTeam.getDisplayName().copy()
              .withStyle(ChatFormatting.BOLD)
              .append(SPACE)
              .append(WON);
      switch (winningTeam) {
        case RED:
          redTeam.broadcastVictorySounds(ImmerseSoundEvents.RED_VICTORY.get(),
              this.getContext().getMinecraftServer());
          blueTeam.broadcastDefeatSounds(ImmerseSoundEvents.BLUE_DEFEAT.get(),
              this.getContext().getMinecraftServer());
          break;
        case BLUE:
          blueTeam.broadcastVictorySounds(ImmerseSoundEvents.BLUE_VICTORY.get(),
              this.getContext().getMinecraftServer());
          redTeam.broadcastDefeatSounds(ImmerseSoundEvents.RED_DEFEAT.get(),
              this.getContext().getMinecraftServer());
          break;
        default:
          break;
      }
    }

    this.sendWinAnnoucement(winnerText, TdmTeam.getScore(redTeam),
        TdmTeam.getScore(blueTeam));
  }

  private void sendWinAnnoucement(Component winnerText, int redScore, int blueScore) {
    Component scores = new TextComponent("Red: " + redScore + "    Blue: " + blueScore)
        .withStyle(ChatFormatting.RESET, ChatFormatting.ITALIC);
    GameUtil.sendChatAnnouncement(DEATHMATCH,
        winnerText
            .copy()
            .append(GameUtil.NEW_LINE)
            .append(scores),
        this.getContext().getMinecraftServer());
  }
}
