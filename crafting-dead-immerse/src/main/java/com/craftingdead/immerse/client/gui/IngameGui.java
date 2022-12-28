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

package com.craftingdead.immerse.client.gui;

import java.util.ArrayDeque;
import java.util.Queue;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.GunItem;
import com.craftingdead.immerse.world.KillFeedEntry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.Util;

public class IngameGui {

  private static final int KILLED_MESSAGE_LIFE_MS = 5 * 1000;

  private static final int KILL_FEED_MESSAGE_LIFE_MS = 5000;

  private final Minecraft minecraft = Minecraft.getInstance();

  private final Queue<KillFeedEntry> killFeedMessages = new ArrayDeque<>();

  private long killedMessageVisibleTimeMs;

  private KilledMessage killedMessage;

  private long killFeedVisibleTimeMs;
  private long killFeedAnimationTimeMs;

  public void addKillFeedEntry(KillFeedEntry killFeedMessage) {
    if (this.killFeedMessages.isEmpty()) {
      this.killFeedVisibleTimeMs = 0L;
    }
    this.killFeedMessages.add(killFeedMessage);
  }

  public void renderOverlay(PlayerExtension<AbstractClientPlayer> player, PoseStack poseStack,
      int width, int height, float partialTick) {
    this.renderKilledMessage(poseStack, width, height);
    this.renderKillFeed(poseStack, partialTick);
  }

  private void renderKilledMessage(PoseStack poseStack, int width, int height) {
    if (this.killedMessage != null) {
      final long currentTime = Util.getMillis();
      if (currentTime - this.killedMessageVisibleTimeMs > KILLED_MESSAGE_LIFE_MS) {
        this.killedMessage = null;
        return;
      }
      this.killedMessage.render(poseStack, this.minecraft.font, width, height);
    }
  }

  public void displayKilledMessage(KilledMessage killedMessage) {
    this.killedMessageVisibleTimeMs = Util.getMillis();
    this.killedMessage = killedMessage;
  }

  private void renderKillFeed(PoseStack poseStack, float partialTick) {
    if (this.killFeedVisibleTimeMs == 0L) {
      this.killFeedVisibleTimeMs = Util.getMillis();
      this.killFeedAnimationTimeMs = 0L;
    }

    final long currentTime = Util.getMillis();
    float durationPct = Mth.clamp(
        (float) (currentTime - this.killFeedVisibleTimeMs) / KILL_FEED_MESSAGE_LIFE_MS, 0.0F, 1.0F);
    if (durationPct == 1.0F && !this.killFeedMessages.isEmpty()) {
      this.killFeedMessages.poll();
      if (!this.killFeedMessages.isEmpty()) {
        this.killFeedVisibleTimeMs = Util.getMillis();
        this.killFeedAnimationTimeMs = 0L;
      }
    } else if (durationPct >= 0.75F && this.killFeedAnimationTimeMs == 0L) {
      this.killFeedAnimationTimeMs = Util.getMillis();
    }

    float animationPct = this.killFeedAnimationTimeMs != 0L
        ? Mth.clamp((float) (currentTime - this.killFeedAnimationTimeMs)
            / ((KILL_FEED_MESSAGE_LIFE_MS / 4.0F) - partialTick), 0.0F, 1.0F)
        : 0.0F;

    final int killFeedMessageX = 5;
    int i = 0;
    for (var killFeedMessage : this.killFeedMessages) {
      float killFeedMessageY = 5.0F + ((i - (1.0F * animationPct)) * 12.0F);
      this.renderKillFeedEntry(killFeedMessage, poseStack, killFeedMessageX, killFeedMessageY,
          i == 0 ? 1.0F - animationPct : 1.0F);
      i++;
    }
  }

  private void renderKillFeedEntry(KillFeedEntry entry, PoseStack poseStack,
      float x, float y, float alpha) {
    var killerName = entry.killerName();
    var deadName = entry.targetName();
    int killerNameWidth = this.minecraft.font.width(killerName);
    int deadNameWidth = this.minecraft.font.width(deadName);

    int spacing = 20;
    alpha *= entry.killerEntityId() == this.minecraft.player.getId() ? 0.7F : 0.5F;

    switch (entry.type()) {
      case WALLBANG_HEADSHOT:
        spacing += 16;
      case HEADSHOT:
      case WALLBANG:
        spacing += 16;
        break;
      default:
        break;
    }

    final int opacity = Math.min((int) (alpha * 255.0F), 255);
    if (opacity < 8) {
      return;
    }

    int colour = 0x000000 + (opacity << 24);
    RenderUtil.fillGradient(poseStack, x, y,
        x + killerNameWidth + deadNameWidth + spacing, y + 11, colour, colour);

    this.minecraft.font.drawShadow(poseStack, killerName,
        x + 2, y + 2, 0xFFFFFF + ((int) (alpha * 255.0F) << 24));
    this.minecraft.font.drawShadow(poseStack, deadName,
        x + killerNameWidth + spacing - 1, y + 2, 0xFFFFFF + (opacity << 24));

    switch (entry.type()) {
      case HEADSHOT:
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.setShaderTexture(0,
            new ResourceLocation(CraftingDead.ID, "textures/gui/headshot.png"));
        RenderUtil.blit(x + killerNameWidth + 17, y - 1, 12, 12);
        RenderSystem.disableBlend();
        break;
      case WALLBANG:
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.setShaderTexture(0,
            new ResourceLocation(CraftingDead.ID, "textures/gui/wallbang.png"));
        RenderUtil.blit(x + killerNameWidth + 35, y - 1, 12, 12);
        RenderSystem.disableBlend();
        break;
      case WALLBANG_HEADSHOT:
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.setShaderTexture(0,
            new ResourceLocation(CraftingDead.ID, "textures/gui/wallbang.png"));
        RenderUtil.blit(x + killerNameWidth + 35, y - 1, 12, 12);
        RenderSystem.setShaderTexture(0,
            new ResourceLocation(CraftingDead.ID, "textures/gui/headshot.png"));
        RenderUtil.blit(x + killerNameWidth + 35 + 14, y - 1, 12, 12);
        RenderSystem.disableBlend();
        break;
      default:
        break;
    }

    if (!entry.weaponStack().isEmpty()) {
      poseStack.pushPose();
      {
        poseStack.translate(x + killerNameWidth + 4, y - 1, 0);

        if (entry.weaponStack().getItem() instanceof GunItem) {
          var scale = 0.75F;
          poseStack.scale(scale, scale, scale);
        }

        // if (this.itemStack.getItem() instanceof ItemKnife) {
        // double scale = 0.6D;
        // GL11.glScaled(scale, scale, scale);
        // GL11.glRotated(180, 0, 1, 0);
        // GL11.glRotated(-20, 0, 0, 1);
        // }

        if (entry.weaponStack().getItem() instanceof GrenadeItem) {
          var scale = 0.8F;
          poseStack.scale(scale, scale, scale);
          poseStack.translate(4, 1, 0);
        }

        RenderUtil.renderGuiItem(poseStack, entry.weaponStack(), alpha);
      }
      poseStack.popPose();
    }
  }
}
