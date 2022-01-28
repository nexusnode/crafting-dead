/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.client.util;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.ClientDist;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderUtil {

  private static final Minecraft minecraft = Minecraft.getInstance();
  private static final ClientDist clientDist = CraftingDeadImmerse.getInstance().getClientDist();

  public static void renderTextRight(Font fontRenderer, PoseStack matrixStack, float x,
      float y, Component text, int colour, boolean shadow) {
    if (shadow) {
      fontRenderer.drawShadow(matrixStack, text, x - fontRenderer.width(text),
          y, colour);
    } else {
      fontRenderer.draw(matrixStack, text, x - fontRenderer.width(text),
          y, colour);
    }
  }

  public static void renderTextRight(Font fontRenderer, PoseStack matrixStack, float x,
      float y, String text, int colour, boolean shadow) {
    fontRenderer.drawShadow(matrixStack, text, x - fontRenderer.width(text), y, colour,
        shadow);
  }

  public static void enableRoundedFrameShader(float x, float y, float x2, float y2, float radius) {
    RenderSystem.setShader(clientDist::getRoundedFrameShader);
    var shader = clientDist.getRoundedFrameShader();
    shader.getUniform("u_Radius").set(radius - 1);
    shader.getUniform("u_InnerRect").set(x + radius, y + radius, x2 - radius, y2 - radius);
  }

  public static void enableRoundedRectShader(float x, float y, float x2, float y2, float radius) {
    RenderSystem.setShader(clientDist::getRoundedRectShader);
    var shader = clientDist.getRoundedRectShader();
    shader.getUniform("u_Radius").set(radius - 1);
    shader.getUniform("u_InnerRect").set(x + radius, y + radius, x2 - radius, y2 - radius);
  }

  public static void fillWithShadow(PoseStack matrixStack, double x, double y, double width,
      double height, int colour) {
    fill(matrixStack, x - 1, y - 1, x + width + 1, y + height + 1, 0x4D000000);
    fill(matrixStack, x, y, x + width, y + height, colour);
  }

  public static void fillWidthHeight(PoseStack matrixStack, double x, double y, double width,
      double height, int colour) {
    fill(matrixStack, x, y, x + width, y + height, colour);
  }

  public static void fill(PoseStack matrixStack, double x, double y, double x2, double y2,
      long colour) {
    fill(matrixStack, x, y, 0.0D, x2, y2, colour);
  }

  public static void fill(PoseStack matrixStack, double x, double y, double z, double x2,
      double y2, long colour) {
    var alpha = (colour >> 24 & 255) / 255.0F;
    var red = (colour >> 16 & 255) / 255.0F;
    var green = (colour >> 8 & 255) / 255.0F;
    var blue = (colour & 255) / 255.0F;
    fill(matrixStack.last().pose(), x, y, z, x2, y2, red, green, blue, alpha);
  }

  public static void fill(Matrix4f matrix, double x, double y, double z, double x2, double y2,
      float red, float green, float blue, float alpha) {
    if (x < x2) {
      double i = x;
      x = x2;
      x2 = i;
    }
    if (y < y2) {
      double j = y;
      y = y2;
      y2 = j;
    }
    final var tessellator = Tesselator.getInstance();
    final var builder = tessellator.getBuilder();
    RenderSystem.enableBlend();
    RenderSystem.disableTexture();
    RenderSystem.defaultBlendFunc();
    builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    builder.vertex(x, y2, z).color(red, green, blue, alpha).endVertex();
    builder.vertex(x2, y2, z).color(red, green, blue, alpha).endVertex();
    builder.vertex(x2, y, z).color(red, green, blue, alpha).endVertex();
    builder.vertex(x, y, z).color(red, green, blue, alpha).endVertex();
    tessellator.end();
    RenderSystem.enableTexture();
    RenderSystem.disableBlend();
  }

  public static void blit(PoseStack matrixStack, float x, float y, float width,
      float height) {
    blit(matrixStack.last().pose(), x, y, x + width, y + height, 0.0F, 1.0F, 1.0F, 0.0F);
  }

  public static void blit(PoseStack matrixStack, float x, float y, float x2, float y2,
      float textureX, float textureY, float textureX2, float textureY2, float width, float height) {
    float u = textureX / width;
    float u2 = textureX2 / width;
    float v = textureY / height;
    float v2 = textureY2 / height;
    blit(matrixStack.last().pose(), x, y, x2, y2, u, v, u2, v2);
  }

  public static void blit(Matrix4f matrix, float x, float y, float x2, float y2,
      float u, float v, float u2, float v2) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    final var tesselator = Tesselator.getInstance();
    final var builder = tesselator.getBuilder();
    builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    builder.vertex(matrix, x, y2, 0.0F).uv(u, v).endVertex();
    builder.vertex(matrix, x2, y2, 0.0F).uv(u2, v).endVertex();
    builder.vertex(matrix, x2, y, 0.0F).uv(u2, v2).endVertex();
    builder.vertex(matrix, x, y, 0.0F).uv(u, v2).endVertex();
    tesselator.end();
  }

  public static void blitAvatar(
      ResourceLocation skin, PoseStack matrixStack, float x, float y, float width, float height) {
    RenderSystem.setShaderTexture(0, skin);
    spriteBlit(matrixStack, x, y, width, height, 8.0F, 8.0F, 8, 8, 64, 64);
  }

  public static void spriteBlit(PoseStack matrixStack, float x, float y,
      float width, float height, float spriteX, float spriteY, float spriteWidth,
      float spriteHeight, float textureWidth, float textureHeight) {
    spriteBlit(matrixStack, x, y, x + width, y + height, 0, spriteWidth, spriteHeight, spriteX,
        spriteY, textureWidth, textureHeight);
  }

  public static void spriteBlit(PoseStack matrixStack, float x, float y, float x2,
      float y2, float z, float spriteWidth, float spriteHeight,
      float spriteX, float spriteY, float textureWidth, float textureHeight) {
    blit(matrixStack.last().pose(), x, y, x2, y2,
        spriteX / textureWidth,
        (spriteY + spriteHeight) / textureHeight,
        (spriteX + spriteWidth) / textureWidth,
        spriteY / textureHeight);
  }

  public static long lerp(int colour1, int colour2, float pct) {
    return getColour(lerp(getColour4i(colour1), getColour4i(colour2), pct));
  }

  public static int[] lerp(int[] colour1, int[] colour2, float pct) {
    int[] rgba = new int[4];
    for (int i = 0; i < 4; i++) {
      rgba[i] = Mth.ceil(Mth.lerp(pct, colour1[i], colour2[i]));
    }
    return rgba;
  }

  public static float[] lerp(float[] colour1, float[] colour2, float pct) {
    float[] rgba = new float[4];
    for (int i = 0; i < 4; i++) {
      rgba[i] = Mth.lerp(pct, colour1[i], colour2[i]);
    }
    return rgba;
  }

  public static float[] getColour4f(int[] colour4i) {
    return new float[] {colour4i[0] / 255.0F, colour4i[1] / 255.0F, colour4i[2] / 255.0F,
        colour4i[3] / 255.0F};
  }

  public static int[] getColour4i(long colour) {
    int[] rgba = new int[4];
    rgba[0] = (int) ((colour >> 16) & 0xFF);
    rgba[1] = (int) (colour >> 8) & 0xFF;
    rgba[2] = (int) (colour >> 0) & 0xFF;
    rgba[3] = (int) (colour >> 24) & 0xFF;
    return rgba;
  }

  public static int[] getColour4i(float[] colour4f) {
    return new int[] {Mth.ceil(colour4f[0] * 255), Mth.ceil(colour4f[1] * 255),
        Mth.ceil(colour4f[2] * 255), Mth.ceil(colour4f[3] * 255)};
  }

  public static long getColour(int[] colour4i) {
    return ((long) (colour4i[3] & 0xFF) << 24) | ((colour4i[0] & 0xFF) << 16)
        | ((colour4i[1] & 0xFF) << 8)
        | ((colour4i[2] & 0xFF) << 0);
  }

  public static void renderPlayerListRow(PoseStack matrixStack, int x, int y, int width,
      int height, Component ping, Component username,
      Component... stats) {

    final int statWidth = 31;
    final int pingWidth = 26;
    final int columnSpacing = 1;
    final int textYOffset = 1 + (height - minecraft.font.lineHeight) / 2;

    RenderSystem.setShader(GameRenderer::getPositionColorShader);

    fillWidthHeight(matrixStack, x, y, pingWidth, height, 0xB4000000);
    GuiComponent.drawCenteredString(matrixStack, minecraft.font, ping,
        x + 13, y + textYOffset, 0xFFFFFFFF);

    int usernameColumnWidth =
        width - pingWidth - columnSpacing - (stats.length * (statWidth));
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    fillWidthHeight(matrixStack, x + pingWidth + columnSpacing, y, usernameColumnWidth, height,
        0xB4000000);
    minecraft.font.drawShadow(matrixStack, username, x + 54, y + textYOffset,
        0xFFFFFFFF);

    int statsX = x + pingWidth - 8 + usernameColumnWidth;
    for (int i = 0; i < stats.length; i++) {
      var stat = stats[i];
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      RenderUtil.fillWidthHeight(matrixStack, statsX + 10 + (i * 31), y, 30, height, 0xB4000000);
      GuiComponent.drawCenteredString(matrixStack, minecraft.font, stat,
          statsX + 10 + (i * 31) + 16, y + textYOffset, 0xFFFFFFFF);
    }
  }
}
