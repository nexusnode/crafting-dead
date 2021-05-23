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

package com.craftingdead.immerse.client.gui.screen.game.shop;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.craftingdead.immerse.game.shop.Shop;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class ItemButton extends GameButton implements InfoPanel {

  private final FontRenderer font = Minecraft.getInstance().font;

  private final PlayerExtension<?> player;
  private final Shop shop;
  private final ItemStack itemStack;
  private final int price;

  public ItemButton(ItemStack itemStack, PlayerExtension<?> player, Shop shop, int price) {
    super(0, 0, 0, 0, itemStack.getDisplayName(), btn -> shop.buyItem(player, itemStack));
    this.player = player;
    this.shop = shop;
    this.itemStack = itemStack;
    this.price = price;
  }

  private ITextComponent getFormattedPrice() {
    return new StringTextComponent("$" + this.price)
        .withStyle(this.shop.canAfford(this.player, this.price)
            ? TextFormatting.GREEN
            : TextFormatting.RED);
  }

  @Override
  public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
    if (this.price > 0) {
      RenderUtil.renderTextRight(this.font, matrixStack, x + width - 2, y + 7,
          this.getFormattedPrice(), 0, true);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void renderInfo(int x, int y, MatrixStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    this.font.drawShadow(matrixStack, this.getMessage(), x - 20, y - 65, 0xFFFFFFFF);

    drawCenteredString(matrixStack, this.font, this.getFormattedPrice(), x + 53, y - 75, 0);

    RenderSystem.pushMatrix();
    RenderSystem.translatef(x + 10, y - 40, 0);
    double scale = 1.2D;
    RenderSystem.scaled(scale, scale, scale);
    com.craftingdead.core.client.util.RenderUtil.renderGuiItem(this.itemStack, 0, 0,
        0xFFFFFFFF, ItemCameraTransforms.TransformType.FIXED);
    RenderSystem.popMatrix();

    List<ITextComponent> itemInfo = new ArrayList<>();
    this.itemStack.getItem().appendHoverText(this.itemStack, null, itemInfo, TooltipFlags.NORMAL);

    for (int i = 0; i < itemInfo.size(); i++) {
      ITextComponent info = itemInfo.get(i);
      this.font.drawShadow(matrixStack, info, x - 20,
          y + (i * this.font.lineHeight + 1), 0xFFFFFFFF);
    }
  }
}
