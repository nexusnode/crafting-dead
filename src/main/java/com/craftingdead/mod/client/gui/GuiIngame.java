package com.craftingdead.mod.client.gui;

import com.craftingdead.mod.CommonConfig;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.client.Graphics;
import com.craftingdead.mod.client.crosshair.Crosshair;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiIngame {

  private static final ResourceLocation DAYS_SURVIVED =
      new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/hud/days_survived.png");
  private static final ResourceLocation ZOMBIES_KILLED =
      new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/hud/zombies_killed.png");
  private static final ResourceLocation PLAYERS_KILLED =
      new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/hud/players_killed.png");

  private static final ResourceLocation BLOOD =
      new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/blood.png");
  private static final ResourceLocation BLOOD_2 =
      new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/blood_2.png");

  private final Minecraft minecraft;

  private final ClientDist client;

  private ResourceLocation crosshairLocation;

  private float lastSpread;

  public GuiIngame(Minecraft minecraft, ClientDist client, ResourceLocation crosshairLocation) {
    this.minecraft = minecraft;
    this.client = client;
    this.crosshairLocation = crosshairLocation;
  }

  public void renderGameOverlay(float partialTicks) {
    MainWindow window = this.minecraft.mainWindow;
    final int width = window.getScaledWidth();
    final int height = window.getScaledHeight();
    // final int mouseX = Mouse.getX() * width / this.client.getMinecraft().displayWidth;
    // final int mouseY = height - Mouse.getY() * height / this.client.getMinecraft().displayHeight
    // - 1;

    this.client.getPlayer().ifPresent((player) -> {
      ClientPlayerEntity entity = player.getEntity();
      float healthPercentage = entity.getHealth() / entity.getMaxHealth();
      if (!entity.isCreative() && healthPercentage < 1.0F
          && CommonConfig.clientConfig.displayBlood.get()) {
        ResourceLocation res = healthPercentage <= 0.25F ? BLOOD_2 : BLOOD;

        GlStateManager.enableBlend();

        Graphics.bind(res);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1 - healthPercentage);
        Graphics.drawTexturedRectangle(0, 0, width, height);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.disableBlend();
      }

      // Only draw in survival
      if (this.minecraft.playerController.shouldDrawHUD()) {
        this.renderPlayerStats(this.minecraft.fontRenderer, width, height, player.getDaysSurvived(),
            player.getZombiesKilled(), player.getPlayersKilled());
      }
    });
  }

  private void renderPlayerStats(FontRenderer fontRenderer, int width, int height, int daysSurvived,
      int zombiesKilled, int playersKilled) {
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

  public void renderCrosshairs(float accuracy, float partialTicks) {
    final double imageWidth = 16.0D;
    final double imageHeight = 16.0D;

    final double x = (this.minecraft.mainWindow.getScaledWidth() / 2.0D) - (imageWidth / 2.0D);
    final double y = (this.minecraft.mainWindow.getScaledHeight() / 2.0D) - (imageHeight / 2.0D);

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
