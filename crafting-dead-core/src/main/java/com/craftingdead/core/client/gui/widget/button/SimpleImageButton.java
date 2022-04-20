/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.gui.widget.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SimpleImageButton extends Button {

  private ResourceLocation textureLocation;

  public SimpleImageButton(int x, int y, int width, int height,
      ResourceLocation textureLocation, Component text, Button.OnPress actionListener) {
    super(x, y, width, height, text, actionListener);
    this.textureLocation = textureLocation;
  }

  @Override
  public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
    RenderSystem.setShaderTexture(0, this.textureLocation);
    blit(matrixStack, this.x, this.y, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
    if (this.isHoveredOrFocused()) {
      final int opacity = Math.min((int) (this.alpha * 0.5F * 255.0F), 255);
      fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height,
          0xFFFFFF + (opacity << 24));
    }
  }
}
