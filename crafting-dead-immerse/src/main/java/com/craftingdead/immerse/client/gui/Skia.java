package com.craftingdead.immerse.client.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL33;
import com.craftingdead.immerse.client.gui.view.ScissorStack;
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

  private  Canvas canvas;
  
  private transient boolean started;

  public void init(RenderTarget renderTarget) {
    RenderSystem.assertOnRenderThread();
    
    if(started) {
      throw new IllegalStateException();
    }
    
    // Lazy init context.
    if (this.context == null) {
      this.context = DirectContext.makeGL();
    }


    this.closeRenderTarget();
    this.backendRenderTarget = BackendRenderTarget.makeGL(
        Minecraft.getInstance().getWindow().getWidth(),
        Minecraft.getInstance().getWindow().getHeight(),
        /* samples */0,
        /* stencil */0,
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
    RenderSystem.assertOnRenderThread();
    if (this.canvas.isClosed()) {
      throw new IllegalStateException();
    }
    return this.canvas;
  }

  public void begin() {
    if(started) {
      throw new IllegalStateException();
    }
    started = true;
    RenderSystem.assertOnRenderThread();
    this.context.resetAll();

    this.canvas.save();
    var scissor = ScissorStack.peek();
    if (scissor != null) {
      this.canvas.clipRect(
          Rect.makeXYWH(scissor.getX(), scissor.getY(), scissor.getWidth(), scissor.getHeight()));
    }
  }

  public void end() {
    if(!started) {
      throw new IllegalStateException();
    }
    started = false;
    RenderSystem.assertOnRenderThread();
    this.canvas.restore();

    this.surface.flush();


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
  }

  @Override
  public void close() {
    if(started) {
      throw new IllegalStateException();
    }
    RenderSystem.assertOnRenderThread();
    this.closeRenderTarget();
    this.context.close();
  }
}
