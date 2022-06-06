package sm0keysa1m0n.bliss;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
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

public class Skia implements AutoCloseable {

  private DirectContext context;

  public Surface surface;
  private BackendRenderTarget backendRenderTarget;

  private Canvas canvas;

  public void init(RenderTarget renderTarget) {
    RenderSystem.assertOnRenderThread();

    // Lazy init context.
    if (this.context == null) {
      this.context = DirectContext.makeGL();
    }
    renderTarget.enableStencil();

    this.closeRenderTarget();
    this.backendRenderTarget = BackendRenderTarget.makeGL(
        renderTarget.width, renderTarget.height,
        /* samples */ 0,
        /* stencil */ 8,
        renderTarget.frameBufferId,
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
