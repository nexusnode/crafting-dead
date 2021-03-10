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

import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.client.gui.component.Blur;
import com.craftingdead.immerse.client.gui.screen.game.GameButton;
import com.craftingdead.immerse.game.deathmatch.DeathmatchTeam;
import com.craftingdead.immerse.game.deathmatch.message.RequestJoinTeamMessage;
import com.craftingdead.immerse.game.network.GameNetworkChannel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;

public class SelectTeamScreen extends Screen {

  private final Blur blur = new Blur();

  public SelectTeamScreen() {
    super(Text.translate("gui.screen.select_team.title"));
  }

  @Override
  public void init() {
    super.init();

    this.addButton(new GameButton((int) (this.width * 0.25F) - 50,
        (int) (this.height * 0.5F), 100, 20,
        Text.of("Join Red"),
        btn -> {
          GameNetworkChannel.sendToServer(new RequestJoinTeamMessage(DeathmatchTeam.RED));
          this.onClose();
        }));

    this.addButton(new GameButton(this.width - (int) (this.width * 0.25F) - 50,
        (int) (this.height * 0.5F), 100, 20,
        Text.of("Join Blue"),
        btn -> {
          GameNetworkChannel.sendToServer(new RequestJoinTeamMessage(DeathmatchTeam.BLUE));
          this.onClose();
        }));

    this.addButton(new GameButton(this.width - (int) (this.width * 0.5F) - 25,
        (int) (this.height * 0.75F), 50, 10,
        Text.of("Spectate"),
        btn -> {
          GameNetworkChannel.sendToServer(new RequestJoinTeamMessage(null));
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
    drawCenteredString(matrixStack, this.font, Text.of("Select a Team"), 0, 0, 0xFFFFFFFF);
    matrixStack.popPose();
  }
}
