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

package com.craftingdead.core.client.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.world.action.ActionObserver;
import com.craftingdead.core.world.damagesource.KillFeedEntry;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.GunItem;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.magazine.Magazine;
import com.craftingdead.core.world.item.scope.Scope;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

public class IngameGui {

  private static final Random random = new Random();

  private static final ResourceLocation HEALTH =
      new ResourceLocation(CraftingDead.ID, "textures/gui/health.png");
  private static final ResourceLocation SHIELD =
      new ResourceLocation(CraftingDead.ID, "textures/gui/shield.png");

  private static final int KILL_FEED_MESSAGE_LIFE_MS = 5000;

  private final Minecraft minecraft;

  private final ClientDist client;

  private final List<KillFeedEntry> killFeedMessages = new LinkedList<>();

  private ResourceLocation crosshairLocation;

  private float lastSpread;

  private float lastFlashScale = 0;

  @Nullable
  private HitMarker hitMarker;

  private long killFeedVisibleTimeMs;
  private long killFeedAnimationTimeMs;

  public IngameGui(Minecraft minecraft, ClientDist client, ResourceLocation crosshairLocation) {
    this.minecraft = minecraft;
    this.client = client;
    this.crosshairLocation = crosshairLocation;
  }

  public void setCrosshairLocation(ResourceLocation crosshairLocation) {
    this.crosshairLocation = crosshairLocation;
  }

  public void addKillFeedEntry(KillFeedEntry killFeedMessage) {
    if (this.killFeedMessages.isEmpty()) {
      this.killFeedVisibleTimeMs = 0L;
    }
    this.killFeedMessages.add(killFeedMessage);
  }

  public void setHitMarker(@Nullable HitMarker hitMarker) {
    this.hitMarker = hitMarker;
  }

