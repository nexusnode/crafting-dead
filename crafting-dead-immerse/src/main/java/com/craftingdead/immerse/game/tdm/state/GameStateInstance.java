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
import com.craftingdead.immerse.util.ModSoundEvents;
import com.craftingdead.immerse.util.state.State;
import com.craftingdead.immerse.util.state.TimedStateInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class GameStateInstance extends TimedStateInstance<TdmServer> {

  private static final ITextComponent TIE_GAME = new StringTextComponent("Tie Game!");
  private static final ITextComponent SPACE = new StringTextComponent(" ");
  private static final ITextComponent WON = new StringTextComponent("Won!");
  private static final ITextComponent DEATHMATCH = new StringTextComponent("Deathmatch");

  public GameStateInstance(State<?> state, TdmServer context) {
    super(state, context, context.getGameDuration());
  }

  @Override
  public boolean tick() {
    if (this.getTimeRemainingSeconds() <= 20 && this.hasSecondPast()) {
      GameUtil.broadcastSound(ModSoundEvents.COUNTDOWN.get(),
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

    ITextComponent winnerText;

    if (winningTeam == null) {
      winnerText = TIE_GAME.copy().withStyle(TextFormatting.AQUA, TextFormatting.BOLD);
      redTeam.broadcastVictorySounds(ModSoundEvents.RED_VICTORY.get(),
          this.getContext().getMinecraftServer());
      blueTeam.broadcastVictorySounds(ModSoundEvents.BLUE_VICTORY.get(),
          this.getContext().getMinecraftServer());
    } else {
      winnerText =
          winningTeam.getDisplayName().copy()
              .withStyle(TextFormatting.BOLD)
              .append(SPACE)
              .append(WON);
      switch (winningTeam) {
        case RED:
          redTeam.broadcastVictorySounds(ModSoundEvents.RED_VICTORY.get(),
              this.getContext().getMinecraftServer());
          blueTeam.broadcastDefeatSounds(ModSoundEvents.BLUE_DEFEAT.get(),
              this.getContext().getMinecraftServer());
          break;
        case BLUE:
          blueTeam.broadcastVictorySounds(ModSoundEvents.BLUE_VICTORY.get(),
              this.getContext().getMinecraftServer());
          redTeam.broadcastDefeatSounds(ModSoundEvents.RED_DEFEAT.get(),
              this.getContext().getMinecraftServer());
          break;
        default:
          break;
      }
    }

    this.sendWinAnnoucement(winnerText, TdmTeam.getScore(redTeam),
        TdmTeam.getScore(blueTeam));
  }

  private void sendWinAnnoucement(ITextComponent winnerText, int redScore, int blueScore) {
    ITextComponent scores = new StringTextComponent("Red: " + redScore + "    Blue: " + blueScore)
        .withStyle(TextFormatting.RESET, TextFormatting.ITALIC);
    GameUtil.sendChatAnnouncement(DEATHMATCH,
        winnerText
            .copy()
            .append(GameUtil.NEW_LINE)
            .append(scores),
        this.getContext().getMinecraftServer());
  }
}
