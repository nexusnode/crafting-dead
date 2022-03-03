/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view;

import java.io.IOException;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

public class Blur implements AutoCloseable {

  private static final Logger logger = LogUtils.getLogger();

  private static final ResourceLocation BLUR_SHADER =
      new ResourceLocation(CraftingDeadImmerse.ID, "shaders/post/blur.json");

  private final Minecraft minecraft = Minecraft.getInstance();

  private PostChain postChain;

  private float lastWidth;
  private float lastHeight;

  public Blur() {
    this(-1);
  }

  public Blur(float radius) {
    try {
      this.postChain = new PostChain(this.minecraft.getTextureManager(),
          this.minecraft.getResourceManager(), this.minecraft.getMainRenderTarget(), BLUR_SHADER);
      this.setRadius(radius);
      this.postChain.resize(this.minecraft.getWindow().getWidth(),
          this.minecraft.getWindow().getHeight());
    } catch (JsonSyntaxException | IOException ioexception) {
      logger.warn("Failed to load shader: {}", BLUR_SHADER, ioexception);
      this.postChain = null;
    }
  }

  public void setRadius(float radius) {
    if (radius > -1 && this.postChain != null) {
      com.craftingdead.core.client.util.RenderUtil.updateUniform("Radius", radius, this.postChain);
    }
  }

  public void tick() {
    var width = this.minecraft.getMainRenderTarget().width;
    var height = this.minecraft.getMainRenderTarget().height;
    // Can't use #resized as it's called before the framebuffer is resized.
    if (width != this.lastWidth || height != this.lastHeight) {
      if (this.postChain != null) {
        this.postChain.resize(this.minecraft.getWindow().getWidth(),
            this.minecraft.getWindow().getHeight());
      }
      this.lastWidth = width;
      this.lastHeight = height;
    }
  }

  public void process(float partialTick) {
    if (this.postChain != null) {
      this.postChain.process(partialTick);
      this.minecraft.getMainRenderTarget().bindWrite(false);
    }
  }

  public void render(PoseStack poseStack, float x, float y, float width, float height) {
    if (this.postChain != null) {
      var renderTarget = this.postChain.getTempTarget("output");
      RenderSystem.setShaderTexture(0, renderTarget.getColorTextureId());
      var textureWidth = (float) (renderTarget.width
          / this.minecraft.getWindow().getGuiScale());
      var textureHeight = (float) (renderTarget.height
          / this.minecraft.getWindow().getGuiScale());
      var textureX = x;
      var textureY = (textureHeight - height) - y;
      RenderUtil.blit(poseStack, x, y, x + width, y + height, textureX, textureY,
          textureX + width, textureY + height, textureWidth, textureHeight);
    }
  }

  @Override
  public void close() {
    if (this.postChain != null) {
      this.postChain.close();
    }
  }
}
