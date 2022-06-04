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

package com.craftingdead.immerse.client.gui.view;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import io.github.humbleui.skija.BackendRenderTarget;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ColorSpace;
import io.github.humbleui.skija.DirectContext;
import io.github.humbleui.skija.FramebufferFormat;
import io.github.humbleui.skija.PixelGeometry;
import io.github.humbleui.skija.Surface;
import io.github.humbleui.skija.SurfaceColorFormat;
import io.github.humbleui.skija.SurfaceOrigin;
import io.github.humbleui.skija.SurfaceProps;
import io.github.humbleui.types.Rect;
import net.minecraft.client.Minecraft;

public class Skia implements AutoCloseable {

  private DirectContext context;

  public Surface surface;
  private BackendRenderTarget backendRenderTarget;

  private Canvas canvas;

  public int framebufferId = -1;
  public int colorBuffer = -1;
  public int depthBufferId = -1;

  public void init(RenderTarget renderTarget) {
    RenderSystem.assertOnRenderThread();

    // Lazy init context.
    if (this.context == null) {
      this.context = DirectContext.makeGL();
    }
    renderTarget.enableStencil();


    if (this.framebufferId != -1) {
      GL30.glDeleteFramebuffers(this.framebufferId);
      GL30.glDeleteRenderbuffers(this.colorBuffer);
      GL30.glDeleteRenderbuffers(this.depthBufferId);
    }
    this.framebufferId = GL30.glGenFramebuffers();
    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferId);

    this.colorBuffer = GL30.glGenRenderbuffers();
    GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, colorBuffer);
    GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_RGBA8, renderTarget.width,
        renderTarget.height);
    GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
        GL30.GL_RENDERBUFFER, this.colorBuffer);

    this.depthBufferId = GL30.glGenRenderbuffers();
    GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBufferId);
    GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, renderTarget.width,
        renderTarget.height);
    GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT,
        GL30.GL_RENDERBUFFER, this.depthBufferId);

    this.closeRenderTarget();
    this.backendRenderTarget = BackendRenderTarget.makeGL(
        Minecraft.getInstance().getWindow().getWidth(),
        Minecraft.getInstance().getWindow().getHeight(),
        /* samples */0,
        /* stencil */8,
        /* fbId */1,
        FramebufferFormat.GR_GL_RGBA8);
    this.surface = Surface.makeFromBackendRenderTarget(
        this.context,
        this.backendRenderTarget,
        SurfaceOrigin.BOTTOM_LEFT,
        SurfaceColorFormat.RGBA_8888,
        ColorSpace.getDisplayP3(), // TODO load monitor profile
        new SurfaceProps(PixelGeometry.RGB_H));
    this.canvas = this.surface.getCanvas();
  }

  private void closeRenderTarget() {
    RenderSystem.assertOnRenderThread();
    if (this.surface != null) {
      this.surface.close();
      this.surface = null;
    }

    if (this.backendRenderTarget != null) {
      this.backendRenderTarget.close();
      this.backendRenderTarget = null;
    }
  }

  public Canvas canvas() {
    return this.canvas;
  }

  public void begin() {
    RenderSystem.assertOnRenderThread();

    // Undo Minecraft's changes
    RenderSystem.pixelStore(GL11.GL_UNPACK_ROW_LENGTH, 0);
    RenderSystem.pixelStore(GL11.GL_UNPACK_SKIP_PIXELS, 0);
    RenderSystem.pixelStore(GL11.GL_UNPACK_SKIP_ROWS, 0);
    RenderSystem.pixelStore(GL11.GL_UNPACK_ALIGNMENT, 4);

    this.context.resetAll();

    this.canvas.save();
    var scissor = ScissorStack.peek();
    if (scissor != null) {
      this.canvas.clipRect(
          Rect.makeXYWH(scissor.getX(), scissor.getY(), scissor.getWidth(), scissor.getHeight()));
    }
  }

  public void end() {
    RenderSystem.assertOnRenderThread();
    this.canvas.restore();

    this.surface.flush();

    // Reset OpenGL state and keep it in sync with Minecraft's tracked state.

    BufferUploader.reset();
    GL33.glBindSampler(0, 0);

    RenderSystem.disableBlend();
    GL11.glDisable(GL11.GL_BLEND);

    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

    RenderSystem.blendEquation(GL14.GL_FUNC_ADD);
    GL14.glBlendEquation(GL14.GL_FUNC_ADD);

    RenderSystem.colorMask(true, true, true, true);
    GL11.glColorMask(true, true, true, true);

    RenderSystem.depthMask(true);
    GL11.glDepthMask(true);

    RenderSystem.disableScissor();
    GL11.glDisable(GL11.GL_SCISSOR_TEST);
    ScissorStack.apply();

    GL11.glDisable(GL11.GL_STENCIL_TEST);

    RenderSystem.disableDepthTest();
    GL11.glDisable(GL11.GL_DEPTH);

    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    RenderSystem.activeTexture(GL13.GL_TEXTURE0);
  }

  @Override
  public void close() {
    RenderSystem.assertOnRenderThread();
    this.closeRenderTarget();
    this.context.close();
  }
}
