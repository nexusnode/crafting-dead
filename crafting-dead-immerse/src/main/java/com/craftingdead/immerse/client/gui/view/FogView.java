/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.util.FitType;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FogView extends View {

  private static final ResourceLocation SMOKE_TEXTURE =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/smoke.png");

  private static long fogStartTime = Util.getMillis();

  public FogView(Properties<?> properties) {
    super(properties);
  }

  @Override
  public void renderContent(PoseStack poseStack, int mouseX, int mouseY, float paritalTicks) {
    var fogSize = FitType.COVER.getSize(1920, 1080, this.getScaledContentWidth(),
        this.getScaledContentHeight());
    float fogWidth = fogSize.x;
    float fogHeight = fogSize.y;

    final float pct =
        Mth.clamp((Util.getMillis() - fogStartTime) / (1000.0F * 100.0F * 2.0F), 0.0F, 1.0F);
    if (pct == 1.0F) {
      fogStartTime = Util.getMillis();
    }

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
  }
}
