package com.craftingdead.immerse.client.gui.screen;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import sm0keysa1m0n.bliss.ObjectFit;
import sm0keysa1m0n.bliss.view.View;

public class FogView extends View {

  private static final ResourceLocation SMOKE_TEXTURE =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/smoke.png");

  private static long fogStartTime = Util.getMillis();

  public FogView(Properties properties) {
    super(properties);
  }

  @Override
  public void renderContent(PoseStack poseStack, int mouseX, int mouseY, float paritalTicks) {
    var fogSize = ObjectFit.COVER.getSize(1920, 1080, this.getScaledContentWidth(),
        this.getScaledContentHeight());
    float fogWidth = fogSize.x;
    float fogHeight = fogSize.y;

    final float pct =
        Mth.clamp((Util.getMillis() - fogStartTime) / (1000.0F * 100.0F * 2.0F), 0.0F, 1.0F);
    if (pct == 1.0F) {
      fogStartTime = Util.getMillis();
    }

    this.graphicsContext.end();
    poseStack.pushPose();
    {
      poseStack.scale(4F, 4F, 4F);
      RenderSystem.enableBlend();

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.3F * this.getAlpha());

      RenderSystem.setShaderTexture(0, SMOKE_TEXTURE);

      final float smokeX = this.getScaledX() + pct * this.getContentWidth();

      RenderUtil.blit(poseStack, smokeX, this.getScaledContentY(), fogWidth, fogHeight);
      RenderUtil.blit(poseStack, smokeX - fogWidth, this.getScaledContentY(), fogWidth,
          fogHeight);

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

      RenderSystem.disableBlend();
    }
    poseStack.popPose();
    this.graphicsContext.begin();
  }
}
