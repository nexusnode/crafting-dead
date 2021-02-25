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

package com.craftingdead.immerse.game.deathmatch.client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.craftingdead.immerse.game.IGameClient;
import com.craftingdead.immerse.game.deathmatch.DeathmatchGame;
import com.craftingdead.immerse.game.deathmatch.DeathmatchPlayerData;
import com.craftingdead.immerse.game.deathmatch.DeathmatchShop;
import com.craftingdead.immerse.game.deathmatch.DeathmatchTeam;
import com.craftingdead.immerse.game.deathmatch.state.DeathmatchState;
import com.craftingdead.immerse.game.shop.IShop;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DeathmatchClient extends DeathmatchGame implements IGameClient {

  private static final ResourceLocation STOPWATCH =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/stopwatch.png");

  private static final ResourceLocation BANNER_INVERTED =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/banner_inverted.png");

  private static final ResourceLocation DEAD =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/dead.png");

  private final Minecraft minecraft = Minecraft.getInstance();

  private DeathmatchState lastGameState;

  private final IShop shop = new DeathmatchShop(true);

  private boolean sentInitialTeamRequest = false;

  private ITextComponent getTimer() {
    int totalSeconds = this.getTimerValueSeconds();
    int mins = totalSeconds / 60;
    int seconds = totalSeconds % 60;

    TextFormatting colour;
    switch (this.getGameState()) {
      case PRE_GAME:
        colour = TextFormatting.YELLOW;
        break;
      case GAME:
        if (totalSeconds <= 20) {
          colour = TextFormatting.RED;
          break;
        }
      default:
        colour = TextFormatting.WHITE;
        break;
    }

    return Text.of(mins + ":" + (seconds < 10 ? "0" : "") + seconds).mergeStyle(TextFormatting.BOLD,
        colour);
  }

  @SubscribeEvent
  public void handleRenderNameplate(RenderNameplateEvent event) {
    if (event.getEntity() instanceof PlayerEntity
        && this.minecraft.player instanceof PlayerEntity) {
      // @formatter:off
      DeathmatchTeam playerTeam =
          this.getPlayerTeam(IPlayer.getExpected((PlayerEntity) event.getEntity()))
              .orElse(null);
      DeathmatchTeam ourTeam = this.minecraft.getRenderViewEntity()
          .getCapability(ModCapabilities.LIVING)
          .<IPlayer<?>>cast()
          .resolve()
          .flatMap(this::getPlayerTeam)
          .orElse(null);
      // @formatter:on
      if (playerTeam != ourTeam) {
        event.setResult(Event.Result.DENY);
      }
    }
  }

  @Override
  public Optional<IShop> getShop() {
    return Optional.of(this.shop);
  }

  @Override
  public void tick() {
    if (this.getGameState() != this.lastGameState) {
      this.lastGameState = this.getGameState();

      // Warm Up Notification
      if (this.getGameState() == DeathmatchState.PRE_GAME) {
        this.minecraft.ingameGUI.setDefaultTitlesTimes();
        this.minecraft.ingameGUI.func_238452_a_(Text.translate("title.warm_up"), null, -1, -1, -1);
      }

      // Post Game Notification
      if (this.getGameState() == DeathmatchState.POST_GAME) {
        this.minecraft.ingameGUI.setDefaultTitlesTimes();
        this.minecraft.ingameGUI.func_238452_a_(
            Text.translate("title.game_over").mergeStyle(TextFormatting.YELLOW), null, -1, -1, -1);
      }
    }

    if (!this.sentInitialTeamRequest && this.minecraft.world != null) {
      this.minecraft.displayGuiScreen(new SelectTeamScreen());
      this.sentInitialTeamRequest = true;
    }
  }

  @Override
  public void renderOverlay(IPlayer<? extends AbstractClientPlayerEntity> player,
      MatrixStack matrixStack, int width,
      int height, float partialTicks) {

    final int middleWidth = width / 2;

    ITextComponent redScore =
        Text.of(DeathmatchTeam.getScore(this.getTeamInstance(DeathmatchTeam.RED)))
            .mergeStyle(DeathmatchTeam.RED.getColourStyle());
    ITextComponent blueScore =
        Text.of(DeathmatchTeam.getScore(this.getTeamInstance(DeathmatchTeam.BLUE)))
            .mergeStyle(DeathmatchTeam.BLUE.getColourStyle());

    ITextComponent timer = this.getTimer();

    // Render Red Score
    RenderUtil.fill2(middleWidth - 19, 15, 18, 11, 0x99000000);
    AbstractGui.drawCenteredString(matrixStack, this.minecraft.fontRenderer, redScore,
        middleWidth - 9, 17, 0);

    // Render Blue Score
    RenderUtil.fill2(middleWidth + 1, 15, 18, 11, 0x99000000);
    AbstractGui.drawCenteredString(matrixStack, this.minecraft.fontRenderer, blueScore,
        middleWidth + 10, 17, 0);

    // Render Time
    RenderUtil.fill2(middleWidth - 19, 1, 38, 13, 0x99000000);
    AbstractGui.drawCenteredString(matrixStack, this.minecraft.fontRenderer, timer, middleWidth + 1,
        4, 0);

    final int headWidth = 20;
    final int headHeight = 20;

    // Render Red Team Player Heads
    List<UUID> redMembers = this.getTeamInstance(DeathmatchTeam.RED).getMembers();
    for (int i = 0; i < redMembers.size(); i++) {
      NetworkPlayerInfo playerInfo =
          this.minecraft.getConnection().getPlayerInfo(redMembers.get(i));
      if (playerInfo != null) {
        DeathmatchPlayerData playerData = this.getPlayerData(playerInfo.getGameProfile().getId());

        int x = middleWidth - 21 - headWidth - (i * (headWidth + 3));
        int y = 2;

        RenderUtil.fill2(x - 1, y - 1, headWidth + 2, headHeight + 2, 0x80000000);
        if (playerData.isDead()) {
          RenderUtil.fill2(x, y, headWidth, headHeight, 0x80000000);
          RenderSystem.enableBlend();
          RenderUtil.bind(DEAD);
          RenderUtil.blit(x, y, headWidth, headHeight);
          RenderSystem.disableBlend();
        } else {
          RenderUtil.renderHead(playerInfo.getLocationSkin(), matrixStack,
              x, y, headWidth, headHeight);
        }

        RenderUtil.fill2(x - 1, y + headHeight + 2, headWidth + 2,
            this.minecraft.fontRenderer.FONT_HEIGHT + 2,
            0x80000000);
        AbstractGui.drawCenteredString(matrixStack, this.minecraft.fontRenderer,
            String.valueOf(playerData.getScore()), x + headWidth / 2, y + headHeight + 4,
            0xFFFFFFFF);
      }
    }

    // Render Blue Team Player Heads
    List<UUID> blueMembers = this.getTeamInstance(DeathmatchTeam.BLUE).getMembers();
    for (int i = 0; i < blueMembers.size(); i++) {
      NetworkPlayerInfo playerInfo =
          this.minecraft.getConnection().getPlayerInfo(blueMembers.get(i));
      if (playerInfo != null) {
        DeathmatchPlayerData playerData = this.getPlayerData(playerInfo.getGameProfile().getId());

        int x = middleWidth + 21 + (i * (headWidth + 3));
        int y = 2;

        RenderUtil.fill2(x - 1, y - 1, headWidth + 2, headHeight + 2, 0x80000000);
        if (playerData.isDead()) {
          RenderUtil.fill2(x, y, headWidth, headHeight, 0x80000000);
          RenderSystem.enableBlend();
          RenderUtil.bind(DEAD);
          RenderUtil.blit(x, y, headWidth, headHeight);
          RenderSystem.disableBlend();
        } else {
          RenderUtil.renderHead(playerInfo.getLocationSkin(), matrixStack,
              x, y, headWidth, headHeight);
        }

        RenderUtil.fill2(x - 1, y + headHeight + 2, headWidth + 2,
            this.minecraft.fontRenderer.FONT_HEIGHT + 2,
            0x80000000);
        AbstractGui.drawCenteredString(matrixStack, this.minecraft.fontRenderer,
            String.valueOf(playerData.getScore()), x + headWidth / 2, y + headHeight + 4,
            0xFFFFFFFF);
      }
    }
  }

  @Override
  public void renderPlayerList(IPlayer<? extends AbstractClientPlayerEntity> player,
      MatrixStack matrixStack, int width, int height, float partialTicks) {
    int mwidth = width / 2;
    int mheight = height / 2;

    int sbwidth = 330;
    int sbheight = 200;
    int sbx = mwidth - (sbwidth / 2);
    int sby = mheight - (sbheight / 2) - 18;

    // Render SB Header
    RenderUtil.fill2(sbx, sby, sbwidth, 40, 0xCC000000);
    RenderUtil.fill2(sbx, sby + 3, sbwidth, 34, 0x33FFFFFF);

    // Render Logo
    RenderSystem.enableBlend();
    RenderUtil.bind(BANNER_INVERTED);
    RenderUtil.blit(sbx + 2, sby + 5, 125, 27.5);
    RenderSystem.disableBlend();
    // Render Game Information

    ITextComponent gameTitle =
        this.getGameType().getDisplayName().copyRaw().mergeStyle(TextFormatting.WHITE,
            TextFormatting.BOLD);
    this.minecraft.fontRenderer.func_243246_a(matrixStack, gameTitle,
        sbx + sbwidth - 3 - this.minecraft.fontRenderer.getStringPropertyWidth(gameTitle),
        sby + 9, 0);

    ITextComponent mapTitle = Text.of(this.getDisplayName()).mergeStyle(TextFormatting.GRAY);
    this.minecraft.fontRenderer.func_243246_a(matrixStack, mapTitle,
        sbx + sbwidth - 3 - this.minecraft.fontRenderer.getStringPropertyWidth(mapTitle),
        sby + 20, 0);

    // Render Time
    RenderSystem.enableBlend();
    RenderUtil.bind(STOPWATCH);
    RenderUtil.blit(sbx + (sbwidth / 2) - 8, sby + 5, 16, 16);
    RenderSystem.disableBlend();
    ITextComponent timer = this.getTimer();
    AbstractGui.drawCenteredString(matrixStack, this.minecraft.fontRenderer, timer,
        sbx + (sbwidth / 2),
        sby + 24, 0);

    // Render Teams and Player Information
    RenderUtil.fill2(sbx, sby + 41, sbwidth, 193, 0x80000000);
    RenderUtil.renderPlayerListRow(matrixStack, sbx, sby + 41, sbwidth, 13, Text.of("Ping"),
        Text.of("Username"), Text.of("K"), Text.of("A"), Text.of("D"), Text.of("Score"));

    List<UUID> blueMembers = this.getTeamInstance(DeathmatchTeam.BLUE).getMembers();
    for (int i = 0; i < blueMembers.size(); i++) {
      NetworkPlayerInfo playerInfo =
          this.minecraft.getConnection().getPlayerInfo(blueMembers.get(i));
      if (playerInfo != null) {
        ITextComponent username =
            playerInfo.getDisplayName() == null ? Text.of(playerInfo.getGameProfile().getName())
                : playerInfo.getDisplayName();
        DeathmatchPlayerData playerData = this.getPlayerData(playerInfo.getGameProfile().getId());
        RenderUtil.renderPlayerListRow(matrixStack, sbx, sby + 55 + (i * 11), sbwidth, 10,
            Text.of(String.valueOf(playerInfo.getResponseTime())),
            username, Text.of(playerData.getKills()), Text.of(playerData.getAssists()),
            Text.of(playerData.getDeaths()),
            Text.of(playerData.getScore()));
      }
    }

    List<UUID> redMembers = this.getTeamInstance(DeathmatchTeam.RED).getMembers();
    for (int i = 0; i < redMembers.size(); i++) {
      NetworkPlayerInfo playerInfo =
          this.minecraft.getConnection().getPlayerInfo(redMembers.get(i));
      if (playerInfo != null) {
        ITextComponent username =
            playerInfo.getDisplayName() == null ? Text.of(playerInfo.getGameProfile().getName())
                : playerInfo.getDisplayName();
        DeathmatchPlayerData playerData = this.getPlayerData(playerInfo.getGameProfile().getId());
        RenderUtil.renderPlayerListRow(matrixStack, sbx, sby + 147 + (i * 11), sbwidth, 10,
            Text.of(String.valueOf(playerInfo.getResponseTime())),
            username, Text.of(playerData.getKills()), Text.of(playerData.getAssists()),
            Text.of(playerData.getDeaths()),
            Text.of(playerData.getScore()));
      }
    }

    RenderUtil.fill2(sbx + 4, sby + 144, sbwidth - 8, 1, 0x80FFFFFF);
  }
}
