package com.craftingdead.mod.client.gui;

import com.craftingdead.mod.CommonConfig;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.client.Graphics;
import com.craftingdead.mod.client.crosshair.Crosshair;
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
          renderWaterLevel(width, height, (float) player.getWater() / (float) player.getMaxWater());
        }

        renderPlayerStats(this.minecraft.fontRenderer, width, height, player.getDaysSurvived(),
            player.getZombiesKilled(), player.getPlayersKilled());
      }
    });
  }

  private static void renderBlood(int width, int height, float healthPercentage) {
    if (healthPercentage < 1.0F) {
      ResourceLocation res = healthPercentage <= 0.25F ? BLOOD_2 : BLOOD;

      GlStateManager.enableBlend();

      Graphics.bind(res);
      GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1 - healthPercentage);
      Graphics.drawTexturedRectangle(0, 0, width, height);
      GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

      GlStateManager.disableBlend();
    }
  }

  private static void renderPlayerStats(FontRenderer fontRenderer, int width, int height,
      int daysSurvived, int zombiesKilled, int playersKilled) {
    int y = height / 2;
    int x = 4;

    GlStateManager.enableBlend();

    Graphics.bind(DAYS_SURVIVED);
    Graphics.drawTexturedRectangle(x, y - 20, 16, 16);
    fontRenderer.drawStringWithShadow(String.valueOf(daysSurvived), x + 20, y - 16, 0xFFFFFF);

    Graphics.bind(ZOMBIES_KILLED);
    Graphics.drawTexturedRectangle(x, y, 16, 16);
    fontRenderer.drawStringWithShadow(String.valueOf(zombiesKilled), x + 20, y + 4, 0xFFFFFF);

    Graphics.bind(PLAYERS_KILLED);
    Graphics.drawTexturedRectangle(x, y + 20, 16, 16);
    fontRenderer.drawStringWithShadow(String.valueOf(playersKilled), x + 20, y + 24, 0xFFFFFF);

    GlStateManager.disableBlend();
  }

  private static void renderWaterLevel(int width, int height, float waterPercentage) {
    final int y = height - 49;
    final int x = width / 2 + 91;
    GlStateManager.enableBlend();
    Graphics.bind(Graphics.ICONS);

    for (int i = 0; i < 10; i++) {
      // Draw droplet outline
      Graphics.drawTexturedRectangle(x - i * 8 - 9, y, 9, 9, 0, 32);

      float scaledWater = 10.0F * waterPercentage;
      if (i + 1 <= scaledWater) {
        // Draw full droplet
        Graphics.drawTexturedRectangle(x - i * 8 - 9, y, 9, 9, 9, 32);
      } else if (scaledWater >= i + 0.5F) {
        // Draw half droplet
        Graphics.drawTexturedRectangle(x - i * 8 - 9, y, 9, 9, 18, 32);
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

      Graphics.bind(crosshair.getMiddle());
      Graphics.drawTexturedRectangle(x, y, imageWidth, imageHeight);

      Graphics.bind(crosshair.getTop());
      Graphics.drawTexturedRectangle(x, y - lerpSpread, imageWidth, imageHeight);

      Graphics.bind(crosshair.getBottom());
      Graphics.drawTexturedRectangle(x, y + lerpSpread, imageWidth, imageHeight);

      Graphics.bind(crosshair.getLeft());
      Graphics.drawTexturedRectangle(x - lerpSpread, y, imageWidth, imageHeight);

      Graphics.bind(crosshair.getRight());
      Graphics.drawTexturedRectangle(x + lerpSpread, y, imageWidth, imageHeight);
      GlStateManager.disableBlend();
    }
    GlStateManager.popMatrix();

    this.lastSpread = lerpSpread;
  }
}
