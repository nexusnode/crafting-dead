/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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

import java.lang.reflect.Field;
import java.util.List;
import org.lwjgl.opengl.GL11;
import com.craftingdead.immerse.client.shader.RoundedFrameShader;
import com.craftingdead.immerse.client.shader.RoundedRectShader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderDefault;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class RenderUtil {

  private static final Minecraft minecraft = Minecraft.getInstance();

  private static final Field listShaders =
      ObfuscationReflectionHelper.findField(ShaderGroup.class, "field_148031_d");

  public static void updateUniform(String name, float value, ShaderGroup shaderGroup) {
    if (shaderGroup != null) {
      try {
        @SuppressWarnings("unchecked")
        List<Shader> shaders = (List<Shader>) listShaders.get(shaderGroup);
        for (Shader shader : shaders) {
          ShaderDefault variable = shader.getShaderManager().getShaderUniform(name);
          if (variable != null) {
            variable.set(value);
          }
        }
      } catch (IllegalArgumentException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void drawRoundedFrame(double x, double y, double x2, double y2, int colour,
      float radius) {
    ShaderLinkHelper.func_227804_a_(RoundedFrameShader.INSTANCE.getProgram());
    RoundedFrameShader.INSTANCE.setRadius(radius - 1);
    RoundedRectShader.INSTANCE.setInnerRect((float) x + radius, (float) y + radius,
        (float) x2 - radius, (float) y2 - radius);
    fill(x, y, x2, y2, colour);
    ShaderLinkHelper.func_227804_a_(0);
  }

  public static void roundedFill(double x, double y, double x2, double y2, int colour,
      float radius) {
    ShaderLinkHelper.func_227804_a_(RoundedRectShader.INSTANCE.getProgram());
    RoundedRectShader.INSTANCE.setRadius(radius - 1); // we have feather radius 1px
    RoundedRectShader.INSTANCE.setInnerRect((float) x + radius, (float) y + radius,
        (float) x2 - radius, (float) y2 - radius);
    fill(x, y, x2, y2, colour);
    ShaderLinkHelper.func_227804_a_(0);
  }

  public static void fill(double x, double y, double x2, double y2, int colour) {
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

    float f3 = (float) (colour >> 24 & 255) / 255.0F;
    float f = (float) (colour >> 16 & 255) / 255.0F;
    float f1 = (float) (colour >> 8 & 255) / 255.0F;
    float f2 = (float) (colour & 255) / 255.0F;
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    RenderSystem.enableBlend();
    RenderSystem.disableTexture();
    RenderSystem.defaultBlendFunc();
    buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
    buffer.pos(x, y2, 0.0D).color(f, f1, f2, f3).endVertex();
    buffer.pos(x2, y2, 0.0D).color(f, f1, f2, f3).endVertex();
    buffer.pos(x2, y, 0.0D).color(f, f1, f2, f3).endVertex();
    buffer.pos(x, y, 0.0D).color(f, f1, f2, f3).endVertex();
    tessellator.draw();
    RenderSystem.enableTexture();
    RenderSystem.disableBlend();
  }

  public static void fillGradient(double x, double y, double x2, double y2, int startColor,
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

    float endAlpha = (float) (endColor >> 24 & 255) / 255.0F;
    float endRed = (float) (endColor >> 16 & 255) / 255.0F;
    float endGreen = (float) (endColor >> 8 & 255) / 255.0F;
    float endBlue = (float) (endColor & 255) / 255.0F;

    buffer.pos(x, y2, 0.0D).color(startRed, startGreen, startBlue, startAlpha).endVertex();
    buffer.pos(x2, y2, 0.0D).color(endRed, endGreen, endBlue, endAlpha).endVertex();
    buffer.pos(x2, y, 0.0D).color(endRed, endGreen, endBlue, endAlpha).endVertex();
    buffer.pos(x, y, 0.0D).color(startRed, startGreen, startBlue, startAlpha).endVertex();
    tessellator.draw();

    RenderSystem.shadeModel(GL11.GL_FLAT);
    RenderSystem.enableAlphaTest();
    RenderSystem.disableBlend();
    RenderSystem.enableTexture();
  }

  public static void blit(double x, double y, float width, float height, float textureX,
      float textureY) {
    blit(x, y, x + width, y + height, textureX, textureY, textureX + width, textureY + height, 256,
        256);
  }

  public static void blit(double x, double y, double x2, double y2, float textureX, float textureY,
      float textureX2, float textureY2, float width, float height) {
    float u = textureX / width;
    float u2 = textureX2 / width;
    float v = textureY / height;
    float v2 = textureY2 / height;
    blit(x, y, x2, y2, u, v, u2, v2);
  }

  public static void blit(double x, double y, double width, double height) {
    blit(x, y, x + width, y + height, 0.0F, 1.0F, 1.0F, 0.0F);
  }

  public static void blit(double x, double y, double x2, double y2, float u, float v, float u2,
      float v2) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    bufferbuilder.pos(x, y2, 0.0D).tex(u, v).endVertex();
    bufferbuilder.pos(x2, y2, 0.0D).tex(u2, v).endVertex();
    bufferbuilder.pos(x2, y, 0.0D).tex(u2, v2).endVertex();
    bufferbuilder.pos(x, y, 0.0D).tex(u, v2).endVertex();
    tessellator.draw();
  }

  public static void bind(ResourceLocation resourceLocation) {
    minecraft.getTextureManager().bindTexture(resourceLocation);
  }

  public static int lerp(int colour1, int colour2, float pct) {
    return getColour(lerp(getColour4i(colour1), getColour4i(colour2), pct));
  }

  public static int[] lerp(int[] colour1, int[] colour2, float pct) {
    int[] rgba = new int[4];
    for (int i = 0; i < 4; i++) {
      rgba[i] = MathHelper.ceil(MathHelper.lerp(pct, colour1[i], colour2[i]));
    }
    return rgba;
  }

  public static float[] getColour4f(int[] colour4i) {
    return new float[] {colour4i[0] / 255.0F, colour4i[1] / 255.0F, colour4i[2] / 255.0F,
        colour4i[3] / 255.0F};
  }

  public static int[] getColour4i(int colour) {
    int[] rgba = new int[4];
    rgba[0] = (colour >> 16) & 0xFF;
    rgba[1] = (colour >> 8) & 0xFF;
    rgba[2] = (colour >> 0) & 0xFF;
    rgba[3] = (colour >> 24) & 0xFF;
    return rgba;
  }

  public static int[] getColour4i(float[] colour4f) {
    return new int[] {MathHelper.ceil(colour4f[0] * 255), MathHelper.ceil(colour4f[1] * 255),
        MathHelper.ceil(colour4f[2] * 255), MathHelper.ceil(colour4f[3] * 255)};
  }

  public static int getColour(int[] colour4i) {
    return ((colour4i[3] & 0xFF) << 24) | ((colour4i[0] & 0xFF) << 16) | ((colour4i[1] & 0xFF) << 8)
        | ((colour4i[2] & 0xFF) << 0);
  }
}
