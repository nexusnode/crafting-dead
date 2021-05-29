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

package com.craftingdead.immerse.client.gui.screen.game;

import com.craftingdead.immerse.client.gui.screen.game.shop.GameButton;
import com.craftingdead.immerse.client.gui.view.Blur;
import com.craftingdead.immerse.game.network.GameNetworkChannel;
import com.craftingdead.immerse.game.tdm.TdmTeam;
import com.craftingdead.immerse.game.tdm.message.RequestJoinTeamMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SelectTeamScreen extends Screen {

  private static final ITextComponent TITLE =
      new TranslationTextComponent("gui.screen.select_team.title");
  private static final ITextComponent JOIN_RED =
      new TranslationTextComponent("gui.screen.select_team.join_red");
  private static final ITextComponent JOIN_BLUE =
      new TranslationTextComponent("gui.screen.select_team.join_blue");
  private static final ITextComponent SPECTATE =
      new TranslationTextComponent("gui.screen.select_team.spectate");

  private final Blur blur = new Blur();

  public SelectTeamScreen() {
    super(TITLE);
  }

  @Override
  public void init() {
    super.init();

    this.addButton(new GameButton((int) (this.width * 0.25F) - 50,
        (int) (this.height * 0.5F), 100, 20,
        JOIN_RED,
        btn -> {
          GameNetworkChannel.sendToServer(null, new RequestJoinTeamMessage(TdmTeam.RED));
          this.onClose();
        }));

    this.addButton(new GameButton(this.width - (int) (this.width * 0.25F) - 50,
        (int) (this.height * 0.5F), 100, 20,
        JOIN_BLUE,
        btn -> {
          GameNetworkChannel.sendToServer(null, new RequestJoinTeamMessage(TdmTeam.BLUE));
          this.onClose();
        }));

    this.addButton(new GameButton(this.width - (int) (this.width * 0.5F) - 25,
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
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    this.blur.render(matrixStack, 0, 0, this.width, this.height, partialTicks);

    super.render(matrixStack, mouseX, mouseY, partialTicks);

    matrixStack.pushPose();
    matrixStack.translate(this.width / 2, this.height / 4, 0);
    matrixStack.scale(1.5F, 1.5F, 1.5F);
    drawCenteredString(matrixStack, this.font, TITLE, 0, 0, 0xFFFFFFFF);
    matrixStack.popPose();
  }
}
