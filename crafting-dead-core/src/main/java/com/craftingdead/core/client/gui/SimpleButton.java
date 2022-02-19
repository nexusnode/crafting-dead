/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.core.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class SimpleButton extends Button {

  private int backgroundColour = 0x333333;

  public SimpleButton(int x, int y, int width, int height, Component text, OnPress action) {
    super(x, y, width, height, text, action);
    this.setFGColor(0xE3BE2B);
  }

  public void setBackgroundColour(int backgroundColour) {
    this.backgroundColour = backgroundColour;
  }

  @Override
  public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    final Minecraft minecraft = Minecraft.getInstance();
    fill(matrixStack, this.x, this.y, this.x + this.width + 1, this.y + this.height + 1,
        0x33000000);
    fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height,
        this.backgroundColour | Mth.ceil(this.alpha * 255.0F) << 24);
    drawCenteredString(matrixStack, minecraft.font, this.getMessage(),
        this.x + this.width / 2, this.y + (this.height - 8) / 2,
        (this.active ? this.isHovered ? this.packedFGColor : 0xFFFFFF : 0xA0A0A0)
            | Mth.ceil(this.alpha * 255.0F) << 24);
    // RenderSystem.enableBlend();
    // RenderSystem.defaultBlendFunc();
    // RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
    // GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
  }
}
