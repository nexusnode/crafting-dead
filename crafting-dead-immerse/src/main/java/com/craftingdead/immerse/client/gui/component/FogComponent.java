package com.craftingdead.immerse.client.gui.component;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.component.type.FitType;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;

public class FogComponent extends Component<FogComponent> {

  private static final ResourceLocation SMOKE_TEXTURE =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/smoke.png");

  private static long fogStartTime = Util.milliTime();

  @SuppressWarnings("deprecation")
  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float paritalTicks) {
    Vector2f fogSize =
        FitType.COVER.getSize(1920, 1080, this.getScaledContentWidth(),
            this.getScaledContentHeight());
    double fogWidth = fogSize.x;
    double fogHeight = fogSize.y;

    final double pct =
        MathHelper.clamp((Util.milliTime() - fogStartTime) / (1000.0D * 100.0D * 2.0D), 0.0D, 1.0D);
    if (pct == 1.0D) {
      fogStartTime = Util.milliTime();
    }

    RenderSystem.pushMatrix();
    {
      RenderSystem.scalef(4F, 4F, 4F);
      RenderSystem.enableBlend();
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.3F);

      RenderUtil.bind(SMOKE_TEXTURE);

      final double smokeX = this.getScaledX() + pct * this.getContentWidth();

      RenderUtil.blit(smokeX, this.getScaledContentY(), fogWidth, fogHeight);
      RenderUtil.blit(smokeX - fogWidth, this.getScaledContentY(), fogWidth, fogHeight);

      RenderSystem.disableBlend();
    }
    RenderSystem.popMatrix();
  }
}
