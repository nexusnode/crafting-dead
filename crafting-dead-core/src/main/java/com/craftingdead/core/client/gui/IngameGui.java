package com.craftingdead.core.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.living.Player;
import com.craftingdead.core.capability.scope.IScope;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.client.crosshair.Crosshair;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.item.MagazineItem;
import com.craftingdead.core.potion.ModEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tiviacz.travelersbackpack.capability.CapabilityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;

public class IngameGui {

  private static final Random random = new Random();

  private static final ResourceLocation BLOOD =
      new ResourceLocation(CraftingDead.ID, "textures/gui/blood.png");
  private static final ResourceLocation BLOOD_2 =
      new ResourceLocation(CraftingDead.ID, "textures/gui/blood_2.png");

  private static final int HIT_MARKER_FADE_TIME_MS = 3000;

  private static final int KILL_FEED_MESSAGE_LIFE_MS = 5000;

  private final Minecraft minecraft;

  private final ClientDist client;

  private final List<KillFeedEntry> killFeedMessages = new ArrayList<>();

  private ResourceLocation crosshairLocation;

  private float lastSpread;

  private int lastShotCount;

  private float lastFlashScale = 0;

  private long hitMarkerFadeStartTimeMs;
  private Vec3d hitMarkerPos;
  private int hitMarkerColour;

  private long killFeedVisibleTimeMs;
  private long killFeedAnimationTimeMs;

  public IngameGui(Minecraft minecraft, ClientDist client, ResourceLocation crosshairLocation) {
    this.minecraft = minecraft;
    this.client = client;
    this.crosshairLocation = crosshairLocation;
  }

  public void addKillFeedMessage(KillFeedEntry killFeedMessage) {
    if (this.killFeedMessages.isEmpty()) {
      this.killFeedVisibleTimeMs = 0L;
    }
    this.killFeedMessages.add(killFeedMessage);
  }

  public void displayHitMarker(Vec3d pos, int colour) {
    this.hitMarkerFadeStartTimeMs = 0L;
    this.hitMarkerPos = pos;
    this.hitMarkerColour = colour;
  }