  private void renderGunFlash(PoseStack poseStack, Gun gun, int width, int height,
      float partialTicks) {
    if (gun.getClient().isFlashing()) {
      poseStack.pushPose();
      {
        final var flashIntensity = (random.nextInt(3) + 5) / 10.0F;
        final var scale = this.lastFlashScale =
            Mth.lerp(partialTicks, this.lastFlashScale, flashIntensity);
        RenderSystem.setShaderTexture(0,
            new ResourceLocation(CraftingDead.ID, "textures/flash/white_flash.png"));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, flashIntensity - 0.15F);
        final float x = width * 0.625F;
        final float y = height * 0.625F;
        final float flashWidth = 300;
        final float flashHeight = 300;
        poseStack.translate((x - x * scale), y - y * scale, 0.0F);
        poseStack.scale(scale, scale, 1.0F);
        RenderUtil.blit(x - flashWidth / 2, y - flashHeight / 2, flashWidth,
            flashHeight);
        RenderSystem.disableBlend();
      }
      poseStack.popPose();
    }
  }

  private static void renderScopeOverlay(PlayerExtension<AbstractClientPlayer> player,
      Scope scope, int width, int height) {
    scope.getOverlayTexture(player).ifPresent(overlayTexture -> {
      RenderSystem.setShaderTexture(0, overlayTexture);
      var overlayTextureWidth = scope.getOverlayTextureWidth();
      var overlayTextureHeight = scope.getOverlayTextureHeight();
      var scale = RenderUtil.getFitScale(overlayTextureWidth, overlayTextureHeight);
      overlayTextureWidth *= scale;
      overlayTextureHeight *= scale;
      RenderSystem.enableBlend();
      RenderUtil.blit(width / 2.0F - overlayTextureWidth / 2.0F,
          height / 2.0F - overlayTextureHeight / 2.0F, overlayTextureWidth,
          overlayTextureHeight);
      RenderSystem.disableBlend();
    });
  }

  public void renderOverlay(PlayerExtension<AbstractClientPlayer> player, ItemStack heldStack,
      @Nullable Gun gun, PoseStack poseStack, int width, int height, float partialTicks) {
    if (this.hitMarker != null) {
      if (this.hitMarker.render(poseStack, width, height, partialTicks)) {
        this.hitMarker = null;
      }
    }

    this.renderKillFeed(poseStack, partialTicks);

    heldStack.getCapability(Scope.CAPABILITY)
        .filter(scope -> scope.isScoping(player))
        .ifPresent(scope -> renderScopeOverlay(player, scope, width, height));

    // Draws Flashbang effect
    MobEffectInstance flashEffect =
        player.getEntity()
            .getEffect(ModMobEffects.FLASH_BLINDNESS.get());
    if (flashEffect != null) {
      int alpha = (int) (255F
          * (Mth.clamp(flashEffect.getDuration() - partialTicks, 0, 20) / 20F));
      int flashColour = 0x00FFFFFF | (alpha & 255) << 24;
      RenderUtil.fillGradient(poseStack, 0, 0, width, height, flashColour, flashColour);
    }

    player.getActionObserver()
        .flatMap(ActionObserver::getProgressBar)
        .ifPresent(observer -> renderProgress(poseStack, this.minecraft.font, width,
            height, observer.getMessage(), observer.getSubMessage().orElse(null),
            observer.getProgress(partialTicks)));

    if (gun != null) {
      this.renderGunFlash(poseStack, gun, width, height, partialTicks);
    }

    // Needs to render after blood or else it causes Z level issues
    if (gun != null) {
      this.renderAmmo(poseStack, width, height, gun);
    }

    if (player.isCombatModeEnabled()) {
      this.renderCombatMode(player, poseStack, width, height);
    }

    this.renderHandcuffsDamage(poseStack, player.getHandcuffs(), width, height);
  }

  private void renderHandcuffsDamage(PoseStack poseStack, ItemStack handcuffs,
      int width, int height) {
    if (!handcuffs.isEmpty()) {
      final var mWidth = width / 2;
      final var mHeight = height / 2;
      final var damage = handcuffs.getMaxDamage() - handcuffs.getDamageValue();
      GuiComponent.drawCenteredString(poseStack, this.minecraft.font,
          new TextComponent(damage + "/" + handcuffs.getMaxDamage()), mWidth + 1, mHeight + 10,
          0xFFFFFFFF);

      final var modelViewStack = RenderSystem.getModelViewStack();
      modelViewStack.pushPose();
      {
        modelViewStack.translate(mWidth - 20.0F, mHeight - 30.0F, 0.0F);
        final var scale = 2.5F;
        modelViewStack.scale(scale, scale, scale);
        this.minecraft.getItemRenderer().renderGuiItem(handcuffs, 0, 0);
      }
      modelViewStack.popPose();
      RenderSystem.applyModelViewMatrix();
    }
  }

  private void renderKillFeed(PoseStack poseStack, float partialTicks) {
    if (this.killFeedVisibleTimeMs == 0L) {
      this.killFeedVisibleTimeMs = Util.getMillis();
      this.killFeedAnimationTimeMs = 0L;
    }

    final long currentTime = Util.getMillis();
    float durationPct = Mth.clamp(
        (float) (currentTime - this.killFeedVisibleTimeMs) / KILL_FEED_MESSAGE_LIFE_MS, 0.0F, 1.0F);
    if (durationPct == 1.0F && !this.killFeedMessages.isEmpty()) {
      this.killFeedMessages.remove(0);
      if (!this.killFeedMessages.isEmpty()) {
        this.killFeedVisibleTimeMs = Util.getMillis();
        this.killFeedAnimationTimeMs = 0L;
      }
    } else if (durationPct >= 0.75F && this.killFeedAnimationTimeMs == 0L) {
      this.killFeedAnimationTimeMs = Util.getMillis();
    }

    float animationPct =
        this.killFeedAnimationTimeMs != 0L
            ? Mth.clamp((float) (currentTime - this.killFeedAnimationTimeMs)
                / ((KILL_FEED_MESSAGE_LIFE_MS / 4.0F) - partialTicks), 0.0F, 1.0F)
            : 0.0F;

    final int killFeedMessageX = 5;
    for (int i = 0; i < this.killFeedMessages.size(); i++) {
      final KillFeedEntry killFeedMessage = this.killFeedMessages.get(i);
      float killFeedMessageY = 5.0F + ((i - (1.0F * animationPct)) * 12.0F);
      this.renderKillFeedEntry(killFeedMessage, poseStack, killFeedMessageX, killFeedMessageY,
          i == 0 ? 1.0F - animationPct : 1.0F);
    }
  }

  private void renderKillFeedEntry(KillFeedEntry entry, PoseStack poseStack,
      float x, float y, float alpha) {
    final String killerName = entry.getKillerName().getString();
    final String deadName = entry.getDeadName().getString();
    final int killerNameWidth = this.minecraft.font.width(killerName);
    final int deadNameWidth = this.minecraft.font.width(deadName);

    int spacing = 20;
    alpha *= entry.getKillerEntityId() == this.minecraft.player.getId() ? 0.7F : 0.5F;

    switch (entry.getType()) {
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

    switch (entry.getType()) {
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

    if (!entry.getWeaponStack().isEmpty()) {
      poseStack.pushPose();
      {
        poseStack.translate(x + killerNameWidth + 4, y - 1, 0);

        if (entry.getWeaponStack().getItem() instanceof GunItem) {
          var scale = 0.75F;
          poseStack.scale(scale, scale, scale);
        }

        // if (this.itemStack.getItem() instanceof ItemKnife) {
        // double scale = 0.6D;
        // GL11.glScaled(scale, scale, scale);
        // GL11.glRotated(180, 0, 1, 0);
        // GL11.glRotated(-20, 0, 0, 1);
        // }

        if (entry.getWeaponStack().getItem() instanceof GrenadeItem) {
          var scale = 0.8F;
          poseStack.scale(scale, scale, scale);
          poseStack.translate(4, 1, 0);
        }

        RenderUtil.renderGuiItem(entry.getWeaponStack(), 0, 0, 0xFFFFFF + (opacity << 24));
      }
      poseStack.popPose();
    }
  }

  private void renderAmmo(PoseStack poseStack, int width, int height, Gun gun) {
    int x = width - 115;
    int boxHeight = 25;

    RenderUtil.fillGradient(poseStack, x - 10, height - boxHeight, x + 30, height, 0x00000000,
        0x55000000);
    GuiComponent.fill(poseStack, x + 30, height - boxHeight, x + 30 + 90, height, 0x55000000);

    AmmoProvider ammoProvider = gun.getAmmoProvider();
    int ammoCount = ammoProvider.getMagazine().map(Magazine::getSize).orElse(0);
    int reserveSize = ammoProvider.getReserveSize();
    var empty = ammoCount == 0 && reserveSize == 0;

    var ammoText = empty ? I18n.get("hud.empty_magazine")
        : String.valueOf(ammoCount);
    int ammoTextWidth = this.minecraft.font.width(ammoText);

    float reserveTextScale = 0.6F;
    String reserveText = " / " + reserveSize;
    int reserveTextWidth =
        (int) (this.minecraft.font.width(reserveText) * reserveTextScale);

    this.minecraft.font.drawShadow(poseStack,
        ammoText,
        x + 55 - ammoTextWidth - (empty ? 0 : reserveTextWidth),
        height - (boxHeight / 2) - this.minecraft.font.lineHeight / 2,
        empty ? ChatFormatting.RED.getColor() : 0xFFFFFFFF);

    if (!empty) {
      poseStack.pushPose();
      poseStack.translate(x + 55 - reserveTextWidth,
          height - (boxHeight / 2) - (reserveTextScale * 2), 0);
      poseStack.scale(reserveTextScale, reserveTextScale, reserveTextScale);
      this.minecraft.font.drawShadow(poseStack,
          reserveText, 0, 0, empty ? ChatFormatting.RED.getColor() : 0xFFFFFFFF);
      poseStack.popPose();
    }

    String fireMode = I18n.get(gun.getFireMode().getTranslationKey());
    this.minecraft.font.drawShadow(poseStack,
        fireMode, x + 65, height - 16, 0xFFFFFFFF);
  }

  private static void renderProgress(PoseStack poseStack, Font font,
      int width, int height, Component message, @Nullable Component subMessage, float percent) {
    final int barWidth = 100;
    final int barHeight = 10;
    final int barColour = 0xC0FFFFFF;
    final float x = width / 2 - barWidth / 2;
    final float y = height / 2;
    font.drawShadow(poseStack, message.getString(), x,
        y - barHeight - ((font.lineHeight / 2) + 0.5F), 0xFFFFFF);
    RenderUtil.fillGradient(poseStack, x, y, x + barWidth * percent, y + barHeight, barColour,
        barColour);
    if (subMessage != null) {
      font.drawShadow(poseStack, subMessage.getString(), x,
          y + barHeight + ((font.lineHeight / 2) + 0.5F), 0xFFFFFF);
    }
  }

  private void renderCombatMode(PlayerExtension<AbstractClientPlayer> player,
      PoseStack poseStack, int width, int height) {
    final var inventory = player.getEntity().getInventory();

    int boxX = width - 115;
    int boxWidth = 110;
    int boxY = height - 170;
    int boxHeight = 30;
    int boxMarginY = 31;

    int currentItemIndex = inventory.selected;

    // Render primary
    var primaryStack = inventory.getItem(0);
    if (currentItemIndex == 0) {
      RenderUtil.fill(poseStack, boxX + 1, boxY + 1, boxWidth - 2, boxHeight - 2, 0xCCFFFFFF);
    }
    RenderUtil.fill(poseStack, boxX, boxY, boxWidth, boxHeight, 0x66000000);
    this.minecraft.font.drawShadow(poseStack, "1", boxX + 5, boxY + 5,
        0xFFFFFFFF);
    final var modelViewStack = RenderSystem.getModelViewStack();
    modelViewStack.pushPose();
    {
      modelViewStack.translate(boxX + boxWidth / 2 - 16 / 2,
          boxY + (boxHeight / 2) - 16 / 2, 0);
      modelViewStack.scale(1.2F, 1.2F, 1.2F);
      RenderUtil.renderGuiItem(primaryStack, 0, 0, -1, ItemTransforms.TransformType.FIXED);
    }
    modelViewStack.popPose();
    RenderSystem.applyModelViewMatrix();

    // Render secondary
    var secondaryStack = inventory.getItem(1);
    boxY += boxMarginY;
    if (currentItemIndex == 1) {
      RenderUtil.fill(poseStack, boxX + 1, boxY + 1, boxWidth - 2, boxHeight - 2, 0xCCFFFFFF);
    }
    RenderUtil.fill(poseStack, boxX, boxY, boxWidth, boxHeight, 0x66000000);
    this.minecraft.font.drawShadow(poseStack, "2", boxX + 5, boxY + 5,
        0xFFFFFFFF);
    modelViewStack.pushPose();
    {
      modelViewStack.translate(boxX + boxWidth / 2 - 16 / 2,
          boxY + (boxHeight / 2) - 16 / 2, 0);
      modelViewStack.scale(1.2F, 1.2F, 1.2F);
      RenderUtil.renderGuiItem(secondaryStack, 0, 0, 0xFFFFFFFF,
          ItemTransforms.TransformType.FIXED);
    }
    modelViewStack.popPose();
    RenderSystem.applyModelViewMatrix();

    // Render melee
    var meleeStack = inventory.getItem(2);
    boxY += boxMarginY;
    if (currentItemIndex == 2) {
      RenderUtil.fill(poseStack, boxX + 1, boxY + 1, boxWidth - 2, boxHeight - 2, 0xCCFFFFFF);
    }
    RenderUtil.fill(poseStack, boxX, boxY, boxWidth, boxHeight, 0x66000000);
    this.minecraft.font.drawShadow(poseStack, "3", boxX + 5, boxY + 5,
        0xFFFFFFFF);
    modelViewStack.pushPose();
    {
      modelViewStack.translate(boxX + boxWidth / 2 - 16 / 2,
          boxY + (boxHeight / 2) - 16 / 2, 0);
      modelViewStack.scale(1.2F, 1.2F, 1.2F);
      RenderUtil.renderGuiItem(meleeStack, 0, 0, 0xFFFFFFFF,
          ItemTransforms.TransformType.FIXED);
    }
    modelViewStack.popPose();
    RenderSystem.applyModelViewMatrix();

    // Render extras
    boxY += boxMarginY;
    boxHeight = 25;
    boxWidth = 25;
    for (int i = 0; i < 4; i++) {
      ItemStack extraStack = inventory.getItem(3 + i);
      if (currentItemIndex == 3 + i) {
        RenderUtil.fill(poseStack, boxX + 1, boxY + 1, boxWidth - 2, boxHeight - 2, 0xCCFFFFFF);
      }
      RenderUtil.fill(poseStack, boxX, boxY, boxWidth, boxHeight, 0x66000000);
      this.minecraft.font.drawShadow(poseStack, String.valueOf(4 + i), boxX + 1,
          boxY + 1, 0xFFFFFFFF);

      RenderUtil.renderGuiItem(extraStack, boxX + boxWidth / 2 - 16 / 2,
          boxY + (boxHeight / 2) - 6, 0xFFFFFFFF);

      boxX += 28;
    }

    final int healthBoxHeight = 25;
    // Render Health
    final var health = player.getEntity().getHealth();
    final var armour = player.getEntity().getArmorValue();

    int healthWidth = 100;
    if (armour > 0) {
      healthWidth *= 2;
    }

    GuiComponent.fill(poseStack, 0, height - healthBoxHeight, healthWidth, height, 0x55000000);

    RenderUtil.fillGradient(poseStack, healthWidth, height - healthBoxHeight, healthWidth + 40,
        height, 0x55000000, 0x00000000);

    RenderSystem.setShaderTexture(0, HEALTH);
    RenderSystem.enableBlend();
    RenderUtil.blit(5, height - healthBoxHeight / 2 - 8, 16, 16);
    RenderSystem.disableBlend();

    GuiComponent.drawCenteredString(poseStack, this.minecraft.font,
        String.valueOf(Math.round(health)), 31,
        height - healthBoxHeight / 2 - this.minecraft.font.lineHeight / 2, 0xFFFFFFFF);
    RenderUtil.fill(poseStack, 42, height - healthBoxHeight / 2 - 5, 65, 10, 0x66000000);
    RenderUtil.fill(poseStack, 42, height - healthBoxHeight / 2 - 5,
        Math.round(65 * (health / player.getEntity().getMaxHealth())), 10, 0xCCFFFFFF);

    if (armour > 0) {
      final var armourX = healthWidth / 2 + 7;
      RenderSystem.setShaderTexture(0, SHIELD);
      RenderSystem.enableBlend();
      RenderUtil.blit(armourX + 5, height - healthBoxHeight / 2 - 8, 16, 16);
      RenderSystem.disableBlend();

      GuiComponent.drawCenteredString(poseStack, this.minecraft.font,
          String.valueOf(Math.round(armour)), armourX + 31,
          height - healthBoxHeight / 2 - this.minecraft.font.lineHeight / 2, 0xFFFFFFFF);
      RenderUtil.fill(poseStack, armourX + 42, height - healthBoxHeight / 2 - 5, 65, 10,
          0x66000000);
      RenderUtil.fill(poseStack, armourX + 42, height - healthBoxHeight / 2 - 5,
          Math.round(65 * (armour / 20)), 10, 0xCCFFFFFF);
    }
  }

  public void renderCrosshairs(PoseStack poseStack, float accuracy, float partialTicks, int width,
      int height) {
    final var imageWidth = 16.0F;
    final var imageHeight = 16.0F;

    final var x = (width / 2.0F) - (imageWidth / 2.0F) - 0.5F;
    final var y = (height / 2.0F) - (imageHeight / 2.0F);

    final var newSpread = (1.15F - accuracy) * 60.0F;
    final var lerpSpread = Mth.lerp(0.5F, this.lastSpread, newSpread);
    final var crosshair = this.client.getCrosshairManager().getCrosshair(this.crosshairLocation);

    poseStack.pushPose();
    {
      RenderSystem.enableBlend();

      RenderSystem.setShaderTexture(0, crosshair.getMiddle());
      RenderUtil.blit(x, y, imageWidth, imageHeight);

      RenderSystem.setShaderTexture(0, crosshair.getTop());
      RenderUtil.blit(x, y - lerpSpread, imageWidth, imageHeight);

      RenderSystem.setShaderTexture(0, crosshair.getBottom());
      RenderUtil.blit(x, y + lerpSpread, imageWidth, imageHeight);

      RenderSystem.setShaderTexture(0, crosshair.getLeft());
      RenderUtil.blit(x - lerpSpread, y, imageWidth, imageHeight);

      RenderSystem.setShaderTexture(0, crosshair.getRight());
      RenderUtil.blit(x + lerpSpread, y, imageWidth, imageHeight);
      RenderSystem.disableBlend();
    }
    poseStack.popPose();

    this.lastSpread = lerpSpread;
  }
}
