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

import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.game.GameUtil;
import com.craftingdead.immerse.game.deathmatch.DeathmatchServer;
import com.craftingdead.immerse.game.deathmatch.DeathmatchTeam;
import com.craftingdead.immerse.game.state.IState;
import com.craftingdead.immerse.game.state.TimedStateInstance;
import com.craftingdead.immerse.game.team.TeamInstance;
import com.craftingdead.immerse.util.ModSoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class GameStateInstance extends TimedStateInstance<DeathmatchServer> {

  public GameStateInstance(IState<?> state, DeathmatchServer context) {
    super(state, context, context.getGameDuration());
  }

  @Override
  public boolean tick() {
    if (this.getTimeRemainingSeconds() <= 20 && this.hasSecondPast()) {
      GameUtil.broadcastSound(ModSoundEvents.COUNTDOWN.get(),
          this.getContext().getMinecraftServer());
    }

    TeamInstance<DeathmatchTeam> redTeam = this.getContext().getTeamInstance(DeathmatchTeam.RED);
    TeamInstance<DeathmatchTeam> blueTeam = this.getContext().getTeamInstance(DeathmatchTeam.BLUE);

    if (DeathmatchTeam.getScore(redTeam) >= this.getContext().getMaxScore()
        ^ DeathmatchTeam.getScore(blueTeam) >= this.getContext().getMaxScore()) {
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
    DeathmatchTeam winningTeam = this.getContext().getWinningTeam().orElse(null);

    this.getContext().setMovementBlocked(true);

    ITextComponent winnerText;

    if (winningTeam == null) {
      winnerText = Text.of("Tie Game!").withStyle(TextFormatting.AQUA, TextFormatting.BOLD);
      redTeam.broadcastVictorySounds(ModSoundEvents.RED_VICTORY.get(),
          this.getContext().getMinecraftServer());
      blueTeam.broadcastVictorySounds(ModSoundEvents.BLUE_VICTORY.get(),
          this.getContext().getMinecraftServer());
    } else {
      winnerText =
          winningTeam.getDisplayName().copy().withStyle(TextFormatting.BOLD)
              .append(Text.of(" Won!"));
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

    this.sendWinAnnoucement(winnerText, DeathmatchTeam.getScore(redTeam),
        DeathmatchTeam.getScore(blueTeam));
  }

  private void sendWinAnnoucement(ITextComponent winnerText, int redScore, int blueScore) {
    ITextComponent scores = Text.of("Red: " + redScore + "    Blue: " + blueScore)
        .withStyle(TextFormatting.RESET, TextFormatting.ITALIC);
    GameUtil.sendChatAnnouncement(Text.of("Deathmatch"),
        Text.copyAndJoin(winnerText, GameUtil.NEW_LINE, scores),
        this.getContext().getMinecraftServer());
  }
}
