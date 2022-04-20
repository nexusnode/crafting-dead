/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.GameClient;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public class SurvivalClient extends SurvivalGame implements GameClient {

  private static final ResourceLocation DAYS_SURVIVED =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/days_survived.png");
  private static final ResourceLocation ZOMBIES_KILLED =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/zombies_killed.png");
  private static final ResourceLocation PLAYERS_KILLED =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/players_killed.png");

  private final Minecraft minecraft = Minecraft.getInstance();

  @Override
  public boolean renderOverlay(PlayerExtension<? extends AbstractClientPlayer> player,
      PoseStack poseStack, int width, int height, float partialTick) {
    var survivalPlayer = player.getHandlerOrThrow(SurvivalPlayerHandler.TYPE);
    int y = height / 2;
    int x = 4;

    RenderSystem.enableBlend();

    RenderSystem.setShaderTexture(0, DAYS_SURVIVED);
    RenderUtil.blit(x, y - 20, 16, 16);
    this.minecraft.font.drawShadow(poseStack,
        String.valueOf(survivalPlayer.getDaysSurvived()), x + 20, y - 16, 0xFFFFFF);

    RenderSystem.setShaderTexture(0, ZOMBIES_KILLED);
    RenderUtil.blit(x, y, 16, 16);
    this.minecraft.font.drawShadow(poseStack,
        String.valueOf(survivalPlayer.getZombiesKilled()), x + 20, y + 4, 0xFFFFFF);

    RenderSystem.setShaderTexture(0, PLAYERS_KILLED);
    RenderUtil.blit(x, y + 20, 16, 16);
    this.minecraft.font.drawShadow(poseStack,
        String.valueOf(survivalPlayer.getPlayersKilled()), x + 20, y + 24, 0xFFFFFF);

    RenderSystem.disableBlend();

    return false;
  }
}
