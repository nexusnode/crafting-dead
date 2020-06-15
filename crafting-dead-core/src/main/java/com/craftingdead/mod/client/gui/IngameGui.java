package com.craftingdead.mod.client.gui;

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
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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

  private static final ResourceLocation ADRENALINE_SHADER =
      new ResourceLocation(CraftingDead.ID, "shaders/post/adrenaline.json");

  private final Minecraft minecraft;

  private final ClientDist client;

  private ResourceLocation crosshairLocation;

  private float lastSpread;

  private long adrenalineShaderStartTime = 0L;

  public IngameGui(Minecraft minecraft, ClientDist client, ResourceLocation crosshairLocation) {
    this.minecraft = minecraft;
    this.client = client;
    this.crosshairLocation = crosshairLocation;
    MinecraftForge.EVENT_BUS.register(this);

  }

  public void renderGameOverlay(float partialTicks, int width, int height) {
    // final int mouseX = Mouse.getX() * width / this.client.getMinecraft().displayWidth;
    // final int mouseY = height - Mouse.getY() * height / this.client.getMinecraft().displayHeight
    // - 1;
    this.client.getPlayer().ifPresent(player -> {
      ClientPlayerEntity playerEntity = player.getEntity();

      // this.updateAdrenalineShader();

      ItemStack heldStack = playerEntity.getHeldItemMainhand();
      heldStack
          .getCapability(ModCapabilities.SCOPE)
          .filter(scope -> scope.isAiming(playerEntity, heldStack))
          .ifPresent(scope -> {
            scope.getOverlayTexture(playerEntity, heldStack).ifPresent(overlayTexture -> {
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


      heldStack
          .getCapability(ModCapabilities.ACTION)
          .filter(action -> action.isActive(playerEntity))
          .ifPresent(action -> renderActionProgress(this.minecraft.fontRenderer, width, height,
              action.getText(playerEntity), action.getProgress(playerEntity, partialTicks)));

      // Only draw in survival
      if (this.minecraft.playerController.shouldDrawHUD()) {
        if (ClientDist.clientConfig.displayBlood.get()) {
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

      // Needs to render after blood or else it causes Z level issues
      heldStack
          .getCapability(ModCapabilities.GUN)
          .ifPresent(
              gun -> renderAmmo(this.minecraft.getItemRenderer(), this.minecraft.fontRenderer,
                  width, height, gun.getMagazineSize(), gun.getMagazineStack()));
    });
  }


  @SubscribeEvent
  public void updateAdrenalineShader(TickEvent.RenderTickEvent event) {
    if (event.phase != TickEvent.Phase.END || this.minecraft.player == null) {
      return;
    }
    final GameRenderer gameRenderer = this.minecraft.gameRenderer;
    final boolean shaderLoaded = gameRenderer.getShaderGroup() != null
        && gameRenderer.getShaderGroup().getShaderGroupName().equals(ADRENALINE_SHADER.toString());
    if (this.minecraft.player.isPotionActive(ModEffects.ADRENALINE.get())) {
      final long currentTime = Util.milliTime();
      if (this.adrenalineShaderStartTime == 0L) {
        this.adrenalineShaderStartTime = currentTime;
      }
      float progress = MathHelper
          .clamp((((currentTime - this.adrenalineShaderStartTime)
              - Minecraft.getInstance().getRenderPartialTicks())) / 5000.0F, 0.0F, 1.0F);
      if (!shaderLoaded) {
        if (gameRenderer.getShaderGroup() != null) {
          gameRenderer.stopUseShader();
        }
        gameRenderer.loadShader(ADRENALINE_SHADER);
      }
      ShaderGroup shaderGroup = gameRenderer.getShaderGroup();
      RenderUtil.updateUniform("Saturation", progress * 0.25F, shaderGroup);
    } else if (shaderLoaded) {
      this.adrenalineShaderStartTime = 0L;
      gameRenderer.stopUseShader();
    }
  }

  private static void renderAmmo(ItemRenderer itemRenderer, FontRenderer fontRenderer, int width,
      int height, int ammo, ItemStack magazineStack) {
    if (magazineStack.getItem() instanceof MagazineItem) {
      MagazineItem magazine = (MagazineItem) magazineStack.getItem();
      String text = ammo + "/" + magazine.getSize();
      int x = width - 15 - fontRenderer.getStringWidth(text);
      int y = height - 10 - fontRenderer.FONT_HEIGHT;
      fontRenderer.drawStringWithShadow(text, x, y, 0xFFFFFF);
      itemRenderer.renderItemAndEffectIntoGUI(magazineStack, x - 15, y - 5);
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
      RenderSystem.disableAlphaTest();

      RenderUtil.bind(res);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1 - healthPercentage);
      RenderUtil.drawTexturedRectangle(0, 0, width, height);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.enableAlphaTest();
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
