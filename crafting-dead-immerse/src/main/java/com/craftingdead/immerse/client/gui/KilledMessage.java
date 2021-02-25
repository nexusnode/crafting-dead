/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.client.gui;

import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class KilledMessage {

  private final AbstractClientPlayerEntity killerEntity;
  private final ItemStack itemStack;

  public KilledMessage(AbstractClientPlayerEntity killerEntity, ItemStack itemStack) {
    this.killerEntity = killerEntity;
    this.itemStack = itemStack;
  }

  @SuppressWarnings("deprecation")
  public void render(MatrixStack matrixStack, FontRenderer fontRenderer, int width, int height) {
    int boxWidth = 200;
    int boxContentsHeight = 80;
    int boxTitleHeight = 20;

    int x = width / 2 - (boxWidth / 2);
    int y = height / 2 + 10;

    RenderUtil.fillWithShadow(x, y, boxWidth, boxTitleHeight, 0xDD1c1c1c);

    matrixStack.push();
    matrixStack.translate(x + 5, y + 5, 0);
    matrixStack.scale(1.5F, 1.5F, 1.5F);
    fontRenderer.func_243246_a(matrixStack,
        Text.translate("gui.killed_message.killed_by", this.killerEntity.getDisplayName())
            .mergeStyle(TextFormatting.DARK_RED),
        0, 0, 0);
    matrixStack.pop();

    RenderUtil.fillWithShadow(x, y + boxTitleHeight, boxWidth, boxContentsHeight - boxTitleHeight,
        0xDD000000);

    matrixStack.push();
    matrixStack.translate(x + 5, y + 24, 0);
    matrixStack.scale(1.5F, 1.5F, 1.5F);
    fontRenderer.func_243246_a(matrixStack,
        Text.translate("gui.killed_message.hp", this.killerEntity.getHealth()), 0, 0,
        0xFFFFFFFF);
    matrixStack.pop();

    RenderUtil.renderHead(this.killerEntity.getLocationSkin(), matrixStack, x + 5, y + 40, 35, 35);

    fontRenderer.func_243246_a(matrixStack, this.itemStack.getDisplayName(), x + 80, y + 30,
        0xFFFFFFFF);

    RenderSystem.pushMatrix();
    RenderSystem.translatef(x + 110, y + 50, 0);
    double scale = 1.2D;
    RenderSystem.scaled(scale, scale, scale);
    com.craftingdead.core.client.util.RenderUtil.renderItemIntoGUI(this.itemStack, 0, 0,
        0xFFFFFFFF, true);
    RenderSystem.popMatrix();
  }
}
