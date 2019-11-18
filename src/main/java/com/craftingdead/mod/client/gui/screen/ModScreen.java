package com.craftingdead.mod.client.gui.screen;

import javax.vecmath.Vector2d;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.gui.transition.ITransition;
import com.craftingdead.mod.client.gui.transition.Transitions;
import com.craftingdead.mod.client.util.RenderUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public abstract class ModScreen extends Screen {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      new ResourceLocation(CraftingDead.ID, "textures/gui/background.png");

  private static final ResourceLocation SMOKE_TEXTURE =
      new ResourceLocation(CraftingDead.ID, "textures/gui/smoke.png");

  private static double smokeX = 0;
  private static double lastSmokeX;

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

    Vector2d backgroundSize = RenderUtil.scaleToFit(2048, 1152);
    RenderUtil.bind(BACKGROUND_TEXTURE);
    RenderUtil.drawTexturedRectangle(0, 0, backgroundSize.getX(), backgroundSize.getY());

    this.renderSmoke();
  }

  protected void renderSmoke() {
    if (smokeX < this.width) {
      lastSmokeX = smokeX;
      smokeX += 0.0125;
    } else {
      lastSmokeX = 0;
      smokeX = 0;
    }

    Vector2d smokeSize = RenderUtil.scaleToFit(1920, 1080);

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

      double smoothSmokeX = MathHelper.lerp(this.minecraft.getTickLength(), lastSmokeX, smokeX);

      RenderUtil.drawTexturedRectangle(smoothSmokeX, 0, smokeSize.getX(), smokeSize.getY());
      RenderUtil
          .drawTexturedRectangle(smoothSmokeX - smokeSize.getX(), 0, smokeSize.getX(),
              smokeSize.getY());

      GlStateManager.disableBlend();
    }
    GlStateManager.popMatrix();
  }

  public ITransition getTransition() {
    return Transitions.GROW;
  }
}
