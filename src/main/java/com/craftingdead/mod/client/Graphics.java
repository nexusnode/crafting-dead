package com.craftingdead.mod.client;

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

public class Graphics {

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static void drawRectangle(double x, double y, double width, double height, int color,
      float alpha, boolean shadow) {
    if (shadow) {
      drawRectangle(x - 1, y - 1, width + 2, height + 2, color, alpha * 0.3F, false);
    }

    Tessellator tessellator = Tessellator.getInstance();

    GlStateManager.enableBlend();
    GlStateManager.disableTexture();

    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
        GlStateManager.DestFactor.ZERO);

    float f = (float) (color >> 16 & 255) / 255.0F;
    float f1 = (float) (color >> 8 & 255) / 255.0F;
    float f2 = (float) (color & 255) / 255.0F;
    GlStateManager.color4f(f, f1, f2, alpha);

    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
    bufferbuilder.pos((double) x, (double) y + height, 0.0D).endVertex();
    bufferbuilder.pos((double) x + width, (double) y + height, 0.0D).endVertex();
    bufferbuilder.pos((double) x + width, (double) y, 0.0D).endVertex();
    bufferbuilder.pos((double) x, (double) y, 0.0D).endVertex();
    tessellator.draw();

    GlStateManager.enableTexture();
    GlStateManager.disableBlend();
  }

  public static void drawGradientRectangle(double x, double y, double width, double height,
      int startColor, int endColor, float alpha) {
    GlStateManager.disableTexture();
    GlStateManager.enableBlend();
    GlStateManager.disableAlphaTest();
    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
        GlStateManager.DestFactor.ZERO);
    GlStateManager.shadeModel(7425);
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

    float f = (float) (startColor >> 24 & 255) / 255.0F;
    float f1 = (float) (startColor >> 16 & 255) / 255.0F;
    float f2 = (float) (startColor >> 8 & 255) / 255.0F;
    float f3 = (float) (startColor & 255) / 255.0F;
    float f4 = (float) (endColor >> 24 & 255) / 255.0F;
    float f5 = (float) (endColor >> 16 & 255) / 255.0F;
    float f6 = (float) (endColor >> 8 & 255) / 255.0F;
    float f7 = (float) (endColor & 255) / 255.0F;

    bufferbuilder.pos((double) x, (double) y + height, 0.0D).color(f1, f2, f3, f).endVertex();
    bufferbuilder.pos((double) x + width, (double) y + height, 0.0D).color(f5, f6, f7, f4)
        .endVertex();
    bufferbuilder.pos((double) x + width, (double) y, 0.0D).color(f5, f6, f7, f4).endVertex();
    bufferbuilder.pos((double) x, (double) y, 0.0D).color(f1, f2, f3, f).endVertex();
    tessellator.draw();
    GlStateManager.shadeModel(7424);
    GlStateManager.disableBlend();
    GlStateManager.enableAlphaTest();
    GlStateManager.enableTexture();
  }

  public static void drawTexturedRectangle(double x, double y, double width, double height) {
    drawTexturedRectangle(x, y, width, height, 0.0D, 1.0D, 1.0D, 0.0D);
  }

  public static void drawTexturedRectangle(double x, double y, double textureX, double textureY,
      double width, double height) {
    drawTexturedRectangle(width / 2, height / 2, width, height, textureX, textureY + height,
        textureX + width, textureY);
  }

  public static void drawTexturedRectangle(double x, double y, double width, double height,
      double u, double v, double u2, double v2) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    bufferbuilder.pos(x, y + height, 0.0D).tex(u, v).endVertex();
    bufferbuilder.pos(x + width, y + height, 0.0D).tex(u2, v).endVertex();
    bufferbuilder.pos(x + width, y, 0.0D).tex(u2, v2).endVertex();
    bufferbuilder.pos(x, y, 0.0D).tex(u, v2).endVertex();
    tessellator.draw();
  }

  public static ResourceLocation getPlayerAvatar(UUID playerId) {
    ResourceLocation resourceLocation =
        new ResourceLocation(CraftingDead.MOD_ID, "textures/avatars/" + playerId + ".png");
    ITextureObject object = minecraft.getTextureManager().getTexture(resourceLocation);
    if (object == null) {
      DownloadingTexture imageData =
          new DownloadingTexture(null, PlayerResource.AVATAR_URL.getUrl(playerId),
              new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/avatar.png"), null);
      minecraft.getTextureManager().loadTexture(resourceLocation, imageData);
    }
    return resourceLocation;
  }

  @SuppressWarnings("deprecation")
  public static void renderModel(IBakedModel model, VertexFormat vertextFormat) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    buffer.begin(GL11.GL_QUADS, vertextFormat);
    for (BakedQuad bakedquad : model.getQuads(null, null, new Random())) {
      buffer.addVertexData(bakedquad.getVertexData());
    }
    tessellator.draw();
  }

  @SuppressWarnings("deprecation")
  public static void renderModel(IBakedModel model, VertexFormat vertexFormat, int color) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    buffer.begin(GL11.GL_QUADS, vertexFormat);
    for (BakedQuad bakedquad : model.getQuads(null, null, new Random())) {
      LightUtil.renderQuadColor(buffer, bakedquad, color);
    }
    tessellator.draw();
  }

  public static void bind(ResourceLocation resourceLocation) {
    minecraft.getTextureManager().bindTexture(resourceLocation);
  }
}
