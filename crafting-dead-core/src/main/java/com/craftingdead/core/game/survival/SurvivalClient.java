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
package com.craftingdead.core.game.survival;

import java.util.HashSet;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.game.AbstractGame;
import com.craftingdead.core.game.GameTypes;
import com.craftingdead.core.game.IGameClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.util.ResourceLocation;

public class SurvivalClient extends
    AbstractGame<SurvivorsTeam, SurvivalPlayer<AbstractClientPlayerEntity>, AbstractClientPlayerEntity>
    implements IGameClient<SurvivorsTeam, SurvivalPlayer<AbstractClientPlayerEntity>> {

  private static final ResourceLocation DAYS_SURVIVED =
      new ResourceLocation(CraftingDead.ID, "textures/gui/hud/days_survived.png");
  private static final ResourceLocation ZOMBIES_KILLED =
      new ResourceLocation(CraftingDead.ID, "textures/gui/hud/zombies_killed.png");
  private static final ResourceLocation PLAYERS_KILLED =
      new ResourceLocation(CraftingDead.ID, "textures/gui/hud/players_killed.png");

  public SurvivalClient() {
    super(GameTypes.SURVIVAL, new HashSet<>());
  }

  @Override
  public SurvivalPlayer<AbstractClientPlayerEntity> createPlayer(
      AbstractClientPlayerEntity playerEntity) {
    return new SurvivalPlayer<>(playerEntity);
  }

  @Override
  public void renderHud(Minecraft minecraft, SurvivalPlayer<AbstractClientPlayerEntity> player,
      int width, int height, float partialTicks) {
    int y = height / 2;
    int x = 4;

    RenderSystem.enableBlend();

    RenderUtil.bind(DAYS_SURVIVED);
    RenderUtil.drawTexturedRectangle(x, y - 20, 16, 16);
    minecraft.fontRenderer.drawStringWithShadow(String.valueOf(player.getDaysSurvived()), x + 20,
        y - 16, 0xFFFFFF);

    RenderUtil.bind(ZOMBIES_KILLED);
    RenderUtil.drawTexturedRectangle(x, y, 16, 16);
    minecraft.fontRenderer.drawStringWithShadow(String.valueOf(player.getZombiesKilled()), x + 20,
        y + 4, 0xFFFFFF);

    RenderUtil.bind(PLAYERS_KILLED);
    RenderUtil.drawTexturedRectangle(x, y + 20, 16, 16);
    minecraft.fontRenderer.drawStringWithShadow(String.valueOf(player.getPlayersKilled()), x + 20,
        y + 24, 0xFFFFFF);

    RenderSystem.disableBlend();
  }
}
