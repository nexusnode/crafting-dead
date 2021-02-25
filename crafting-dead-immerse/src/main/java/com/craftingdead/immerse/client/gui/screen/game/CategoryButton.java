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

package com.craftingdead.immerse.client.gui.screen.game;

import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.immerse.game.shop.IShopCategory;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

public class CategoryButton extends GameButton implements IInfoPanel {

  private final ITextComponent info;

  public CategoryButton(AbstractShopScreen screen, IPlayer<?> player, IShopCategory category) {
    super(0, 0, 0, 0, category.getDisplayName(),
        btn -> Minecraft.getInstance()
            .displayGuiScreen(new CategoryScreen(screen, player, category)));
    this.info = category.getInfo();
  }

  @Override
  public void renderInfo(int x, int y, MatrixStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    this.fontRenderer.func_243246_a(matrixStack, this.getMessage(), x - 20, y - 65, 0xFFFFFFFF);
    this.fontRenderer.func_238418_a_(this.info, x - 20, y - 45, 90, 0xFFFFFFFF);
  }
}
