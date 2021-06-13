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

package com.craftingdead.core.client.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.client.crosshair.Crosshair;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.world.damagesource.KillFeedEntry;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.gun.Gun;
import com.craftingdead.core.world.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.gun.magazine.Magazine;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.GunItem;
import com.craftingdead.core.world.item.scope.Scope;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tiviacz.travelersbackpack.capability.CapabilityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

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

  public void addKillFeedMessage(KillFeedEntry killFeedMessage) {
    if (this.killFeedMessages.isEmpty()) {
      this.killFeedVisibleTimeMs = 0L;
    }
    this.killFeedMessages.add(killFeedMessage);
  }

  public void displayHitMarker(HitMarker hitMarker) {
    this.hitMarker = hitMarker;
  }

  @SuppressWarnings("deprecation")
  private void renderGunFlash(AbstractClientPlayerEntity playerEntity, Gun gun, int width,
      int height,
      float partialTicks) {
    if (gun.getClient().isFlashing()) {
      RenderSystem.pushMatrix();
      {
        final float flashIntensity = (random.nextInt(3) + 5) / 10.0F;
        final float scale = this.lastFlashScale =
            MathHelper.lerp(partialTicks, this.lastFlashScale, flashIntensity);
        this.minecraft.getTextureManager()
            .bind(
                new ResourceLocation(CraftingDead.ID, "textures/flash/white_flash.png"));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, flashIntensity - 0.15F);
        final float x = width * 0.625F;
        final float y = height * 0.625F;
        final float flashWidth = 300;
        final float flashHeight = 300;
        RenderSystem.translatef((x - x * scale), y - y * scale, 0.0F);
        RenderSystem.scalef(scale, scale, 1.0F);
        RenderUtil.blit(x - flashWidth / 2, y - flashHeight / 2, flashWidth,
            flashHeight);
        RenderSystem.disableBlend();
      }
      RenderSystem.popMatrix();
    }
  }

  private static void renderScopeOverlay(AbstractClientPlayerEntity playerEntity, Scope scope,
      int width,
      int height) {
    scope.getOverlayTexture(playerEntity)
        .ifPresent(overlayTexture -> {
          RenderUtil.bind(overlayTexture);
          double overlayTextureWidth = scope.getOverlayTextureWidth();
          double overlayTextureHeight = scope.getOverlayTextureHeight();
          double scale = RenderUtil.getFitScale(overlayTextureWidth, overlayTextureHeight);
          overlayTextureWidth *= scale;
          overlayTextureHeight *= scale;
          RenderSystem.enableBlend();
          RenderUtil
              .blit(width / 2 - overlayTextureWidth / 2,
                  height / 2 - overlayTextureHeight / 2, overlayTextureWidth,
                  overlayTextureHeight);
          RenderSystem.disableBlend();
        });
  }

  public void renderOverlay(PlayerExtension<AbstractClientPlayerEntity> player, ItemStack heldStack,
      @Nullable Gun gun, MatrixStack matrixStack, int width, int height, float partialTicks) {

    // TODO Fixes Minecraft bug when using post-processing shaders.
    RenderSystem.enableTexture();

    if (this.hitMarker != null) {
      if (this.hitMarker.render(width, height, partialTicks)) {
        this.hitMarker = null;
      }
    }

    this.renderKillFeed(matrixStack, partialTicks);

    final AbstractClientPlayerEntity playerEntity = player.getEntity();

    heldStack.getCapability(Capabilities.SCOPE)
        .filter(scope -> scope.isAiming(playerEntity))
        .ifPresent(scope -> renderScopeOverlay(playerEntity, scope, width, height));

    // Draws Flashbang effect
    EffectInstance flashEffect =
        player.getEntity()
            .getEffect(ModMobEffects.FLASH_BLINDNESS.get());
    if (flashEffect != null) {
      int alpha = (int) (255F
          * (MathHelper.clamp(flashEffect.getDuration() - partialTicks, 0, 20) / 20F));
      int flashColour = 0x00FFFFFF | (alpha & 255) << 24;
      RenderUtil.drawGradientRectangle(0, 0, width, height, flashColour, flashColour);
    }

    player.getProgressMonitor()
        .ifPresent(observer -> renderProgress(matrixStack, this.minecraft.font, width,
            height, observer.getMessage(), observer.getSubMessage().orElse(null),
            observer.getProgress(partialTicks)));

    if (gun != null) {
      this.renderGunFlash(playerEntity, gun, width, height, partialTicks);
    }

    // Needs to render after blood or else it causes Z level issues
    if (gun != null) {
      this.renderAmmo(matrixStack, width, height, gun);
    }

    if (player.isCombatModeEnabled()) {
      this.renderCombatMode(player, matrixStack, width, height);
    }
  }

  private void renderKillFeed(MatrixStack matrixStack, float partialTicks) {
    if (this.killFeedVisibleTimeMs == 0L) {
      this.killFeedVisibleTimeMs = Util.getMillis();
      this.killFeedAnimationTimeMs = 0L;
    }

    final long currentTime = Util.getMillis();
    float durationPct = MathHelper.clamp(
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
            ? MathHelper.clamp((float) (currentTime - this.killFeedAnimationTimeMs)
                / ((KILL_FEED_MESSAGE_LIFE_MS / 4.0F) - partialTicks), 0.0F, 1.0F)
            : 0.0F;

    final int killFeedMessageX = 5;
    for (int i = 0; i < this.killFeedMessages.size(); i++) {
      final KillFeedEntry killFeedMessage = this.killFeedMessages.get(i);
      float killFeedMessageY = 5.0F + ((i - (1.0F * animationPct)) * 12.0F);
      this.renderKillFeedEntry(killFeedMessage, matrixStack, killFeedMessageX, killFeedMessageY,
          i == 0 ? 1.0F - animationPct : 1.0F);
    }
  }

  @SuppressWarnings("deprecation")
  private void renderKillFeedEntry(KillFeedEntry entry, MatrixStack matrixStack, float x, float y,
      float alpha) {
    final String playerEntityName = entry.getKillerEntity().getDisplayName().getString();
    final String deadEntityName = entry.getDeadEntity().getDisplayName().getString();
    final int playerEntityNameWidth = minecraft.font.width(playerEntityName);
    final int deadEntityNameWidth = minecraft.font.width(deadEntityName);

    int spacing = 20;
    alpha *= this.minecraft.player == entry.getKillerEntity() ? 0.7F : 0.5F;

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
    RenderUtil.drawGradientRectangle(x, y,
        x + playerEntityNameWidth + deadEntityNameWidth + spacing, y + 11, colour, colour);

    this.minecraft.font.drawShadow(matrixStack,
        entry.getKillerEntity().getDisplayName().getString(),
        x + 2, y + 2, 0xFFFFFF + ((int) (alpha * 255.0F) << 24));
    this.minecraft.font.drawShadow(matrixStack,
        entry.getDeadEntity().getDisplayName().getString(),
        x + playerEntityNameWidth + spacing - 1, y + 2, 0xFFFFFF + (opacity << 24));

    switch (entry.getType()) {
      case HEADSHOT:
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
        RenderUtil.bind(new ResourceLocation(CraftingDead.ID, "textures/gui/headshot.png"));
        RenderUtil.blit(x + playerEntityNameWidth + 17, y - 1, 12, 12);
        RenderSystem.disableBlend();
        break;
      case WALLBANG:
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
        RenderUtil.bind(new ResourceLocation(CraftingDead.ID, "textures/gui/wallbang.png"));
        RenderUtil.blit(x + playerEntityNameWidth + 35, y - 1, 12, 12);
        RenderSystem.disableBlend();
        break;
      case WALLBANG_HEADSHOT:
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
        RenderUtil.bind(new ResourceLocation(CraftingDead.ID, "textures/gui/wallbang.png"));
        RenderUtil.blit(x + playerEntityNameWidth + 35, y - 1, 12, 12);
        RenderUtil.bind(new ResourceLocation(CraftingDead.ID, "textures/gui/headshot.png"));
        RenderUtil.blit(x + playerEntityNameWidth + 35 + 14, y - 1, 12, 12);
        RenderSystem.disableBlend();
        break;
      default:
        break;
    }

    if (!entry.getWeaponStack().isEmpty()) {
      RenderSystem.pushMatrix();
      {
        RenderSystem.translated(x + playerEntityNameWidth + 4, y - 1, 0);

        if (entry.getWeaponStack().getItem() instanceof GunItem) {
          double scale = 0.75D;
          RenderSystem.scaled(scale, scale, scale);
        }

        // if (this.itemStack.getItem() instanceof ItemKnife) {
        // double scale = 0.6D;
        // GL11.glScaled(scale, scale, scale);
        // GL11.glRotated(180, 0, 1, 0);
        // GL11.glRotated(-20, 0, 0, 1);
        // }

        if (entry.getWeaponStack().getItem() instanceof GrenadeItem) {
          double scale = 0.8D;
          RenderSystem.scaled(scale, scale, scale);
          RenderSystem.translated(4, 1, 0);
        }

        RenderUtil.renderGuiItem(entry.getWeaponStack(), 0, 0, 0xFFFFFF + (opacity << 24));
      }
      RenderSystem.popMatrix();
    }
  }

  private void renderAmmo(MatrixStack matrixStack, int width,
      int height, Gun gun) {

    int x = width - 115;
    int boxHeight = 25;
    if (CraftingDead.getInstance().isTravelersBackpacksLoaded()
        && CapabilityUtils.isWearingBackpack(this.minecraft.player)) {
      x -= 30;
    }

    RenderUtil.drawGradientRectangle(x - 10, height - boxHeight, x + 30, height, 0x00000000,
        0x55000000);
    AbstractGui.fill(matrixStack, x + 30, height - boxHeight, x + 30 + 90, height, 0x55000000);

    AmmoProvider ammoProvider = gun.getAmmoProvider();
    int ammoCount = ammoProvider.getMagazine().map(Magazine::getSize).orElse(0);
    int reserveSize = ammoProvider.getReserveSize();
    boolean empty = ammoCount == 0 && reserveSize == 0;

    String ammoText = empty ? I18n.get("hud.empty_magazine")
        : String.valueOf(ammoCount);
    int ammoTextWidth = this.minecraft.font.width(ammoText);

    float reserveTextScale = 0.6F;
    String reserveText = " / " + reserveSize;
    int reserveTextWidth =
        (int) (this.minecraft.font.width(reserveText) * reserveTextScale);

    this.minecraft.font.drawShadow(matrixStack,
        ammoText,
        x + 55 - ammoTextWidth - (empty ? 0 : reserveTextWidth),
        height - (boxHeight / 2) - this.minecraft.font.lineHeight / 2,
        empty ? TextFormatting.RED.getColor() : 0xFFFFFFFF);

    if (!empty) {
      matrixStack.pushPose();
      matrixStack.translate(x + 55 - reserveTextWidth,
          height - (boxHeight / 2) - (reserveTextScale * 2), 0);
      matrixStack.scale(reserveTextScale, reserveTextScale, reserveTextScale);
      this.minecraft.font.drawShadow(matrixStack,
          reserveText, 0, 0, empty ? TextFormatting.RED.getColor() : 0xFFFFFFFF);
      matrixStack.popPose();
    }

    String fireMode = I18n.get(gun.getFireMode().getTranslationKey());
    this.minecraft.font.drawShadow(matrixStack,
        fireMode, x + 65, height - 16, 0xFFFFFFFF);
  }

  private static void renderProgress(MatrixStack matrixStack, FontRenderer fontRenderer,
      int width, int height,
      ITextComponent message, @Nullable ITextComponent subMessage, float percent) {
    final int barWidth = 100;
    final int barHeight = 10;
    final int barColour = 0xC0FFFFFF;
    final float x = width / 2 - barWidth / 2;
    final float y = height / 2;
    fontRenderer.drawShadow(matrixStack, message.getString(), x,
        y - barHeight - ((fontRenderer.lineHeight / 2) + 0.5F), 0xFFFFFF);
    RenderUtil.drawGradientRectangle(x, y, x + barWidth * percent, y + barHeight, barColour,
        barColour);
    if (subMessage != null) {
      fontRenderer.drawShadow(matrixStack, subMessage.getString(), x,
          y + barHeight + ((fontRenderer.lineHeight / 2) + 0.5F), 0xFFFFFF);
    }
  }

  @SuppressWarnings("deprecation")
  private void renderCombatMode(PlayerExtension<AbstractClientPlayerEntity> player,
      MatrixStack matrixStack,
      int width, int height) {
    final PlayerInventory inventory = player.getEntity().inventory;

    int boxX = width - 115;
    int boxWidth = 110;
    int boxY = height - 170;
    int boxHeight = 30;
    int boxMarginY = 31;

    int currentItemIndex = inventory.selected;

    // Render primary
    ItemStack primaryStack = inventory.getItem(0);
    if (currentItemIndex == 0) {
      RenderUtil.fill(matrixStack, boxX + 1, boxY + 1, boxWidth - 2, boxHeight - 2, 0xCCFFFFFF);
    }
    RenderUtil.fill(matrixStack, boxX, boxY, boxWidth, boxHeight, 0x66000000);
    this.minecraft.font.drawShadow(matrixStack, "1", boxX + 5, boxY + 5,
        0xFFFFFFFF);
    RenderSystem.pushMatrix();
    RenderSystem.translatef(boxX + boxWidth / 2 - 16 / 2,
        boxY + (boxHeight / 2) - 16 / 2, 0);
    RenderSystem.scalef(1.2F, 1.2F, 1.2F);
    RenderUtil.renderGuiItem(primaryStack, 0, 0, -1, ItemCameraTransforms.TransformType.FIXED);
    RenderSystem.popMatrix();

    // Render secondary
    ItemStack secondaryStack = inventory.getItem(1);
    boxY += boxMarginY;
    if (currentItemIndex == 1) {
      RenderUtil.fill(matrixStack, boxX + 1, boxY + 1, boxWidth - 2, boxHeight - 2, 0xCCFFFFFF);
    }
    RenderUtil.fill(matrixStack, boxX, boxY, boxWidth, boxHeight, 0x66000000);
    this.minecraft.font.drawShadow(matrixStack, "2", boxX + 5, boxY + 5,
        0xFFFFFFFF);
    RenderSystem.pushMatrix();
    RenderSystem.translatef(boxX + boxWidth / 2 - 16 / 2,
        boxY + (boxHeight / 2) - 16 / 2, 0);
    RenderSystem.scalef(1.2F, 1.2F, 1.2F);
    RenderUtil.renderGuiItem(secondaryStack, 0, 0, 0xFFFFFFFF,
        ItemCameraTransforms.TransformType.FIXED);
    RenderSystem.popMatrix();

    // Render melee
    ItemStack meleeStack = inventory.getItem(2);
    boxY += boxMarginY;
    if (currentItemIndex == 2) {
      RenderUtil.fill(matrixStack, boxX + 1, boxY + 1, boxWidth - 2, boxHeight - 2, 0xCCFFFFFF);
    }
    RenderUtil.fill(matrixStack, boxX, boxY, boxWidth, boxHeight, 0x66000000);
    this.minecraft.font.drawShadow(matrixStack, "3", boxX + 5, boxY + 5,
        0xFFFFFFFF);
    RenderSystem.pushMatrix();
    RenderSystem.translatef(boxX + boxWidth / 2 - 16 / 2,
        boxY + (boxHeight / 2) - 16 / 2, 0);
    RenderSystem.scalef(1.2F, 1.2F, 1.2F);
    RenderUtil.renderGuiItem(meleeStack, 0, 0, 0xFFFFFFFF,
        ItemCameraTransforms.TransformType.FIXED);
    RenderSystem.popMatrix();


    // Render extras
    boxY += boxMarginY;
    boxHeight = 25;
    boxWidth = 25;
    for (int i = 0; i < 4; i++) {
      ItemStack extraStack = inventory.getItem(3 + i);
      if (currentItemIndex == 3 + i) {
        RenderUtil.fill(matrixStack, boxX + 1, boxY + 1, boxWidth - 2, boxHeight - 2, 0xCCFFFFFF);
      }
      RenderUtil.fill(matrixStack, boxX, boxY, boxWidth, boxHeight, 0x66000000);
      this.minecraft.font.drawShadow(matrixStack, String.valueOf(4 + i), boxX + 1,
          boxY + 1, 0xFFFFFFFF);

      RenderSystem.pushMatrix();

      RenderUtil.renderGuiItem(extraStack, boxX + boxWidth / 2 - 16 / 2,
          boxY + (boxHeight / 2) - 6, 0xFFFFFFFF);
      RenderSystem.popMatrix();

      boxX += 28;
    }


    final int healthBoxHeight = 25;
    // Render Health
    final float health = player.getEntity().getHealth();
    final float armour = player.getEntity().getArmorValue();



    int healthWidth = 100;
    if (armour > 0) {
      healthWidth *= 2;
    }

    AbstractGui.fill(matrixStack, 0, height - healthBoxHeight, healthWidth, height, 0x55000000);

    RenderUtil.drawGradientRectangle(healthWidth, height - healthBoxHeight, healthWidth + 40,
        height, 0x55000000, 0x00000000);

    RenderUtil.bind(HEALTH);
    RenderSystem.enableBlend();
    RenderUtil.blit(5, height - healthBoxHeight / 2 - 8, 16, 16);
    RenderSystem.disableBlend();

    AbstractGui.drawCenteredString(matrixStack, this.minecraft.font,
        String.valueOf((int) health), 31,
        height - healthBoxHeight / 2 - this.minecraft.font.lineHeight / 2, 0xFFFFFFFF);
    RenderUtil.fill(matrixStack, 42, height - healthBoxHeight / 2 - 5, 65, 10, 0x66000000);
    RenderUtil.fill(matrixStack, 42, height - healthBoxHeight / 2 - 5,
        (int) (65 * (health / player.getEntity().getMaxHealth())), 10, 0xCCFFFFFF);

    if (armour > 0) {
      int armourX = healthWidth / 2 + 7;
      RenderUtil.bind(SHIELD);
      RenderSystem.enableBlend();
      RenderUtil.blit(armourX + 5, height - healthBoxHeight / 2 - 8, 16, 16);
      RenderSystem.disableBlend();

      AbstractGui.drawCenteredString(matrixStack, this.minecraft.font,
          String.valueOf((int) armour), armourX + 31,
          height - healthBoxHeight / 2 - this.minecraft.font.lineHeight / 2, 0xFFFFFFFF);
      RenderUtil.fill(matrixStack, armourX + 42, height - healthBoxHeight / 2 - 5, 65, 10,
          0x66000000);
      RenderUtil.fill(matrixStack, armourX + 42, height - healthBoxHeight / 2 - 5,
          (int) (65 * (armour / 20)), 10, 0xCCFFFFFF);
    }
  }

  @SuppressWarnings("deprecation")
  public void renderCrosshairs(float accuracy, float partialTicks, int width, int height) {
    final double imageWidth = 16.0D;
    final double imageHeight = 16.0D;

    final double x = (width / 2.0D) - (imageWidth / 2.0D) - 0.5F;
    final double y = (height / 2.0D) - (imageHeight / 2.0D);

    final float newSpread = (1.15F - accuracy) * 60.0F;
    final float lerpSpread = MathHelper.lerp(0.5F, this.lastSpread, newSpread);
    final Crosshair crosshair =
        this.client.getCrosshairManager().getCrosshair(this.crosshairLocation);

    RenderSystem.pushMatrix();
    {
      RenderSystem.enableBlend();

      RenderUtil.bind(crosshair.getMiddle());
      RenderUtil.blit(x, y, imageWidth, imageHeight);

      RenderUtil.bind(crosshair.getTop());
      RenderUtil.blit(x, y - lerpSpread, imageWidth, imageHeight);

      RenderUtil.bind(crosshair.getBottom());
      RenderUtil.blit(x, y + lerpSpread, imageWidth, imageHeight);

      RenderUtil.bind(crosshair.getLeft());
      RenderUtil.blit(x - lerpSpread, y, imageWidth, imageHeight);

      RenderUtil.bind(crosshair.getRight());
      RenderUtil.blit(x + lerpSpread, y, imageWidth, imageHeight);
      RenderSystem.disableBlend();
    }
    RenderSystem.popMatrix();

    this.lastSpread = lerpSpread;
  }
}
