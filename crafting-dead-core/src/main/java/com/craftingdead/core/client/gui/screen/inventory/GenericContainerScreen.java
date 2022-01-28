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

package com.craftingdead.core.client.gui.screen.inventory;

import com.craftingdead.core.world.inventory.AbstractMenu;
import com.craftingdead.core.world.inventory.GenericMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class GenericContainerScreen extends AbstractContainerScreen<GenericMenu> {

  private static final ResourceLocation GENERIC_CONTAINER_TEXTURE =
      new ResourceLocation("textures/gui/container/generic_54.png");

  private static final int TITLE_TEXT_COLOUR = 0x404040;

  public GenericContainerScreen(GenericMenu menu, Inventory playerInventory,
      Component title) {
    super(menu, playerInventory, title);
    this.passEvents = false;
    this.imageHeight = 114 + this.menu.getRows() * AbstractMenu.SLOT_SIZE;
  }

  @Override
  public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderTooltip(matrixStack, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
    this.font.draw(poseStack, this.title, 8.0F, 6.0F, 4210752);
    this.font.draw(poseStack, this.playerInventoryTitle, 8.0F,
        this.imageHeight - 96 + 2, TITLE_TEXT_COLOUR);
  }

  @Override
  protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
    this.renderBackground(poseStack);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, GENERIC_CONTAINER_TEXTURE);
    int x = (this.width - this.imageWidth) / 2;
    int y = (this.height - this.imageHeight) / 2;
    this.blit(poseStack, x, y, 0, 0, this.imageWidth,
        this.menu.getRows() * AbstractMenu.SLOT_SIZE + AbstractMenu.SLOT_SIZE - 1);
    this.blit(poseStack, x,
        y + (this.menu.getRows() * AbstractMenu.SLOT_SIZE) + AbstractMenu.SLOT_SIZE - 1, 0,
        7 * AbstractMenu.SLOT_SIZE, this.imageWidth, 96);
  }
}
