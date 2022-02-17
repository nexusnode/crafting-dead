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
