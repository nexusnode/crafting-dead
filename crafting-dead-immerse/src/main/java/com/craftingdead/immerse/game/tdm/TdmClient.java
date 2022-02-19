/*
 * Crafting Dead Copyright (C) 2021 NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
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
import com.craftingdead.immerse.game.module.GameModule;
import com.craftingdead.immerse.game.module.shop.ClientShopModule;
import com.craftingdead.immerse.game.module.team.ClientTeamModule;
import com.craftingdead.immerse.game.module.team.TeamModule;
import com.craftingdead.immerse.game.tdm.state.TdmState;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TdmClient extends TdmGame implements GameClient {

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

  private Component getTimer() {
    int totalSeconds = this.getTimerValueSeconds();
    int mins = totalSeconds / 60;
    int seconds = totalSeconds % 60;

    ChatFormatting colour;
    switch (this.getGameState()) {
      case PRE_GAME:
        colour = ChatFormatting.YELLOW;
        break;
      case GAME:
        if (totalSeconds <= 20) {
          colour = ChatFormatting.RED;
          break;
        }
      default:
        colour = ChatFormatting.WHITE;
        break;
    }

    return new TextComponent(mins + ":" + (seconds < 10 ? "0" : "") + seconds)
        .withStyle(ChatFormatting.BOLD, colour);
  }

  @Override
  public TeamModule<TdmTeam> getTeamModule() {
    return this.teamModule;
  }

  @Override
  public void registerClientModules(Consumer<GameModule> registrar) {
    this.shopModule = new ClientShopModule();
    registrar.accept(this.shopModule);

    this.teamModule = new ClientTeamModule<>(TdmTeam.class, SelectTeamScreen::new);
    registrar.accept(this.teamModule);
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
          this.minecraft.gui.setTitle(
              new TranslatableComponent("title.warm_up").withStyle(ChatFormatting.YELLOW));
          break;
        case GAME:
          this.minecraft.gui.resetTitleTimes();
          this.minecraft.gui.setTitle(
              new TranslatableComponent("title.game_start").withStyle(ChatFormatting.AQUA));
          break;
        case POST_GAME:
          this.minecraft.gui.resetTitleTimes();
          this.minecraft.gui.setTitle(
              new TranslatableComponent("title.game_over").withStyle(ChatFormatting.RED));
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
  public boolean renderOverlay(PlayerExtension<? extends AbstractClientPlayer> player,
      PoseStack matrixStack, int width,
      int height, float partialTicks) {

    final int middleWidth = width / 2;

    Component redScore =
        new TextComponent(
            String.valueOf(TdmTeam.getScore(this.getTeamModule().getTeamInstance(TdmTeam.RED))))
                .withStyle(TdmTeam.RED.getColourStyle());
    Component blueScore =
        new TextComponent(
            String.valueOf(TdmTeam.getScore(this.getTeamModule().getTeamInstance(TdmTeam.BLUE))))
                .withStyle(TdmTeam.BLUE.getColourStyle());

    Component timer = this.getTimer();

    // Render Red Score
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fillWidthHeight(matrixStack, middleWidth - 19, 15, 18, 11, 0x99000000);
    GuiComponent.drawCenteredString(matrixStack, this.minecraft.font, redScore,
        middleWidth - 9, 17, 0);

    // Render Blue Score
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fillWidthHeight(matrixStack, middleWidth + 1, 15, 18, 11, 0x99000000);
    GuiComponent.drawCenteredString(matrixStack, this.minecraft.font, blueScore,
        middleWidth + 10, 17, 0);

    // Render Time
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fillWidthHeight(matrixStack, middleWidth - 19, 1, 38, 13, 0x99000000);
    GuiComponent.drawCenteredString(matrixStack, this.minecraft.font, timer, middleWidth + 1,
        4, 0);

    final int headWidth = 20;
    final int headHeight = 20;

    // Render Red Team Player Heads
    List<UUID> redMembers = this.getTeamModule().getTeamInstance(TdmTeam.RED).getMembers();
    for (int i = 0; i < redMembers.size(); i++) {
      PlayerInfo playerInfo =
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
          RenderSystem.setShaderTexture(0, DEAD);
          RenderUtil.blit(matrixStack, x, y, headWidth, headHeight);
          RenderSystem.disableBlend();
        } else {
          RenderUtil.blitAvatar(playerInfo.getSkinLocation(), matrixStack,
              x, y, headWidth, headHeight);
        }

        RenderUtil.fillWidthHeight(matrixStack, x - 1, y + headHeight + 2, headWidth + 2,
            this.minecraft.font.lineHeight + 2,
            0x80000000);
        GuiComponent.drawCenteredString(matrixStack, this.minecraft.font,
            String.valueOf(playerData.getScore()), x + headWidth / 2, y + headHeight + 4,
            0xFFFFFFFF);
      }
    }

    // Render Blue Team Player Heads
    List<UUID> blueMembers = this.getTeamModule().getTeamInstance(TdmTeam.BLUE).getMembers();
    for (int i = 0; i < blueMembers.size(); i++) {
      PlayerInfo playerInfo =
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
          RenderSystem.setShaderTexture(0, DEAD);
          RenderUtil.blit(matrixStack, x, y, headWidth, headHeight);
          RenderSystem.disableBlend();
        } else {
          RenderUtil.blitAvatar(playerInfo.getSkinLocation(), matrixStack,
              x, y, headWidth, headHeight);
        }

        RenderUtil.fillWidthHeight(matrixStack, x - 1, y + headHeight + 2, headWidth + 2,
            this.minecraft.font.lineHeight + 2,
            0x80000000);
        GuiComponent.drawCenteredString(matrixStack, this.minecraft.font,
            String.valueOf(playerData.getScore()), x + headWidth / 2, y + headHeight + 4,
            0xFFFFFFFF);
      }
    }

    return false;
  }

  @Override
  public boolean renderPlayerList(PlayerExtension<? extends AbstractClientPlayer> player,
      PoseStack matrixStack, int width, int height, float partialTicks) {
    int mwidth = width / 2;
    int mheight = height / 2;

    int sbwidth = 330;
    int sbheight = 200;
    int sbx = mwidth - (sbwidth / 2);
    int sby = mheight - (sbheight / 2) - 18;

    // Render SB Header
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fillWidthHeight(matrixStack, sbx, sby, sbwidth, 40, 0xCC000000);
    RenderUtil.fillWidthHeight(matrixStack, sbx, sby + 3, sbwidth, 34, 0x33FFFFFF);

    // Render Logo
    RenderSystem.enableBlend();
    RenderSystem.setShaderTexture(0, BANNER_INVERTED);
    RenderUtil.blit(matrixStack, sbx + 2, sby + 5, 125, 27.5F);
    RenderSystem.disableBlend();
    // Render Game Information

    Component gameTitle =
        this.getType().getDisplayName().copy().withStyle(ChatFormatting.WHITE,
            ChatFormatting.BOLD);
    this.minecraft.font.drawShadow(matrixStack, gameTitle,
        sbx + sbwidth - 3 - this.minecraft.font.width(gameTitle),
        sby + 9, 0);

    Component mapTitle =
        new TextComponent(this.getDisplayName()).withStyle(ChatFormatting.GRAY);
    this.minecraft.font.drawShadow(matrixStack, mapTitle,
        sbx + sbwidth - 3 - this.minecraft.font.width(mapTitle),
        sby + 20, 0);

    // Render Time
    RenderSystem.enableBlend();
    RenderSystem.setShaderTexture(0, STOPWATCH);
    RenderUtil.blit(matrixStack, sbx + (sbwidth / 2) - 8, sby + 5, 16, 16);
    RenderSystem.disableBlend();
    Component timer = this.getTimer();
    GuiComponent.drawCenteredString(matrixStack, this.minecraft.font, timer,
        sbx + (sbwidth / 2),
        sby + 24, 0);

    // Render Teams and Player Information
    RenderUtil.fillWidthHeight(matrixStack, sbx, sby + 41, sbwidth, 193, 0x80000000);
    RenderUtil.renderPlayerListRow(matrixStack, sbx, sby + 41, sbwidth, 13,
        new TextComponent("Ping"),
        new TextComponent("Username"), new TextComponent("K"),
        new TextComponent("A"), new TextComponent("D"),
        new TextComponent("Score"));

    List<UUID> blueMembers = this.getTeamModule().getTeamInstance(TdmTeam.BLUE).getMembers();
    for (int i = 0; i < blueMembers.size(); i++) {
      PlayerInfo playerInfo =
          this.minecraft.getConnection().getPlayerInfo(blueMembers.get(i));
      if (playerInfo != null) {
        Component username =
            playerInfo.getTabListDisplayName() == null
                ? new TextComponent(playerInfo.getProfile().getName())
                : playerInfo.getTabListDisplayName();
        TdmPlayerData playerData = this.getPlayerData(playerInfo.getProfile().getId());
        RenderUtil.renderPlayerListRow(matrixStack, sbx, sby + 55 + (i * 11), sbwidth, 10,
            new TextComponent(String.valueOf(playerInfo.getLatency())),
            username, new TextComponent(String.valueOf(playerData.getKills())),
            new TextComponent(String.valueOf(playerData.getAssists())),
            new TextComponent(String.valueOf(playerData.getDeaths())),
            new TextComponent(String.valueOf(playerData.getScore())));
      }
    }

    List<UUID> redMembers = this.getTeamModule().getTeamInstance(TdmTeam.RED).getMembers();
    for (int i = 0; i < redMembers.size(); i++) {
      PlayerInfo playerInfo =
          this.minecraft.getConnection().getPlayerInfo(redMembers.get(i));
      if (playerInfo != null) {
        Component username =
            playerInfo.getTabListDisplayName() == null
                ? new TextComponent(playerInfo.getProfile().getName())
                : playerInfo.getTabListDisplayName();
        TdmPlayerData playerData = this.getPlayerData(playerInfo.getProfile().getId());
        RenderUtil.renderPlayerListRow(matrixStack, sbx, sby + 147 + (i * 11), sbwidth, 10,
            new TextComponent(String.valueOf(playerInfo.getLatency())),
            username, new TextComponent(String.valueOf(playerData.getKills())),
            new TextComponent(String.valueOf(playerData.getAssists())),
            new TextComponent(String.valueOf(playerData.getDeaths())),
            new TextComponent(String.valueOf(playerData.getScore())));
      }
    }

    RenderUtil.fillWidthHeight(matrixStack, sbx + 4, sby + 144, sbwidth - 8, 1, 0x80FFFFFF);

    return true;
  }

  @SubscribeEvent
  public void handleLivingLoad(LivingExtensionEvent.Load event) {
    if (event.getLiving() instanceof PlayerExtension<?> player
        && player.getLevel().isClientSide()) {
      player.registerHandler(TdmPlayerHandler.TYPE, new TdmPlayerHandler<>(this, player));
    }
  }

  @SubscribeEvent
  public void handleRenderNameplate(RenderNameplateEvent event) {
    if (event.getEntity() instanceof Player
        && this.minecraft.player instanceof Player) {
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
