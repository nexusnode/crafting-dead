package com.craftingdead.mod.client.util;

import java.awt.Color;
import java.util.Random;
import java.util.UUID;
import javax.vecmath.Vector2d;
import org.lwjgl.opengl.GL11;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.util.PlayerResource;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class RenderUtil {

  public static final ResourceLocation ICONS =
      new ResourceLocation(CraftingDead.ID, "textures/gui/icons.png");

  public static final ResourceLocation SPRINT =
      new ResourceLocation(CraftingDead.ID, "textures/gui/icons.png");

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static void drawGradientRectangle(double x, double y, double x2, double y2, int startColor,
      int endColor) {
    GlStateManager.disableTexture();
    GlStateManager.enableBlend();
    GlStateManager.disableAlphaTest();
    GlStateManager
        .blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO);
    GlStateManager.shadeModel(GL11.GL_SMOOTH);

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

    buffer.pos(x, y2, 0.0D).color(startRed, startGreen, startBlue, startAlpha).endVertex();
    buffer.pos(x2, y2, 0.0D).color(endRed, endGreen, endBlue, endAlpha).endVertex();
    buffer.pos(x2, y, 0.0D).color(endRed, endGreen, endBlue, endAlpha).endVertex();
    buffer.pos(x, y, 0.0D).color(startRed, startGreen, startBlue, startAlpha).endVertex();
    tessellator.draw();

    GlStateManager.shadeModel(GL11.GL_FLAT);
    GlStateManager.enableAlphaTest();
    GlStateManager.disableBlend();
    GlStateManager.enableTexture();
  }

  public static void drawTexturedRectangle(double x, double y, double width, double height,
      double textureX, double textureY) {
    drawTexturedRectangle(x, y, x + width, y + height, textureX, textureY, textureX + width,
        textureY + height, 256, 256);
  }

  public static void drawTexturedRectangle(double x, double y, double x2, double y2,
      double textureX, double textureY, double textureX2, double textureY2, double width,
      double height) {
    double u = textureX / width;
    double u2 = textureX2 / width;
    double v = textureY / height;
    double v2 = textureY2 / height;
    drawTexturedRectangle(x, y, x2, y2, u, v, u2, v2);
  }

  public static void drawTexturedRectangle(double x, double y, double width, double height) {
    drawTexturedRectangle(x, y, x + width, y + height, 0.0D, 1.0D, 1.0D, 0.0D);
  }

  public static void drawTexturedRectangle(double x, double y, double x2, double y2, double u,
      double v, double u2, double v2) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    bufferbuilder.pos(x, y2, 0.0D).tex(u, v).endVertex();
    bufferbuilder.pos(x2, y2, 0.0D).tex(u2, v).endVertex();
    bufferbuilder.pos(x2, y, 0.0D).tex(u2, v2).endVertex();
    bufferbuilder.pos(x, y, 0.0D).tex(u, v2).endVertex();
    tessellator.draw();
  }

  public static ResourceLocation getPlayerAvatar(UUID playerId) {
    ResourceLocation resourceLocation =
        new ResourceLocation(CraftingDead.ID, "textures/avatars/" + playerId + ".png");
    ITextureObject object = minecraft.getTextureManager().getTexture(resourceLocation);
    if (object == null) {
      DownloadingTexture imageData =
          new DownloadingTexture(null, PlayerResource.AVATAR_URL.getUrl(playerId),
              new ResourceLocation(CraftingDead.ID, "textures/gui/avatar.png"), null);
      minecraft.getTextureManager().loadTexture(resourceLocation, imageData);
    }
    return resourceLocation;
  }

  public static void renderModel(IBakedModel model, VertexFormat vertextFormat) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    buffer.begin(GL11.GL_QUADS, vertextFormat);
    for (BakedQuad bakedquad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
      buffer.addVertexData(bakedquad.getVertexData());
    }
    tessellator.draw();
  }

  public static void renderModel(IBakedModel model, VertexFormat vertexFormat, int color) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    buffer.begin(GL11.GL_QUADS, vertexFormat);
    for (BakedQuad bakedquad : model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE)) {
      LightUtil.renderQuadColor(buffer, bakedquad, color);
    }
    tessellator.draw();
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

  public static Vector2d scaleToFit(final double imageWidth, final double imageHeight) {
    double widthScale = minecraft.mainWindow.getWidth() / imageWidth;
    double heightScale = minecraft.mainWindow.getHeight() / imageHeight;

    double scaledImageWidth = imageWidth * widthScale;
    double scaledImageHeight = imageHeight * widthScale;

    if (scaledImageHeight < minecraft.mainWindow.getHeight()) {
      scaledImageWidth = imageWidth * heightScale;
      scaledImageHeight = imageHeight * heightScale;
    }

    Vector2d finalSize = new Vector2d(scaledImageWidth, scaledImageHeight);
    finalSize.scale(1 / minecraft.mainWindow.getGuiScaleFactor());
    return finalSize;
  }
}
