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

package com.craftingdead.immerse.client.gui;

import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

public class KilledMessage {

  private final AbstractClientPlayer killerEntity;
  private final ItemStack itemStack;

  public KilledMessage(AbstractClientPlayer killerEntity, ItemStack itemStack) {
    this.killerEntity = killerEntity;
    this.itemStack = itemStack;
  }

  public void render(PoseStack poseStack, Font font, int width, int height) {
    int boxWidth = 200;
    int boxContentsHeight = 80;
    int boxTitleHeight = 20;

    int x = width / 2 - (boxWidth / 2);
    int y = height / 2 + 10;

    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fillWithShadow(poseStack, x, y, boxWidth, boxTitleHeight, 0xDD1c1c1c);

    poseStack.pushPose();
    {
      poseStack.translate(x + 5, y + 5, 0);
      poseStack.scale(1.5F, 1.5F, 1.5F);
      font.drawShadow(poseStack,
          new TranslatableComponent("gui.killed_message.killed_by",
              this.killerEntity.getDisplayName()).withStyle(ChatFormatting.DARK_RED),
          0, 0, 0);
    }
    poseStack.popPose();

    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fillWithShadow(poseStack, x, y + boxTitleHeight, boxWidth,
        boxContentsHeight - boxTitleHeight, 0xDD000000);

    poseStack.pushPose();
    {
      poseStack.translate(x + 5, y + 24, 0);
      poseStack.scale(1.5F, 1.5F, 1.5F);
      font.drawShadow(poseStack,
          new TranslatableComponent("gui.killed_message.hp", this.killerEntity.getHealth()), 0,
          0, 0xFFFFFFFF);
    }
    poseStack.popPose();

    RenderUtil.blitAvatar(
        this.killerEntity.getSkinTextureLocation(), poseStack, x + 5, y + 40, 35, 35);

    font.drawShadow(
        poseStack, this.itemStack.getDisplayName(), x + 80, y + 30, 0xFFFFFFFF);

    final var modelViewStack = RenderSystem.getModelViewStack();
    modelViewStack.pushPose();
    {
      modelViewStack.translate(x + 110, y + 50, 0);
      var scale = 1.2F;
      modelViewStack.scale(scale, scale, scale);
      com.craftingdead.core.client.util.RenderUtil.renderGuiItem(this.itemStack, 0, 0,
          0xFFFFFFFF, ItemTransforms.TransformType.FIXED);
    }
    modelViewStack.popPose();
  }
}
