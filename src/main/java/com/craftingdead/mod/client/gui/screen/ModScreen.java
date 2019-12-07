package com.craftingdead.mod.client.gui.screen;

import javax.vecmath.Vector2d;
import org.lwjgl.opengl.GL11;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.gui.transition.ITransition;
import com.craftingdead.mod.client.gui.transition.Transitions;
import com.craftingdead.mod.client.util.RenderUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public abstract class ModScreen extends Screen {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      new ResourceLocation(CraftingDead.ID, "textures/gui/background.png");

  private static final ResourceLocation SMOKE_TEXTURE =
      new ResourceLocation(CraftingDead.ID, "textures/gui/smoke.png");

  private static long backgroundStartTime = Util.milliTime();

  private static long fogStartTime = Util.milliTime();

  protected ModScreen(ITextComponent title) {
    super(title);
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
  }

  @Override
  public void renderBackground() {
    if (this.minecraft.world != null) {
      this.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
      return;
    }

    double pct =
        MathHelper.clamp(((Util.milliTime() - backgroundStartTime) / 1000.0F) / 10, 0.0F, 1.0F);
    if (pct == 1.0D) {
      backgroundStartTime = Util.milliTime();
    }

    GlStateManager.pushMatrix();
    {
      GlStateManager
          .translated(Math.sin(Math.toRadians(360 * pct)) * 2.5D,
              Math.cos(Math.toRadians(360 * pct)) * 2.5D, 0);
      double scale = 1.25D + Math.cos(Math.toRadians(360 * pct)) * 1.5D / 100.0D;
      GlStateManager.scaled(scale, scale, scale);

      Vector2d backgroundSize = RenderUtil.scaleToFit(2048, 1152);
      RenderUtil.bind(BACKGROUND_TEXTURE);
      // Enable bilinear filtering
      GlStateManager.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
      RenderUtil
          .drawTexturedRectangle((this.width / 2 - (backgroundSize.getX() * scale) / 2),
              this.height / 2 - (backgroundSize.getY() * scale) / 2, backgroundSize.getX(),
              backgroundSize.getY());
    }
    GlStateManager.popMatrix();

    this.renderFog();
    this.renderFooter();
  }

  private void renderFooter() {
    final String footer = CraftingDead.DISPLAY_NAME + " " + CraftingDead.VERSION;
    GlStateManager.pushMatrix();
    {
      GlStateManager.translated(this.width / 2.0D, this.height - 5.0D, 0.0D);
      GlStateManager.scaled(0.5D, 0.5D, 0.5D);
      this.drawCenteredString(this.font, footer, 0, 0, 0xAAAAAA);
    }
    GlStateManager.popMatrix();
  }

  private void renderFog() {
    Vector2d fogSize = RenderUtil.scaleToFit(1920, 1080);

    final double pct =
        MathHelper.clamp((Util.milliTime() - fogStartTime) / (1000.0D * 100.0D * 2.0D), 0.0D, 1.0D);
    if (pct == 1.0D) {
      fogStartTime = Util.milliTime();
    }

    GlStateManager.pushMatrix();
    {
      GlStateManager.scalef(4F, 4F, 4F);
      GlStateManager.enableBlend();
      GlStateManager
          .blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
              GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
              GlStateManager.DestFactor.ZERO);
      GlStateManager.color4f(1.0F, 1.0F, 1.0F, 0.3F);

      RenderUtil.bind(SMOKE_TEXTURE);

      final double smokeX = pct * this.width;

      RenderUtil.drawTexturedRectangle(smokeX, 0, fogSize.getX(), fogSize.getY());
      RenderUtil.drawTexturedRectangle(smokeX - fogSize.getX(), 0, fogSize.getX(), fogSize.getY());

      GlStateManager.disableBlend();
    }
    GlStateManager.popMatrix();
  }

  public ITransition getTransition() {
    return Transitions.GROW;
  }
}
