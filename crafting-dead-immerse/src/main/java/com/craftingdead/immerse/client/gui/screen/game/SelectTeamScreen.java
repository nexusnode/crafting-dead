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

package com.craftingdead.immerse.client.gui.screen.game;

import com.craftingdead.immerse.client.gui.screen.game.shop.GameButton;
import com.craftingdead.immerse.client.gui.view.Blur;
import com.craftingdead.immerse.game.network.GameNetworkChannel;
import com.craftingdead.immerse.game.tdm.TdmTeam;
import com.craftingdead.immerse.game.tdm.message.RequestJoinTeamMessage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class SelectTeamScreen extends Screen {

  private static final Component TITLE =
      new TranslatableComponent("gui.screen.select_team.title");
  private static final Component JOIN_RED =
      new TranslatableComponent("gui.screen.select_team.join_red");
  private static final Component JOIN_BLUE =
      new TranslatableComponent("gui.screen.select_team.join_blue");
  private static final Component SPECTATE =
      new TranslatableComponent("gui.screen.select_team.spectate");

  private final Blur blur = new Blur();

  public SelectTeamScreen() {
    super(TITLE);
  }

  @Override
  public void init() {
    super.init();

    this.addRenderableWidget(new GameButton((int) (this.width * 0.25F) - 50,
        (int) (this.height * 0.5F), 100, 20,
        JOIN_RED,
        btn -> {
          GameNetworkChannel.sendToServer(null, new RequestJoinTeamMessage(TdmTeam.RED));
          this.onClose();
        }));

    this.addRenderableWidget(new GameButton(this.width - (int) (this.width * 0.25F) - 50,
        (int) (this.height * 0.5F), 100, 20,
        JOIN_BLUE,
        btn -> {
          GameNetworkChannel.sendToServer(null, new RequestJoinTeamMessage(TdmTeam.BLUE));
          this.onClose();
        }));

    this.addRenderableWidget(new GameButton(this.width - (int) (this.width * 0.5F) - 25,
        (int) (this.height * 0.75F), 50, 10,
        SPECTATE,
        btn -> {
          GameNetworkChannel.sendToServer(null, new RequestJoinTeamMessage(null));
          this.onClose();
        }));
  }

  @Override
  public void tick() {
    super.tick();
    this.blur.tick();
  }

  @Override
  public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    this.blur.process(partialTicks);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    this.blur.render(matrixStack, 0, 0, this.width, this.height);

    super.render(matrixStack, mouseX, mouseY, partialTicks);

    matrixStack.pushPose();
    matrixStack.translate(this.width / 2, this.height / 4, 0);
    matrixStack.scale(1.5F, 1.5F, 1.5F);
    drawCenteredString(matrixStack, this.font, TITLE, 0, 0, 0xFFFFFFFF);
    matrixStack.popPose();
  }
}
