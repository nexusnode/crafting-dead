/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.client.gui.SimpleButton;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.inventory.container.PlayerContainer;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.OpenStorageMessage;
import com.craftingdead.core.util.Text;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PlayerScreen extends DisplayEffectsScreen<PlayerContainer> {

  private static final ResourceLocation CONTAINER_BACKGROUND =
      new ResourceLocation(CraftingDead.ID, "textures/gui/container/inventory.png");

  private int oldMouseX;
  private int oldMouseY;

  private Button vestButton;

  private boolean transitioning = false;

  public PlayerScreen(PlayerContainer container, PlayerInventory playerInventory,
      ITextComponent title) {
    super(container, playerInventory, title);
  }

  @Override
  public void init() {
    super.init();
    this.vestButton =
        new SimpleButton(this.guiLeft + 98, this.guiTop + 61, 10, 17, Text.of(">"), (button) -> {
          NetworkChannel.PLAY.getSimpleChannel()
              .sendToServer(new OpenStorageMessage(InventorySlotType.VEST));
          this.transitioning = true;
        });
    this.addButton(this.vestButton);
    this.refreshButtonStatus();
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    this.oldMouseX = mouseX;
    this.oldMouseY = mouseY;
  }

  @Override
  public void tick() {
    super.tick();
    this.refreshButtonStatus();
  }

  private void refreshButtonStatus() {
    this.vestButton.active = this.container
        .getItemHandler()
        .getStackInSlot(InventorySlotType.VEST.getIndex())
        .getCapability(ModCapabilities.STORAGE)
        .isPresent();
  }

  /**
   * If we are waiting for another container GUI to open.
   */
  public boolean isTransitioning() {
    return this.transitioning;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {}

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks,
      int mouseX, int mouseY) {
    this.renderBackground(matrixStack);
    this.minecraft.getTextureManager().bindTexture(CONTAINER_BACKGROUND);

    this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

    ItemStack gunStack = this.container.getGunStack();
    gunStack.getCapability(ModCapabilities.GUN).ifPresent(gunController -> {

      final int gunSlotX = this.guiLeft + 122;
      final int gunSlotY = this.guiTop + 26;

      this.blit(matrixStack, gunSlotX, gunSlotY, 176, 0, 22, 22);

      final boolean draggingItemAccepted =
          gunController.isAcceptedPaintOrAttachment(this.playerInventory.getItemStack());

      if ((!this.container.isCraftingInventoryEmpty() && this.container.isCraftable())
          || draggingItemAccepted) {
        this.blit(matrixStack, gunSlotX, gunSlotY, 176, 22, 22, 22);
      } else if (!this.playerInventory.getItemStack().isEmpty() && !draggingItemAccepted) {
        this.blit(matrixStack, gunSlotX, gunSlotY, 176, 44, 22, 22);
      }
    });

    InventoryScreen.drawEntityOnScreen(this.guiLeft + 33, this.guiTop + 72, 30,
        (this.guiLeft + 51) - this.oldMouseX, (this.guiTop + 75 - 50) - this.oldMouseY,
        this.minecraft.player);
  }
}
