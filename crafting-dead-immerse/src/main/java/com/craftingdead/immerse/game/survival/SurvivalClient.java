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
import com.craftingdead.core.event.LivingExtensionEvent;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.ClientDist;
import com.craftingdead.immerse.game.GameClient;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SurvivalClient extends SurvivalGame implements GameClient {

  private static final ResourceLocation DAYS_SURVIVED =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/days_survived.png");
  private static final ResourceLocation ZOMBIES_KILLED =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/zombies_killed.png");
  private static final ResourceLocation PLAYERS_KILLED =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/players_killed.png");
  private static final ResourceLocation ICONS =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/icons.png");

  private final Minecraft minecraft = Minecraft.getInstance();

  private boolean showStats = true;

  public SurvivalClient() {
    super(false);
  }

  @SubscribeEvent
  public void handleLivingExtensionLoad(LivingExtensionEvent.Load event) {
    if (event.getLiving() instanceof PlayerExtension<?> player
        && player.level().isClientSide()) {
      player.registerHandler(SurvivalPlayerHandler.TYPE, new SurvivalPlayerHandler(this, player));
    }
  }

  @Override
  public boolean renderOverlay(PlayerExtension<? extends AbstractClientPlayer> player,
      PoseStack poseStack, int width, int height, float partialTick) {
    if (this.showStats) {
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

      // Only draw in survival
      if (this.minecraft.gameMode.canHurtPlayer() && !player.isCombatModeEnabled()) {
        // Only render when air level is not being rendered
        if (this.isThirstEnabled()
            && !player.entity().isEyeInFluid(FluidTags.WATER)
            && player.entity().getAirSupply() == player.entity().getMaxAirSupply()) {
          renderWater(width, height,
              (float) survivalPlayer.getWater() / (float) survivalPlayer.getMaxWater(), ICONS);
        }
      }
    }

    return false;
  }

  private static void renderWater(int width, int height, float waterPercentage,
      ResourceLocation resourceLocation) {
    final int y = height - 49;
    final int x = width / 2 + 91;
    RenderSystem.enableBlend();
    RenderSystem.setShaderTexture(0, resourceLocation);

    for (int i = 0; i < 10; i++) {
      // Draw droplet outline
      RenderUtil.blit(x - i * 8 - 9, y, 9, 9, 0, 32);

      float scaledWater = 10.0F * waterPercentage;
      if (i + 1 <= scaledWater) {
        // Draw full droplet
        RenderUtil.blit(x - i * 8 - 9, y, 9, 9, 9, 32);
      } else if (scaledWater >= i + 0.5F) {
        // Draw half droplet
        RenderUtil.blit(x - i * 8 - 9, y, 9, 9, 18, 32);
      }
    }
    RenderSystem.disableBlend();
  }

  @Override
  public void tick() {
    super.tick();
    while (ClientDist.TOGGLE_STATS.consumeClick()) {
      this.showStats = !this.showStats;
    }
  }
}
