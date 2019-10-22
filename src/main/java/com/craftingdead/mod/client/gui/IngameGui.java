package com.craftingdead.mod.client.gui;

import com.craftingdead.mod.CommonConfig;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.client.crosshair.Crosshair;
import com.craftingdead.mod.client.util.RenderUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class IngameGui {

  private static final ResourceLocation DAYS_SURVIVED =
      new ResourceLocation(CraftingDead.ID, "textures/gui/hud/days_survived.png");
  private static final ResourceLocation ZOMBIES_KILLED =
      new ResourceLocation(CraftingDead.ID, "textures/gui/hud/zombies_killed.png");
  private static final ResourceLocation PLAYERS_KILLED =
      new ResourceLocation(CraftingDead.ID, "textures/gui/hud/players_killed.png");

  private static final ResourceLocation BLOOD =
      new ResourceLocation(CraftingDead.ID, "textures/gui/blood.png");
  private static final ResourceLocation BLOOD_2 =
      new ResourceLocation(CraftingDead.ID, "textures/gui/blood_2.png");

  private final Minecraft minecraft;

  private final ClientDist client;

  private ResourceLocation crosshairLocation;

  private float lastSpread;

  public IngameGui(Minecraft minecraft, ClientDist client, ResourceLocation crosshairLocation) {
    this.minecraft = minecraft;
    this.client = client;
    this.crosshairLocation = crosshairLocation;
  }

  public void renderGameOverlay(float partialTicks, int width, int height) {
    // final int mouseX = Mouse.getX() * width / this.client.getMinecraft().displayWidth;
    // final int mouseY = height - Mouse.getY() * height / this.client.getMinecraft().displayHeight
    // - 1;

    this.client.getPlayer().ifPresent((player) -> {
      ClientPlayerEntity entity = player.getEntity();

      // Only draw in survival
      if (this.minecraft.playerController.shouldDrawHUD()) {
        if (CommonConfig.clientConfig.displayBlood.get()) {
          renderBlood(width, height, entity.getHealth() / entity.getMaxHealth());
        }

        // Only render when air level is not being rendered
        if (!entity.areEyesInFluid(FluidTags.WATER) && entity.getAir() == entity.getMaxAir()) {
          renderModGui(width, height, (float) player.getWater() / (float) player.getMaxWater(),
              RenderUtil.ICONS);
        }

        renderModGui(width - 202, height,
            (float) player.getStamina() / (float) player.getMaxStamina(), RenderUtil.SPRINT);

        renderPlayerStats(this.minecraft.fontRenderer, width, height, player.getDaysSurvived(),
            player.getZombiesKilled(), player.getPlayersKilled());
      }
    });
  }

  private static void renderBlood(int width, int height, float healthPercentage) {
    if (healthPercentage < 1.0F) {
      ResourceLocation res = healthPercentage <= 0.25F ? BLOOD_2 : BLOOD;

      GlStateManager.enableBlend();

      RenderUtil.bind(res);
      GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1 - healthPercentage);
      RenderUtil.drawTexturedRectangle(0, 0, width, height);
      GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

      GlStateManager.disableBlend();
    }
  }

  private static void renderPlayerStats(FontRenderer fontRenderer, int width, int height,
      int daysSurvived, int zombiesKilled, int playersKilled) {
    int y = height / 2;
    int x = 4;

    GlStateManager.enableBlend();

    RenderUtil.bind(DAYS_SURVIVED);
    RenderUtil.drawTexturedRectangle(x, y - 20, 16, 16);
    fontRenderer.drawStringWithShadow(String.valueOf(daysSurvived), x + 20, y - 16, 0xFFFFFF);

    RenderUtil.bind(ZOMBIES_KILLED);
    RenderUtil.drawTexturedRectangle(x, y, 16, 16);
    fontRenderer.drawStringWithShadow(String.valueOf(zombiesKilled), x + 20, y + 4, 0xFFFFFF);

    RenderUtil.bind(PLAYERS_KILLED);
    RenderUtil.drawTexturedRectangle(x, y + 20, 16, 16);
    fontRenderer.drawStringWithShadow(String.valueOf(playersKilled), x + 20, y + 24, 0xFFFFFF);

    GlStateManager.disableBlend();
  }

  private static void renderModGui(int width, int height, float waterPercentage,
      ResourceLocation resourceLocation) {
    final int y = height - 49;
    final int x = width / 2 + 91;
    GlStateManager.enableBlend();
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
    GlStateManager.disableBlend();
  }

  public void renderCrosshairs(float accuracy, float partialTicks, int width, int height) {
    final double imageWidth = 16.0D;
    final double imageHeight = 16.0D;

    final double x = (width / 2.0D) - (imageWidth / 2.0D);
    final double y = (height / 2.0D) - (imageHeight / 2.0D);

    final float newSpread = (1.0F - accuracy) * 60.0F;
    final float lerpSpread = MathHelper.lerp(0.5F, this.lastSpread, newSpread);
    final Crosshair crosshair =
        this.client.getCrosshairManager().getCrosshair(this.crosshairLocation);

    GlStateManager.pushMatrix();
    {
      GlStateManager.enableBlend();

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
      GlStateManager.disableBlend();
    }
    GlStateManager.popMatrix();

    this.lastSpread = lerpSpread;
  }
}
