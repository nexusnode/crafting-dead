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

  public BlurComponent() {
    this(-1);
  }

  public BlurComponent(float radius) {
    this.radius = radius;
  }

  @Override
  protected void added() {
    if (this.blurShader != null) {
      this.blurShader.close();
    }

    try {
      this.blurShader = new ShaderGroup(this.minecraft.getTextureManager(),
          this.minecraft.getResourceManager(), this.minecraft.getFramebuffer(), BLUR_SHADER);
      if (this.radius != -1) {
        RenderUtil.updateUniform("Radius", this.radius, this.blurShader);
      }
      this.blurShader
          .createBindFramebuffers(this.minecraft.getMainWindow().getFramebufferWidth(),
              this.minecraft.getMainWindow().getFramebufferHeight());
    } catch (JsonSyntaxException | IOException ioexception) {
      logger.warn("Failed to load shader: {}", BLUR_SHADER, ioexception);
      this.blurShader = null;
    }
  }

  @Override
  protected void removed() {
    if (this.blurShader != null) {
      this.blurShader.close();
    }
  }

  @Override
  protected void resized() {
    if (this.blurShader != null) {
      this.blurShader
          .createBindFramebuffers(this.minecraft.getMainWindow().getFramebufferWidth(),
              this.minecraft.getMainWindow().getFramebufferHeight());
    }
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    final double scale = this.minecraft.getMainWindow().getGuiScaleFactor();
    GL11.glEnable(GL11.GL_SCISSOR_TEST);
    GL11
        .glScissor((int) (this.getX() * scale), (int) ((this.getY() * scale)),
            (int) (this.getWidth() * scale), (int) (this.getHeight() * scale));
    this.blurShader.render(partialTicks);
    GL11.glDisable(GL11.GL_SCISSOR_TEST);
    this.minecraft.getFramebuffer().bindFramebuffer(false);
  }
}
