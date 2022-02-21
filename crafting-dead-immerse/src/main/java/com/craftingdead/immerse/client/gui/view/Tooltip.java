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

import com.craftingdead.immerse.client.gui.view.property.StyleableProperty;
import com.craftingdead.immerse.client.gui.view.property.StatefulProperty;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class Tooltip {

  private final Component text;
  private final StatefulProperty<Integer> opacity =
      StyleableProperty.create("alpha", Integer.class, 0);
  private final StatefulProperty<Integer> textOpacity =
      StyleableProperty.create("text-alpha", Integer.class, 0);

  public Tooltip(Component text) {
    this.text = text;
  }

  public void render(Font font, PoseStack poseStack, float x, float y) {
    final float width = 10.0F + font.width(this.text);
    final float height = 14;

    RenderSystem.enableBlend();
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fill(poseStack, x, y, x + width, y + height, 0x222222 | (this.opacity.get() << 24));

    int shiftedOpacity = this.textOpacity.get() << 24;
    if ((shiftedOpacity & 0xFC000000) != 0) {
      poseStack.pushPose();
      {
        poseStack.translate(0.0D, 0.0D, 400.0D);
        font.draw(poseStack, this.text,
            (x + (width - font.width(this.text)) / 2.0F), y + 3.5F,
            0xFFFFFF | shiftedOpacity);
      }
      poseStack.popPose();
    }
    RenderSystem.disableBlend();

  }

  public StatefulProperty<Integer> getOpacityProperty() {
    return this.opacity;
  }

  public StatefulProperty<Integer> getTextOpacityProperty() {
    return this.textOpacity;
  }
}
