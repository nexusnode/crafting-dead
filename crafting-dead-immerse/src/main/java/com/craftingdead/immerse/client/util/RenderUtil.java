/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.util;

import java.io.IOException;
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

public class RenderUtil {

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static void registerSvgTexture(ResourceLocation resourceLocation) {
    try (var inputStream =
        minecraft.getResourceManager().getResource(resourceLocation).getInputStream()) {

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void renderTextRight(Font fontRenderer, PoseStack poseStack, float x,
      float y, Component text, int colour, boolean shadow) {
    if (shadow) {
      fontRenderer.drawShadow(poseStack, text, x - fontRenderer.width(text),
          y, colour);
    } else {
      fontRenderer.draw(poseStack, text, x - fontRenderer.width(text),
          y, colour);
    }
  }

  public static void renderTextRight(Font fontRenderer, PoseStack poseStack, float x,
      float y, String text, int colour, boolean shadow) {
    fontRenderer.drawShadow(poseStack, text, x - fontRenderer.width(text), y, colour,
        shadow);
  }

  public static void fillWithShadow(PoseStack poseStack, float x, float y, float width,
      float height, int colour) {
    fill(poseStack, x - 1, y - 1, x + width + 1, y + height + 1, 0x4D000000);
    fill(poseStack, x, y, x + width, y + height, colour);
  }

  public static void fillWidthHeight(PoseStack poseStack, float x, float y, float width,
      float height, int colour) {
    fill(poseStack, x, y, x + width, y + height, colour);
  }

  public static void fill(PoseStack poseStack, float x, float y, float x2, float y2,
      long colour) {
    fill(poseStack, x, y, 0.0F, x2, y2, colour);
  }

  public static void fill(PoseStack poseStack, float x, float y, float z, float x2,
      float y2, long colour) {
    float alpha = (float) (colour >> 24 & 255) / 255.0F;
    float red = (float) (colour >> 16 & 255) / 255.0F;
    float green = (float) (colour >> 8 & 255) / 255.0F;
    float blue = (float) (colour & 255) / 255.0F;
    fill(poseStack.last().pose(), x, y, z, x2, y2, red, green, blue, alpha);
  }

  public static void fill(Matrix4f matrix, float x, float y, float z, float x2, float y2,
      float red, float green, float blue, float alpha) {
    if (x < x2) {
      float i = x;
      x = x2;
      x2 = i;
    }
    if (y < y2) {
      float j = y;
      y = y2;
      y2 = j;
    }
    final var tessellator = Tesselator.getInstance();
    final var builder = tessellator.getBuilder();
    RenderSystem.enableBlend();
    RenderSystem.disableTexture();
    RenderSystem.defaultBlendFunc();
    builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    builder.vertex(matrix, x, y2, z).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, x2, y2, z).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, x2, y, z).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, x, y, z).color(red, green, blue, alpha).endVertex();
    tessellator.end();
    RenderSystem.enableTexture();
    RenderSystem.disableBlend();
  }

  public static void blit(PoseStack poseStack, float x, float y, float width, float height) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    blit(poseStack.last().pose(), x, y, x + width, y + height, 0.0F, 1.0F, 1.0F, 0.0F);
  }

  public static void blit(PoseStack poseStack, float x, float y, float x2, float y2,
      float textureX, float textureY, float textureX2, float textureY2, float width, float height) {
    float u = textureX / width;
    float u2 = textureX2 / width;
    float v = textureY / height;
    float v2 = textureY2 / height;
    blit(poseStack.last().pose(), x, y, x2, y2, u, v, u2, v2);
  }

  public static void blit(Matrix4f matrix, float x, float y, float x2, float y2,
      float u, float v, float u2, float v2) {
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
      ResourceLocation skin, PoseStack poseStack, float x, float y, float width, float height) {
    RenderSystem.setShaderTexture(0, skin);
    spriteBlit(poseStack, x, y, width, height, 8.0F, 8.0F, 8, 8, 64, 64);
  }

  public static void spriteBlit(PoseStack poseStack, float x, float y,
      float width, float height, float spriteX, float spriteY, float spriteWidth,
      float spriteHeight, float textureWidth, float textureHeight) {
    spriteBlit(poseStack, x, y, x + width, y + height, 0, spriteWidth, spriteHeight, spriteX,
        spriteY, textureWidth, textureHeight);
  }

  public static void spriteBlit(PoseStack poseStack, float x, float y, float x2,
      float y2, float z, float spriteWidth, float spriteHeight,
      float spriteX, float spriteY, float textureWidth, float textureHeight) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    blit(poseStack.last().pose(), x, y, x2, y2,
        spriteX / textureWidth,
        (spriteY + spriteHeight) / textureHeight,
        (spriteX + spriteWidth) / textureWidth,
        spriteY / textureHeight);
  }

  public static void renderPlayerListRow(PoseStack poseStack, int x, int y, int width,
      int height, Component ping, Component username,
      Component... stats) {

    final int statWidth = 31;
    final int pingWidth = 26;
    final int columnSpacing = 1;
    final int textYOffset = 1 + (height - minecraft.font.lineHeight) / 2;

    RenderSystem.setShader(GameRenderer::getPositionColorShader);

    fillWidthHeight(poseStack, x, y, pingWidth, height, 0xB4000000);
    GuiComponent.drawCenteredString(poseStack, minecraft.font, ping,
        x + 13, y + textYOffset, 0xFFFFFFFF);

    int usernameColumnWidth =
        width - pingWidth - columnSpacing - (stats.length * (statWidth));
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    fillWidthHeight(poseStack, x + pingWidth + columnSpacing, y, usernameColumnWidth, height,
        0xB4000000);
    minecraft.font.drawShadow(poseStack, username, x + 54, y + textYOffset,
        0xFFFFFFFF);

    int statsX = x + pingWidth - 8 + usernameColumnWidth;
    for (int i = 0; i < stats.length; i++) {
      var stat = stats[i];
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      RenderUtil.fillWidthHeight(poseStack, statsX + 10 + (i * 31), y, 30, height, 0xB4000000);
      GuiComponent.drawCenteredString(poseStack, minecraft.font, stat,
          statsX + 10 + (i * 31) + 16, y + textYOffset, 0xFFFFFFFF);
    }
  }
}
