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

package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.IGameClient;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.util.ResourceLocation;

public class SurvivalClient extends SurvivalGame implements IGameClient {

  private static final ResourceLocation DAYS_SURVIVED =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/days_survived.png");
  private static final ResourceLocation ZOMBIES_KILLED =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/zombies_killed.png");
  private static final ResourceLocation PLAYERS_KILLED =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/players_killed.png");

  private final Minecraft minecraft = Minecraft.getInstance();

  @Override
  public void renderOverlay(
      IPlayer<? extends AbstractClientPlayerEntity> player, MatrixStack matrixStack, int width,
      int height, float partialTicks) {
    SurvivalPlayer survivalPlayer =
        (SurvivalPlayer) player.getExpectedExtension(SurvivalPlayer.EXTENSION_ID);
    int y = height / 2;
    int x = 4;

    RenderSystem.enableBlend();

    RenderUtil.bind(DAYS_SURVIVED);
    RenderUtil.drawTexturedRectangle(x, y - 20, 16, 16);
    this.minecraft.fontRenderer.drawStringWithShadow(matrixStack,
        String.valueOf(survivalPlayer.getDaysSurvived()), x + 20, y - 16, 0xFFFFFF);

    RenderUtil.bind(ZOMBIES_KILLED);
    RenderUtil.drawTexturedRectangle(x, y, 16, 16);
    this.minecraft.fontRenderer.drawStringWithShadow(matrixStack,
        String.valueOf(survivalPlayer.getZombiesKilled()), x + 20, y + 4, 0xFFFFFF);

    RenderUtil.bind(PLAYERS_KILLED);
    RenderUtil.drawTexturedRectangle(x, y + 20, 16, 16);
    this.minecraft.fontRenderer.drawStringWithShadow(matrixStack,
        String.valueOf(survivalPlayer.getPlayersKilled()), x + 20, y + 24, 0xFFFFFF);

    RenderSystem.disableBlend();
  }

  @Override
  public void renderPlayerList(
      IPlayer<? extends AbstractClientPlayerEntity> player, MatrixStack matrixStack, int width,
      int height, float partialTicks) {
    this.minecraft.ingameGUI.getTabList().func_238523_a_(matrixStack, width,
        this.minecraft.world.getScoreboard(),
        this.minecraft.world.getScoreboard().getObjectiveInDisplaySlot(0));
  }
}
