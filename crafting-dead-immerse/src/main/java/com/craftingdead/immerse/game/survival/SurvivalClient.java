/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.immerse.game.IGameClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.util.ResourceLocation;

public class SurvivalClient extends SurvivalGame implements IGameClient<SurvivorsTeam> {

  private static final ResourceLocation DAYS_SURVIVED =
      new ResourceLocation(CraftingDead.ID, "textures/gui/hud/days_survived.png");
  private static final ResourceLocation ZOMBIES_KILLED =
      new ResourceLocation(CraftingDead.ID, "textures/gui/hud/zombies_killed.png");
  private static final ResourceLocation PLAYERS_KILLED =
      new ResourceLocation(CraftingDead.ID, "textures/gui/hud/players_killed.png");

  @Override
  public void renderOverlay(Minecraft minecraft,
      IPlayer<? extends AbstractClientPlayerEntity> player, int width, int height,
      float partialTicks) {
    SurvivalPlayer survivalPlayer = SurvivalPlayer.getExpected(player);
    int y = height / 2;
    int x = 4;

    RenderSystem.enableBlend();

    RenderUtil.bind(DAYS_SURVIVED);
    RenderUtil.drawTexturedRectangle(x, y - 20, 16, 16);
    minecraft.fontRenderer.drawStringWithShadow(String.valueOf(survivalPlayer.getDaysSurvived()),
        x + 20, y - 16, 0xFFFFFF);

    RenderUtil.bind(ZOMBIES_KILLED);
    RenderUtil.drawTexturedRectangle(x, y, 16, 16);
    minecraft.fontRenderer.drawStringWithShadow(String.valueOf(survivalPlayer.getZombiesKilled()),
        x + 20, y + 4, 0xFFFFFF);

    RenderUtil.bind(PLAYERS_KILLED);
    RenderUtil.drawTexturedRectangle(x, y + 20, 16, 16);
    minecraft.fontRenderer.drawStringWithShadow(String.valueOf(survivalPlayer.getPlayersKilled()),
        x + 20, y + 24, 0xFFFFFF);

    RenderSystem.disableBlend();
  }
}
