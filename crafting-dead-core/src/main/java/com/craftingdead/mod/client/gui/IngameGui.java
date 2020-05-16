package com.craftingdead.mod.client.gui;

import com.craftingdead.mod.CommonConfig;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.client.crosshair.Crosshair;
import com.craftingdead.mod.client.util.RenderUtil;
import com.craftingdead.mod.item.MagazineItem;
import com.craftingdead.mod.potion.ModEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

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

    this.client.getPlayer().ifPresent(player -> {
      ClientPlayerEntity playerEntity = player.getEntity();

      ItemStack heldStack = playerEntity.getHeldItemMainhand();
      heldStack.getCapability(ModCapabilities.GUN).ifPresent(gunController -> {
        renderAmmo(this.minecraft.getItemRenderer(), this.minecraft.fontRenderer, width, height,
            gunController.getMagazineSize(), gunController.getMagazineStack());
      });

      playerEntity.getHeldItemMainhand().getCapability(ModCapabilities.ACTION).ifPresent(action -> {
        if (action.isActive(playerEntity)) {
          renderActionProgress(this.minecraft.fontRenderer, width, height,
              action.getText(playerEntity), action.getProgress(playerEntity));
        }
      });

      // Draws Flashbang effect
      EffectInstance flashEffect =
          player.getEntity().getActivePotionEffect(ModEffects.FLASH_BLINDNESS.get());
      if (flashEffect != null) {
        int alpha = (int) (255F
            * (MathHelper.clamp(flashEffect.getDuration() - partialTicks, 0, 20) / 20F));
        int flashColour = 0x00FFFFFF | (alpha & 255) << 24;
        RenderUtil.drawGradientRectangle(0, 0, width, height, flashColour, flashColour);
      }

      // Only draw in survival
      if (this.minecraft.playerController.shouldDrawHUD()) {
        if (CommonConfig.clientConfig.displayBlood.get()) {
          renderBlood(width, height, playerEntity.getHealth() / playerEntity.getMaxHealth());
        }

        // Only render when air level is not being rendered
        if (!playerEntity.areEyesInFluid(FluidTags.WATER)
            && playerEntity.getAir() == playerEntity.getMaxAir()) {
          renderWater(width, height, (float) player.getWater() / (float) player.getMaxWater(),
              RenderUtil.ICONS);
        }

        renderPlayerStats(this.minecraft.fontRenderer, width, height, player.getDaysSurvived(),
            player.getZombiesKilled(), player.getPlayersKilled());
      }
    });
  }

  private static void renderAmmo(ItemRenderer itemRenderer, FontRenderer fontRenderer, int width,
      int height, int ammo, ItemStack magazineStack) {
    if (magazineStack.getItem() instanceof MagazineItem) {
      MagazineItem magazine = (MagazineItem) magazineStack.getItem();
      String text = ammo + "/" + magazine.getSize();
      int x = width - 15 - fontRenderer.getStringWidth(text);
      int y = height - 10 - fontRenderer.FONT_HEIGHT;
      fontRenderer.drawStringWithShadow(text, x, y, 0xFFFFFF);
      RenderSystem.pushMatrix();
      {
        itemRenderer.renderItemAndEffectIntoGUI(magazineStack, x - 15, y - 5);
      }
      RenderSystem.popMatrix();
    }
  }

  private static void renderActionProgress(FontRenderer fontRenderer, int width, int height,
      ITextComponent text, float percent) {
    final int barWidth = 100;
    final int barHeight = 10;
    final int barColour = 0xC0FFFFFF;
    final float x = width / 2 - barWidth / 2;
    final float y = height / 2;
    fontRenderer.drawStringWithShadow(text.getFormattedText(), x, y - barHeight - 5, 0xFFFFFF);
    RenderUtil
        .drawGradientRectangle(x, y, x + barWidth * percent, y + barHeight, barColour, barColour);
  }

  private static void renderBlood(int width, int height, float healthPercentage) {
    if (healthPercentage < 1.0F) {
      ResourceLocation res = healthPercentage <= 0.25F ? BLOOD_2 : BLOOD;

      RenderSystem.enableBlend();

      RenderUtil.bind(res);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1 - healthPercentage);
      RenderUtil.drawTexturedRectangle(0, 0, width, height);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

      RenderSystem.disableBlend();
    }
  }

  private static void renderPlayerStats(FontRenderer fontRenderer, int width, int height,
      int daysSurvived, int zombiesKilled, int playersKilled) {
    int y = height / 2;
    int x = 4;

    RenderSystem.enableBlend();

    RenderUtil.bind(DAYS_SURVIVED);
    RenderUtil.drawTexturedRectangle(x, y - 20, 16, 16);
    fontRenderer.drawStringWithShadow(String.valueOf(daysSurvived), x + 20, y - 16, 0xFFFFFF);

    RenderUtil.bind(ZOMBIES_KILLED);
    RenderUtil.drawTexturedRectangle(x, y, 16, 16);
    fontRenderer.drawStringWithShadow(String.valueOf(zombiesKilled), x + 20, y + 4, 0xFFFFFF);

    RenderUtil.bind(PLAYERS_KILLED);
    RenderUtil.drawTexturedRectangle(x, y + 20, 16, 16);
    fontRenderer.drawStringWithShadow(String.valueOf(playersKilled), x + 20, y + 24, 0xFFFFFF);

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

    final float newSpread = (1.0F - accuracy) * 60.0F;
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
