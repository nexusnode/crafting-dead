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

package com.craftingdead.immerse.game.tdm;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import com.craftingdead.core.event.LivingExtensionEvent;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.screen.game.SelectTeamScreen;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.craftingdead.immerse.game.GameClient;
import com.craftingdead.immerse.game.module.Module;
import com.craftingdead.immerse.game.module.shop.ClientShopModule;
import com.craftingdead.immerse.game.module.team.ClientTeamModule;
import com.craftingdead.immerse.game.module.team.TeamModule;
import com.craftingdead.immerse.game.tdm.state.TdmState;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TdmClient extends TdmGame<Module> implements GameClient {

  private static final ResourceLocation STOPWATCH =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/stopwatch.png");

  private static final ResourceLocation BANNER_INVERTED =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/banner_inverted.png");

  private static final ResourceLocation DEAD =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/dead.png");

  private final Minecraft minecraft = Minecraft.getInstance();

  private ClientShopModule shopModule;
  private ClientTeamModule<TdmTeam> teamModule;

  private TdmState lastGameState;

  private boolean sentInitialTeamRequest = false;

  public TdmClient() {
    super("");
  }

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

    return new StringTextComponent(mins + ":" + (seconds < 10 ? "0" : "") + seconds)
        .withStyle(TextFormatting.BOLD, colour);
  }

  @Override
  public TeamModule<TdmTeam> getTeamModule() {
    return this.teamModule;
  }

  @Override
  public void registerModules(Consumer<Module> registrar) {
    this.shopModule = new ClientShopModule();
    registrar.accept(this.shopModule);

    this.teamModule = new ClientTeamModule<>(TdmTeam.class, SelectTeamScreen::new);
    registrar.accept(this.teamModule);
    super.registerModules(registrar);
  }

  @Override
  public boolean disableSwapHands() {
    return true;
  }

  @Override
  public void tick() {
    if (this.getGameState() != this.lastGameState && this.minecraft.player != null
        && this.getTeamModule().getPlayerTeam(this.minecraft.player.getUUID()).isPresent()) {
      this.lastGameState = this.getGameState();


      switch (this.getGameState()) {
        case PRE_GAME:
          this.minecraft.gui.resetTitleTimes();
          this.minecraft.gui.setTitles(
              new TranslationTextComponent("title.warm_up").withStyle(TextFormatting.YELLOW), null,
              -1, -1, -1);
          break;
        case GAME:
          this.minecraft.gui.resetTitleTimes();
          this.minecraft.gui.setTitles(
              new TranslationTextComponent("title.game_start").withStyle(TextFormatting.AQUA), null,
              -1, -1,
              -1);
          break;
        case POST_GAME:
          this.minecraft.gui.resetTitleTimes();
          this.minecraft.gui.setTitles(
              new TranslationTextComponent("title.game_over").withStyle(TextFormatting.RED), null,
              -1, -1, -1);
          break;
        default:
          break;
      }
    }

    if (!this.sentInitialTeamRequest && this.minecraft.level != null) {
      this.minecraft.setScreen(new SelectTeamScreen());
      this.sentInitialTeamRequest = true;
    }
  }

  @Override
  public boolean renderOverlay(PlayerExtension<? extends AbstractClientPlayerEntity> player,
      MatrixStack matrixStack, int width,
      int height, float partialTicks) {

    final int middleWidth = width / 2;

    ITextComponent redScore =
        new StringTextComponent(
            String.valueOf(TdmTeam.getScore(this.getTeamModule().getTeamInstance(TdmTeam.RED))))
                .withStyle(TdmTeam.RED.getColourStyle());
    ITextComponent blueScore =
        new StringTextComponent(
            String.valueOf(TdmTeam.getScore(this.getTeamModule().getTeamInstance(TdmTeam.BLUE))))
                .withStyle(TdmTeam.BLUE.getColourStyle());

    ITextComponent timer = this.getTimer();

    // Render Red Score
    RenderUtil.fillWidthHeight(matrixStack, middleWidth - 19, 15, 18, 11, 0x99000000);
    AbstractGui.drawCenteredString(matrixStack, this.minecraft.font, redScore,
        middleWidth - 9, 17, 0);

    // Render Blue Score
    RenderUtil.fillWidthHeight(matrixStack, middleWidth + 1, 15, 18, 11, 0x99000000);
    AbstractGui.drawCenteredString(matrixStack, this.minecraft.font, blueScore,
        middleWidth + 10, 17, 0);

    // Render Time
    RenderUtil.fillWidthHeight(matrixStack, middleWidth - 19, 1, 38, 13, 0x99000000);
    AbstractGui.drawCenteredString(matrixStack, this.minecraft.font, timer, middleWidth + 1,
        4, 0);

    final int headWidth = 20;
    final int headHeight = 20;

    // Render Red Team Player Heads
    List<UUID> redMembers = this.getTeamModule().getTeamInstance(TdmTeam.RED).getMembers();
    for (int i = 0; i < redMembers.size(); i++) {
      NetworkPlayerInfo playerInfo =
          this.minecraft.getConnection().getPlayerInfo(redMembers.get(i));
      if (playerInfo != null) {
        TdmPlayerData playerData = this.getPlayerData(playerInfo.getProfile().getId());

        int x = middleWidth - 21 - headWidth - (i * (headWidth + 3));
        int y = 2;

        RenderUtil.fillWidthHeight(matrixStack, x - 1, y - 1, headWidth + 2, headHeight + 2,
            0x80000000);
        if (playerData.isDead()) {
          RenderUtil.fillWidthHeight(matrixStack, x, y, headWidth, headHeight, 0x80000000);
          RenderSystem.enableBlend();
          RenderUtil.bind(DEAD);
          RenderUtil.blit(matrixStack, x, y, headWidth, headHeight);
          RenderSystem.disableBlend();
        } else {
          RenderUtil.renderHead(playerInfo.getSkinLocation(), matrixStack,
              x, y, headWidth, headHeight);
        }

        RenderUtil.fillWidthHeight(matrixStack, x - 1, y + headHeight + 2, headWidth + 2,
            this.minecraft.font.lineHeight + 2,
            0x80000000);
        AbstractGui.drawCenteredString(matrixStack, this.minecraft.font,
            String.valueOf(playerData.getScore()), x + headWidth / 2, y + headHeight + 4,
            0xFFFFFFFF);
      }
    }

    // Render Blue Team Player Heads
    List<UUID> blueMembers = this.getTeamModule().getTeamInstance(TdmTeam.BLUE).getMembers();
    for (int i = 0; i < blueMembers.size(); i++) {
      NetworkPlayerInfo playerInfo =
          this.minecraft.getConnection().getPlayerInfo(blueMembers.get(i));
      if (playerInfo != null) {
        TdmPlayerData playerData = this.getPlayerData(playerInfo.getProfile().getId());

        int x = middleWidth + 21 + (i * (headWidth + 3));
        int y = 2;

        RenderUtil.fillWidthHeight(matrixStack, x - 1, y - 1, headWidth + 2, headHeight + 2,
            0x80000000);
        if (playerData.isDead()) {
          RenderUtil.fillWidthHeight(matrixStack, x, y, headWidth, headHeight, 0x80000000);
          RenderSystem.enableBlend();
          RenderUtil.bind(DEAD);
          RenderUtil.blit(matrixStack, x, y, headWidth, headHeight);
          RenderSystem.disableBlend();
        } else {
          RenderUtil.renderHead(playerInfo.getSkinLocation(), matrixStack,
              x, y, headWidth, headHeight);
        }

        RenderUtil.fillWidthHeight(matrixStack, x - 1, y + headHeight + 2, headWidth + 2,
            this.minecraft.font.lineHeight + 2,
            0x80000000);
        AbstractGui.drawCenteredString(matrixStack, this.minecraft.font,
            String.valueOf(playerData.getScore()), x + headWidth / 2, y + headHeight + 4,
            0xFFFFFFFF);
      }
    }

    return false;
  }

  @Override
  public boolean renderPlayerList(PlayerExtension<? extends AbstractClientPlayerEntity> player,
      MatrixStack matrixStack, int width, int height, float partialTicks) {
    int mwidth = width / 2;
    int mheight = height / 2;

    int sbwidth = 330;
    int sbheight = 200;
    int sbx = mwidth - (sbwidth / 2);
    int sby = mheight - (sbheight / 2) - 18;

    // Render SB Header
    RenderUtil.fillWidthHeight(matrixStack, sbx, sby, sbwidth, 40, 0xCC000000);
    RenderUtil.fillWidthHeight(matrixStack, sbx, sby + 3, sbwidth, 34, 0x33FFFFFF);

    // Render Logo
    RenderSystem.enableBlend();
    RenderUtil.bind(BANNER_INVERTED);
    RenderUtil.blit(matrixStack, sbx + 2, sby + 5, 125, 27.5F);
    RenderSystem.disableBlend();
    // Render Game Information

    ITextComponent gameTitle =
        this.getType().getDisplayName().copy().withStyle(TextFormatting.WHITE,
            TextFormatting.BOLD);
    this.minecraft.font.drawShadow(matrixStack, gameTitle,
        sbx + sbwidth - 3 - this.minecraft.font.width(gameTitle),
        sby + 9, 0);

    ITextComponent mapTitle =
        new StringTextComponent(this.getDisplayName()).withStyle(TextFormatting.GRAY);
    this.minecraft.font.drawShadow(matrixStack, mapTitle,
        sbx + sbwidth - 3 - this.minecraft.font.width(mapTitle),
        sby + 20, 0);

    // Render Time
    RenderSystem.enableBlend();
    RenderUtil.bind(STOPWATCH);
    RenderUtil.blit(matrixStack, sbx + (sbwidth / 2) - 8, sby + 5, 16, 16);
    RenderSystem.disableBlend();
    ITextComponent timer = this.getTimer();
    AbstractGui.drawCenteredString(matrixStack, this.minecraft.font, timer,
        sbx + (sbwidth / 2),
        sby + 24, 0);

    // Render Teams and Player Information
    RenderUtil.fillWidthHeight(matrixStack, sbx, sby + 41, sbwidth, 193, 0x80000000);
    RenderUtil.renderPlayerListRow(matrixStack, sbx, sby + 41, sbwidth, 13,
        new StringTextComponent("Ping"),
        new StringTextComponent("Username"), new StringTextComponent("K"),
        new StringTextComponent("A"), new StringTextComponent("D"),
        new StringTextComponent("Score"));

    List<UUID> blueMembers = this.getTeamModule().getTeamInstance(TdmTeam.BLUE).getMembers();
    for (int i = 0; i < blueMembers.size(); i++) {
      NetworkPlayerInfo playerInfo =
          this.minecraft.getConnection().getPlayerInfo(blueMembers.get(i));
      if (playerInfo != null) {
        ITextComponent username =
            playerInfo.getTabListDisplayName() == null
                ? new StringTextComponent(playerInfo.getProfile().getName())
                : playerInfo.getTabListDisplayName();
        TdmPlayerData playerData = this.getPlayerData(playerInfo.getProfile().getId());
        RenderUtil.renderPlayerListRow(matrixStack, sbx, sby + 55 + (i * 11), sbwidth, 10,
            new StringTextComponent(String.valueOf(playerInfo.getLatency())),
            username, new StringTextComponent(String.valueOf(playerData.getKills())),
            new StringTextComponent(String.valueOf(playerData.getAssists())),
            new StringTextComponent(String.valueOf(playerData.getDeaths())),
            new StringTextComponent(String.valueOf(playerData.getScore())));
      }
    }

    List<UUID> redMembers = this.getTeamModule().getTeamInstance(TdmTeam.RED).getMembers();
    for (int i = 0; i < redMembers.size(); i++) {
      NetworkPlayerInfo playerInfo =
          this.minecraft.getConnection().getPlayerInfo(redMembers.get(i));
      if (playerInfo != null) {
        ITextComponent username =
            playerInfo.getTabListDisplayName() == null
                ? new StringTextComponent(playerInfo.getProfile().getName())
                : playerInfo.getTabListDisplayName();
        TdmPlayerData playerData = this.getPlayerData(playerInfo.getProfile().getId());
        RenderUtil.renderPlayerListRow(matrixStack, sbx, sby + 147 + (i * 11), sbwidth, 10,
            new StringTextComponent(String.valueOf(playerInfo.getLatency())),
            username, new StringTextComponent(String.valueOf(playerData.getKills())),
            new StringTextComponent(String.valueOf(playerData.getAssists())),
            new StringTextComponent(String.valueOf(playerData.getDeaths())),
            new StringTextComponent(String.valueOf(playerData.getScore())));
      }
    }

    RenderUtil.fillWidthHeight(matrixStack, sbx + 4, sby + 144, sbwidth - 8, 1, 0x80FFFFFF);

    return true;
  }

  @SubscribeEvent
  public void handleLivingLoad(LivingExtensionEvent.Load event) {
    if (event.getLiving() instanceof PlayerExtension
        && event.getLiving().getLevel().isClientSide()) {
      PlayerExtension<?> player = (PlayerExtension<?>) event.getLiving();
      player.registerHandler(TdmPlayerHandler.ID,
          new TdmPlayerHandler(this, player));
    }
  }

  @SubscribeEvent
  public void handleRenderNameplate(RenderNameplateEvent event) {
    if (event.getEntity() instanceof PlayerEntity
        && this.minecraft.player instanceof PlayerEntity) {
      TdmTeam playerTeam =
          this.getTeamModule().getPlayerTeam(event.getEntity().getUUID())
              .orElse(null);
      TdmTeam ourTeam =
          this.getTeamModule().getPlayerTeam(this.minecraft.getCameraEntity().getUUID())
              .orElse(null);
      if (playerTeam != ourTeam) {
        event.setResult(Event.Result.DENY);
      }
    }
  }
}
