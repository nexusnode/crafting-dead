/**
 * Crafting Dead Copyright (C) 2020 Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.immerse.client.gui.component;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

public class BlurComponent extends Component<BlurComponent> {

  private static final Logger logger = LogManager.getLogger();

  private static final ResourceLocation BLUR_SHADER =
      new ResourceLocation("shaders/post/blur.json");

  private final Minecraft minecraft = Minecraft.getInstance();

  private float radius;

  private ShaderGroup blurShader;

  private float lastFramebufferWidth;
  private float lastFramebufferHeight;

  public BlurComponent() {
    this(-1);
  }

  public BlurComponent(float radius) {
    this.radius = radius;
  }

  @Override
  public void added() {
    super.added();
    if (this.blurShader != null) {
      this.blurShader.close();
    }

    try {
      this.blurShader = new ShaderGroup(this.minecraft.getTextureManager(),
          this.minecraft.getResourceManager(), this.minecraft.getFramebuffer(), BLUR_SHADER);
      if (this.radius != -1) {
        RenderUtil.updateUniform("Radius", this.radius, this.blurShader);
      }
      this.blurShader.createBindFramebuffers(this.minecraft.getMainWindow().getFramebufferWidth(),
          this.minecraft.getMainWindow().getFramebufferHeight());
    } catch (JsonSyntaxException | IOException ioexception) {
      logger.warn("Failed to load shader: {}", BLUR_SHADER, ioexception);
      this.blurShader = null;
    }
  }

  @Override
  public void removed() {
    super.removed();
    if (this.blurShader != null) {
      this.blurShader.close();
    }
  }

  @Override
  public void tick() {
    super.tick();
    float framebufferWidth = this.minecraft.getFramebuffer().framebufferTextureWidth;
    float framebufferHeight = this.minecraft.getFramebuffer().framebufferTextureHeight;
    // Can't use #resized as it's called before the framebuffer is resized.
    if (framebufferWidth != this.lastFramebufferWidth
        || framebufferHeight != this.lastFramebufferHeight) {
      if (this.blurShader != null) {
        this.blurShader.createBindFramebuffers(this.mainWindow.getFramebufferWidth(),
            this.mainWindow.getFramebufferHeight());
      }
      this.lastFramebufferWidth = framebufferWidth;
      this.lastFramebufferHeight = framebufferHeight;
    }
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    final double scale = this.minecraft.getMainWindow().getGuiScaleFactor();
    GL11.glEnable(GL11.GL_SCISSOR_TEST);
    GL11.glScissor((int) (this.getX() * scale),
        (int) (this.mainWindow.getFramebufferHeight() - ((this.getY() + this.getHeight()) * scale)),
        (int) (this.getWidth() * scale), (int) (this.getHeight() * scale));
    this.blurShader.render(partialTicks);
    GL11.glDisable(GL11.GL_SCISSOR_TEST);
    this.minecraft.getFramebuffer().bindFramebuffer(false);
  }
}
