package com.craftingdead.mod.client.util;

import java.util.Random;
import java.util.UUID;
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
    buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);

    float startAlpha = (float) (startColor >> 24 & 255) / 255.0F;
    float startRed = (float) (startColor >> 16 & 255) / 255.0F;
    float startGreen = (float) (startColor >> 8 & 255) / 255.0F;
    float startBlue = (float) (startColor & 255) / 255.0F;

    float endAlpha = (float) (endColor >> 24 & 255) / 255.0F;
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

  @Deprecated
  public static void renderModel(IBakedModel model, VertexFormat vertextFormat) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    buffer.begin(GL11.GL_QUADS, vertextFormat);
    for (BakedQuad bakedquad : model.getQuads(null, null, new Random())) {
      buffer.addVertexData(bakedquad.getVertexData());
    }
    tessellator.draw();
  }

  @Deprecated
  public static void renderModel(IBakedModel model, VertexFormat vertexFormat, int color) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    buffer.begin(GL11.GL_QUADS, vertexFormat);
    for (BakedQuad bakedquad : model.getQuads(null, null, new Random())) {
      LightUtil.renderQuadColor(buffer, bakedquad, color);
    }
    tessellator.draw();
  }

  @SuppressWarnings("deprecation")
  public static void renderModel(IBakedModel model) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    BakedQuad bakedQuad1 = model.getQuads(null, null, new Random()).get(0);

    buffer.begin(GL11.GL_QUADS, bakedQuad1.getFormat());

    for (BakedQuad bakedquad : model.getQuads(null, null, new Random())) {
      LightUtil.renderQuadColor(buffer, bakedquad, -1);
    }

    tessellator.draw();
  }

  public static void bind(ResourceLocation resourceLocation) {
    minecraft.getTextureManager().bindTexture(resourceLocation);
  }
}
