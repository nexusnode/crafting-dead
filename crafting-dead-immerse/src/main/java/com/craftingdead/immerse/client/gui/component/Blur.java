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

package com.craftingdead.immerse.client.gui.component;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

public class Blur implements AutoCloseable {

  private static final Logger logger = LogManager.getLogger();

  private static final ResourceLocation BLUR_SHADER =
      new ResourceLocation(CraftingDeadImmerse.ID, "shaders/post/blur.json");

  private final Minecraft minecraft = Minecraft.getInstance();

  private ShaderGroup blurShader;

  private float lastFramebufferWidth;
  private float lastFramebufferHeight;

  public Blur() {
    this(-1);
  }

  public Blur(float radius) {
    try {
      this.blurShader = new ShaderGroup(this.minecraft.getTextureManager(),
          this.minecraft.getResourceManager(), this.minecraft.getFramebuffer(), BLUR_SHADER);
      this.setRadius(radius);
      this.blurShader.createBindFramebuffers(this.minecraft.getMainWindow().getFramebufferWidth(),
          this.minecraft.getMainWindow().getFramebufferHeight());
    } catch (JsonSyntaxException | IOException ioexception) {
      logger.warn("Failed to load shader: {}", BLUR_SHADER, ioexception);
      this.blurShader = null;
    }
  }

  public void setRadius(float radius) {
    if (radius > -1) {
      RenderUtil.updateUniform("Radius", radius, this.blurShader);
    }
  }

  public void tick() {
    float framebufferWidth = this.minecraft.getFramebuffer().framebufferTextureWidth;
    float framebufferHeight = this.minecraft.getFramebuffer().framebufferTextureHeight;
    // Can't use #resized as it's called before the framebuffer is resized.
    if (framebufferWidth != this.lastFramebufferWidth
        || framebufferHeight != this.lastFramebufferHeight) {
      if (this.blurShader != null) {
        this.blurShader.createBindFramebuffers(this.minecraft.getMainWindow().getFramebufferWidth(),
            this.minecraft.getMainWindow().getFramebufferHeight());
      }
      this.lastFramebufferWidth = framebufferWidth;
      this.lastFramebufferHeight = framebufferHeight;
    }
  }

  public void render(float x, float y, float width, float height, float partialTicks) {
    this.blurShader.render(partialTicks);
    // TODO Fixes Minecraft bug when using post-processing shaders.
    RenderSystem.enableTexture();

    this.minecraft.getFramebuffer().bindFramebuffer(false);
    Framebuffer framebuffer = this.blurShader.getFramebufferRaw("output");
    framebuffer.bindFramebufferTexture();
    float textureWidth = (float) (framebuffer.framebufferTextureWidth
        / this.minecraft.getMainWindow().getGuiScaleFactor());
    float textureHeight = (float) (framebuffer.framebufferTextureHeight
        / this.minecraft.getMainWindow().getGuiScaleFactor());
    float textureX = x;
    float textureY = (textureHeight - height) - y;
    RenderUtil.blit(x, y, x + width, y + height, textureX, textureY, textureX + width,
        textureY + height, textureWidth, textureHeight);
  }

  @Override
  public void close() {
    this.blurShader.close();
  }
}
