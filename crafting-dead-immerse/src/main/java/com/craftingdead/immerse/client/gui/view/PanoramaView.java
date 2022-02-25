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

import java.util.Objects;
import com.craftingdead.core.client.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;

public class PanoramaView extends View {

  private final PanoramaRenderer panorama;

  public PanoramaView(Properties<?> properties, CubeMap cubeMap) {
    super(properties);
    Objects.requireNonNull(cubeMap, "cubeMap cannot be null");
    this.panorama = new PanoramaRenderer(cubeMap);
  }

  @Override
  public void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    super.renderContent(poseStack, mouseX, mouseY, partialTick);
    if (this.minecraft.level == null) {
      this.panorama.render(partialTick, this.getAlpha());
    } else {
      RenderUtil.fillGradient(poseStack, 0, 0, this.getScaledContentWidth(),
          this.getScaledContentHeight(), 0xA0101010, 0xB0101010);
    }
  }
}
