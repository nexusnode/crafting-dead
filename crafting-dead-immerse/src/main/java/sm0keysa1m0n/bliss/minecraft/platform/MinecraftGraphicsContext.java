package sm0keysa1m0n.bliss.minecraft.platform;

import java.nio.FloatBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.math.Matrix4f;
import io.github.humbleui.skija.BackendRenderTarget;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ColorSpace;
import io.github.humbleui.skija.DirectContext;
import io.github.humbleui.skija.FramebufferFormat;
import io.github.humbleui.skija.Matrix33;
import io.github.humbleui.skija.PixelGeometry;
import io.github.humbleui.skija.Surface;
import io.github.humbleui.skija.SurfaceColorFormat;
import io.github.humbleui.skija.SurfaceOrigin;
import io.github.humbleui.skija.SurfaceProps;
import net.minecraftforge.fml.loading.FMLLoader;
import sm0keysa1m0n.bliss.platform.Cursor;
import sm0keysa1m0n.bliss.platform.GraphicsContext;
import sm0keysa1m0n.bliss.platform.ScissorStack;

public class MinecraftGraphicsContext implements GraphicsContext, AutoCloseable {

  private final DirectContext context;

  private Surface surface;
  private BackendRenderTarget backendRenderTarget;

  private Canvas canvas;

  private final long window;
  private final RenderTarget renderTarget;

  private float scale;

  private long ibeamCursor = MemoryUtil.NULL;

  public MinecraftGraphicsContext(long window, RenderTarget renderTarget) {
    RenderSystem.assertOnRenderThread();
    this.window = window;
    this.renderTarget = renderTarget;
    this.renderTarget.enableStencil();
    this.context = DirectContext.makeGL();

    if (FMLLoader.isProduction()) {
      LwjglNativeUtil.load("lwjgl_yoga");
    }
  }

  public void init(float scale) {
    RenderSystem.assertOnRenderThread();
    this.scale = scale;
    this.closeRenderTarget();
    this.backendRenderTarget = BackendRenderTarget.makeGL(
        this.renderTarget.width, this.renderTarget.height,
        /* samples */ 0,
        /* stencil */ 8,
        this.renderTarget.frameBufferId,
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

  @Override
  public Surface surface() {
    return this.surface;
  }

  @Override
  public Canvas canvas() {
    return this.canvas;
  }

  @Override
  public void begin() {
    RenderSystem.assertOnRenderThread();

    // Undo Minecraft's changes
    RenderSystem.pixelStore(GL11.GL_UNPACK_ROW_LENGTH, 0);
    RenderSystem.pixelStore(GL11.GL_UNPACK_SKIP_PIXELS, 0);
    RenderSystem.pixelStore(GL11.GL_UNPACK_SKIP_ROWS, 0);
    RenderSystem.pixelStore(GL11.GL_UNPACK_ALIGNMENT, 4);

    this.context.resetAll();
  }

  @Override
  public void end() {
    RenderSystem.assertOnRenderThread();

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

  public static Matrix33 convert(Matrix4f matrix) {
    var arr = new float[16];
    matrix.storeTransposed(FloatBuffer.wrap(arr));
    return new Matrix33(arr[0], arr[1], arr[3], arr[4], arr[5], arr[7], arr[12], arr[13], arr[15]);
  }

  @Override
  public int width() {
    return this.renderTarget.width;
  }

  @Override
  public int height() {
    return this.renderTarget.height;
  }

  @Override
  public float scale() {
    return this.scale;
  }

  @Override
  public void setCursor(Cursor cursor) {
    RenderSystem.assertOnRenderThread();
    GLFW.glfwSetCursor(this.window, switch (cursor) {
      case DEFAULT -> MemoryUtil.NULL;
      case IBEAM -> this.ibeamCursor == MemoryUtil.NULL
          ? this.ibeamCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR)
          : this.ibeamCursor;
    });
  }
}