  private void renderHitMarker(int width, int height, float partialTicks) {
    if (this.hitMarkerPos != null) {
      if (this.hitMarkerFadeStartTimeMs == 0L) {
        this.hitMarkerFadeStartTimeMs = Util.milliTime();
      }
      float fadePct = MathHelper.clamp(
          (float) (Util.milliTime() - this.hitMarkerFadeStartTimeMs) / HIT_MARKER_FADE_TIME_MS,
          0.0F, 1.0F);

      final Vec2f pos = RenderUtil.projectToPlayerView(this.hitMarkerPos.getX(),
          this.hitMarkerPos.getY(), this.hitMarkerPos.getZ(), partialTicks);

      if (fadePct == 1.0F) {
        this.hitMarkerPos = null;
      }

      if (pos == null) {
        return;
      }

      float alpha = (float) (this.hitMarkerColour >> 24 & 255) / 255.0F;
      float red = (float) (this.hitMarkerColour >> 16 & 255) / 255.0F;
      float green = (float) (this.hitMarkerColour >> 8 & 255) / 255.0F;
      float blue = (float) (this.hitMarkerColour & 255) / 255.0F;

      RenderSystem.enableBlend();
      RenderSystem.color4f(red, green, blue, alpha * (1.0F - fadePct));
      RenderSystem.pushMatrix();
      {
        RenderSystem.translatef((width / 2) + pos.x - 15, (height / 2) - pos.y - 15, 0);
        RenderSystem.lineWidth(5.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(10, 20, 0.0D).endVertex();
        bufferbuilder.pos(20, 10, 0.0D).endVertex();
        tessellator.draw();
        bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(10, 10, 0.0D).endVertex();
        bufferbuilder.pos(20, 20, 0.0D).endVertex();
        tessellator.draw();
      }
      RenderSystem.popMatrix();
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
    }
  }

  private void renderGunFlash(ClientPlayerEntity playerEntity, IGun gun, int width, int height,
      float partialTicks) {
    final boolean aiming =
        gun instanceof IScope
            && ((IScope) gun).isAiming(playerEntity, playerEntity.getHeldItemMainhand());
    final boolean flash =
        gun.getShotCount() != this.lastShotCount && !aiming && gun.getShotCount() > 0;
    this.lastShotCount = gun.getShotCount();

    if (flash) {
      RenderSystem.pushMatrix();
      {
        final float flashIntensity = (random.nextInt(3) + 5) / 10.0F;
        final float scale = this.lastFlashScale =
            MathHelper.lerp(partialTicks, this.lastFlashScale, flashIntensity);
        this.minecraft.getTextureManager()
            .bindTexture(
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
        RenderUtil.drawTexturedRectangle(x - flashWidth / 2, y - flashHeight / 2, flashWidth,
            flashHeight);
        RenderSystem.disableBlend();
      }
      RenderSystem.popMatrix();
    }
  }

  private static void renderScopeOverlay(ClientPlayerEntity playerEntity, IScope scope, int width,
      int height) {
    scope.getOverlayTexture(playerEntity, playerEntity.getHeldItemMainhand())
        .ifPresent(overlayTexture -> {
          RenderUtil.bind(overlayTexture);
          double overlayTextureWidth = scope.getOverlayTextureWidth();
          double overlayTextureHeight = scope.getOverlayTextureHeight();
          double scale = RenderUtil.getFitScale(overlayTextureWidth, overlayTextureHeight);
          overlayTextureWidth *= scale;
          overlayTextureHeight *= scale;
          RenderSystem.enableBlend();
          RenderUtil
              .drawTexturedRectangle(width / 2 - overlayTextureWidth / 2,
                  height / 2 - overlayTextureHeight / 2, overlayTextureWidth,
                  overlayTextureHeight);
          RenderSystem.disableBlend();
        });
  }

  public void renderGameOverlay(Player<ClientPlayerEntity> player, int width, int height, float partialTicks) {
    this.renderHitMarker(width, height, partialTicks);
    this.renderKillFeed(partialTicks);

    final ClientPlayerEntity playerEntity = player.getEntity();
    final ItemStack heldStack = playerEntity.getHeldItemMainhand();
    final IGun gun = heldStack.getCapability(ModCapabilities.GUN).orElse(null);

    if (gun != null) {
      this.renderGunFlash(playerEntity, gun, width, height, partialTicks);
    }

    heldStack.getCapability(ModCapabilities.SCOPE)
        .filter(scope -> scope.isAiming(playerEntity, heldStack))
        .ifPresent(scope -> renderScopeOverlay(playerEntity, scope, width, height));

    // Draws Flashbang effect
    EffectInstance flashEffect =
        player.getEntity()
            .getActivePotionEffect(ModEffects.FLASH_BLINDNESS.get());
    if (flashEffect != null) {
      int alpha = (int) (255F
          * (MathHelper.clamp(flashEffect.getDuration() - partialTicks, 0, 20) / 20F));
      int flashColour = 0x00FFFFFF | (alpha & 255) << 24;
      RenderUtil.drawGradientRectangle(0, 0, width, height, flashColour, flashColour);
    }

    player.getActionProgress()
        .ifPresent(observer -> renderActionProgress(this.minecraft.fontRenderer, width, height,
            observer.getMessage(), observer.getSubMessage(),
            observer.getProgress(partialTicks)));

    // Only draw in survival
    if (this.minecraft.playerController.shouldDrawHUD()) {
      float healthPercentage = playerEntity.getHealth() / playerEntity.getMaxHealth();
      if (ClientDist.clientConfig.displayBlood.get() && healthPercentage < 1.0F
          && playerEntity.isPotionActive(ModEffects.BLEEDING.get())) {
        renderBlood(width, height, healthPercentage);
      }

      // Only render when air level is not being rendered
      if (!playerEntity.areEyesInFluid(FluidTags.WATER)
          && playerEntity.getAir() == playerEntity.getMaxAir()) {
        renderWater(width, height, (float) player.getWater() / (float) player.getMaxWater(),
            RenderUtil.ICONS);
      }
    }

    // Needs to render after blood or else it causes Z level issues
    if (gun != null) {
      renderAmmo(this.minecraft.getItemRenderer(), this.minecraft.fontRenderer,
          width, height, gun.getMagazineSize(), gun.getMagazineStack());
    }
  }

  private void renderKillFeed(float partialTicks) {
    if (this.killFeedVisibleTimeMs == 0L) {
      this.killFeedVisibleTimeMs = Util.milliTime();
      this.killFeedAnimationTimeMs = 0L;
    }

    final long currentTime = Util.milliTime();
    float durationPct = MathHelper.clamp(
        (float) (currentTime - this.killFeedVisibleTimeMs) / KILL_FEED_MESSAGE_LIFE_MS, 0.0F, 1.0F);
    if (durationPct == 1.0F && !this.killFeedMessages.isEmpty()) {
      this.killFeedMessages.remove(0);
      if (!this.killFeedMessages.isEmpty()) {
        this.killFeedVisibleTimeMs = Util.milliTime();
        this.killFeedAnimationTimeMs = 0L;
      }
    } else if (durationPct >= 0.75F && this.killFeedAnimationTimeMs == 0L) {
      this.killFeedAnimationTimeMs = Util.milliTime();
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
      killFeedMessage.render(killFeedMessageX, killFeedMessageY,
          i == 0 ? 1.0F - animationPct : 1.0F);
    }
  }

  private static void renderAmmo(ItemRenderer itemRenderer, FontRenderer fontRenderer, int width,
      int height, int ammo, ItemStack magazineStack) {
    if (magazineStack.getItem() instanceof MagazineItem) {
      MagazineItem magazine = (MagazineItem) magazineStack.getItem();
      String text = ammo + "/" + magazine.getSize();
      int x = width - 15 - fontRenderer.getStringWidth(text);
      if (CraftingDead.getInstance().isTravelersBackpacksLoaded()
          && CapabilityUtils.isWearingBackpack(Minecraft.getInstance().player)) {
        x -= 25;
      }
      int y = height - 10 - fontRenderer.FONT_HEIGHT;
      fontRenderer.drawStringWithShadow(text, x, y, 0xFFFFFF);
      itemRenderer.renderItemAndEffectIntoGUI(magazineStack, x - 16, y - 5);
    }
  }

  private static void renderActionProgress(FontRenderer fontRenderer, int width, int height,
      ITextComponent message, @Nullable ITextComponent subMessage, float percent) {
    final int barWidth = 100;
    final int barHeight = 10;
    final int barColour = 0xC0FFFFFF;
    final float x = width / 2 - barWidth / 2;
    final float y = height / 2;
    fontRenderer.drawStringWithShadow(message.getFormattedText(), x,
        y - barHeight - ((fontRenderer.FONT_HEIGHT / 2) + 0.5F), 0xFFFFFF);
    RenderUtil
        .drawGradientRectangle(x, y, x + barWidth * percent, y + barHeight, barColour, barColour);
    if (subMessage != null) {
      fontRenderer.drawStringWithShadow(subMessage.getFormattedText(), x,
          y + barHeight + ((fontRenderer.FONT_HEIGHT / 2) + 0.5F),
          0xFFFFFF);
    }
  }

  private static void renderBlood(int width, int height, float healthPercentage) {
    ResourceLocation res = healthPercentage <= 0.25F ? BLOOD_2 : BLOOD;

    RenderSystem.enableBlend();
    RenderSystem.disableAlphaTest();

    RenderUtil.bind(res);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1 - healthPercentage);
    RenderUtil.drawTexturedRectangle(0, 0, width, height);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.enableAlphaTest();
    RenderSystem.disableBlend();
  }

  private static void renderWater(int width, int height, float waterPercentage,
      ResourceLocation resourceLocation) {
    final int y = height - 49;
    final int x = width / 2 + 91;
    RenderSystem.enableBlend();
    RenderUtil.bind(resourceLocation);

    for (int i = 0; i < 10; i++) {
      // Draw droplet outline
      RenderUtil.drawTexturedRectangle(x - i * 8 - 9, y, 9, 9, 0, 32);

      float scaledWater = 10.0F * waterPercentage;
      if (i + 1 <= scaledWater) {
        // Draw full droplet
        RenderUtil.drawTexturedRectangle(x - i * 8 - 9, y, 9, 9, 9, 32);
      } else if (scaledWater >= i + 0.5F) {
        // Draw half droplet
        RenderUtil.drawTexturedRectangle(x - i * 8 - 9, y, 9, 9, 18, 32);
      }
    }
    RenderSystem.disableBlend();
  }

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
      RenderUtil.drawTexturedRectangle(x, y, imageWidth, imageHeight);

      RenderUtil.bind(crosshair.getTop());
      RenderUtil.drawTexturedRectangle(x, y - lerpSpread, imageWidth, imageHeight);

      RenderUtil.bind(crosshair.getBottom());
      RenderUtil.drawTexturedRectangle(x, y + lerpSpread, imageWidth, imageHeight);

      RenderUtil.bind(crosshair.getLeft());
      RenderUtil.drawTexturedRectangle(x - lerpSpread, y, imageWidth, imageHeight);

      RenderUtil.bind(crosshair.getRight());
      RenderUtil.drawTexturedRectangle(x + lerpSpread, y, imageWidth, imageHeight);
      RenderSystem.disableBlend();
    }
    RenderSystem.popMatrix();

    this.lastSpread = lerpSpread;
  }
}
