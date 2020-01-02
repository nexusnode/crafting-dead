package com.craftingdead.mod.client.util;

import java.awt.Color;
import java.util.UUID;
import org.lwjgl.opengl.GL11;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.util.PlayerResource;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderUtil {

  public static final ResourceLocation ICONS =
      new ResourceLocation(CraftingDead.ID, "textures/gui/icons.png");

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static void drawGradientRectangle(double x, double y, double x2, double y2, int startColor,
      int endColor) {
    RenderSystem.disableTexture();
    RenderSystem.enableBlend();
    RenderSystem.disableAlphaTest();
    RenderSystem.defaultBlendFunc();
    RenderSystem.shadeModel(GL11.GL_SMOOTH);

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

    float startAlpha = (float) (startColor >> 24 & 255) / 255.0F;
    float startRed = (float) (startColor >> 16 & 255) / 255.0F;
    float startGreen = (float) (startColor >> 8 & 255) / 255.0F;
    float startBlue = (float) (startColor & 255) / 255.0F;

    float endAlpha = (float) (startColor >> 24 & 255) / 255.0F;
    float endRed = (float) (endColor >> 16 & 255) / 255.0F;
    float endGreen = (float) (endColor >> 8 & 255) / 255.0F;
    float endBlue = (float) (endColor & 255) / 255.0F;

    buffer
        .func_225582_a_(x, y2, 0.0D)
        .func_227885_a_(startRed, startGreen, startBlue, startAlpha)
        .endVertex();
    buffer
        .func_225582_a_(x2, y2, 0.0D)
        .func_227885_a_(endRed, endGreen, endBlue, endAlpha)
        .endVertex();
    buffer
        .func_225582_a_(x2, y, 0.0D)
        .func_227885_a_(endRed, endGreen, endBlue, endAlpha)
        .endVertex();
    buffer
        .func_225582_a_(x, y, 0.0D)
        .func_227885_a_(startRed, startGreen, startBlue, startAlpha)
        .endVertex();
    tessellator.draw();

    RenderSystem.shadeModel(GL11.GL_FLAT);
    RenderSystem.enableAlphaTest();
    RenderSystem.disableBlend();
    RenderSystem.enableTexture();
  }

  public static void drawTexturedRectangle(double x, double y, float width, float height,
      float textureX, float textureY) {
    drawTexturedRectangle(x, y, x + width, y + height, textureX, textureY, textureX + width,
        textureY + height, 256, 256);
  }

  public static void drawTexturedRectangle(double x, double y, double x2, double y2, float textureX,
      float textureY, float textureX2, float textureY2, float width, float height) {
    float u = textureX / width;
    float u2 = textureX2 / width;
    float v = textureY / height;
    float v2 = textureY2 / height;
    drawTexturedRectangle(x, y, x2, y2, u, v, u2, v2);
  }

  public static void drawTexturedRectangle(double x, double y, double width, double height) {
    drawTexturedRectangle(x, y, x + width, y + height, 0.0F, 1.0F, 1.0F, 0.0F);
  }

  public static void drawTexturedRectangle(double x, double y, double x2, double y2, float u,
      float v, float u2, float v2) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    bufferbuilder.func_225582_a_(x, y2, 0.0D).func_225583_a_(u, v).endVertex();
    bufferbuilder.func_225582_a_(x2, y2, 0.0D).func_225583_a_(u2, v).endVertex();
    bufferbuilder.func_225582_a_(x2, y, 0.0D).func_225583_a_(u2, v2).endVertex();
    bufferbuilder.func_225582_a_(x, y, 0.0D).func_225583_a_(u, v2).endVertex();
    tessellator.draw();
  }

  public static ResourceLocation getPlayerAvatar(UUID playerId) {
    ResourceLocation resourceLocation =
        new ResourceLocation(CraftingDead.ID, "textures/avatars/" + playerId + ".png");
    Texture object = minecraft.getTextureManager().func_229267_b_(resourceLocation);
    if (object == null) {
      DownloadingTexture imageData =
          new DownloadingTexture(null, PlayerResource.AVATAR_URL.getUrl(playerId),
              new ResourceLocation(CraftingDead.ID, "textures/gui/avatar.png"), false, null);
      minecraft.getTextureManager().func_229263_a_(resourceLocation, imageData);
    }
    return resourceLocation;
  }

  public static void bind(ResourceLocation resourceLocation) {
    minecraft.getTextureManager().bindTexture(resourceLocation);
  }

  public static Color lerp(Color color1, Color color2, double pct) {
    double r = MathHelper.lerp(pct, color1.getRed(), color2.getRed());
    double g = MathHelper.lerp(pct, color1.getGreen(), color2.getGreen());
    double b = MathHelper.lerp(pct, color1.getBlue(), color2.getBlue());
    double a = MathHelper.lerp(pct, color1.getAlpha(), color2.getAlpha());
    return new Color((int) Math.round(r), (int) Math.round(g), (int) Math.round(b),
        (int) Math.round(a));
  }

  public static double getScale(final double imageWidth, final double imageHeight) {
    double widthScale = minecraft.func_228018_at_().getWidth() / imageWidth;
    double heightScale = minecraft.func_228018_at_().getHeight() / imageHeight;
    final double scale =
        imageHeight * widthScale < minecraft.func_228018_at_().getHeight() ? heightScale
            : widthScale;
    return scale / minecraft.func_228018_at_().getGuiScaleFactor();
  }
}
