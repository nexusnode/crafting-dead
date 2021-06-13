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

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.client.gui.SimpleButton;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.OpenStorageMessage;
import com.craftingdead.core.world.inventory.EquipmentMenu;
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class EquipmentScreen extends DisplayEffectsScreen<EquipmentMenu> {

  private static final ResourceLocation BACKGROUND =
      new ResourceLocation(CraftingDead.ID, "textures/gui/container/equipment.png");

  private static final ITextComponent ARROW = new StringTextComponent(">");

  private int oldMouseX;
  private int oldMouseY;

  private Button vestButton;

  private boolean transitioning = false;

  public EquipmentScreen(EquipmentMenu menu, PlayerInventory playerInventory,
      ITextComponent title) {
    super(menu, playerInventory, title);
  }

  @Override
  public void init() {
    super.init();
    this.vestButton =
        new SimpleButton(this.leftPos + 98, this.topPos + 61, 10, 17, ARROW, (button) -> {
          NetworkChannel.PLAY.getSimpleChannel()
              .sendToServer(new OpenStorageMessage(ModEquipmentSlotType.VEST));
          this.transitioning = true;
        });
    this.addButton(this.vestButton);
    this.refreshButtonStatus();
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderTooltip(matrixStack, mouseX, mouseY);
    this.oldMouseX = mouseX;
    this.oldMouseY = mouseY;
  }

  @Override
  public void tick() {
    super.tick();
    this.refreshButtonStatus();
  }

  private void refreshButtonStatus() {
    this.vestButton.active = this.menu
        .getItemHandler()
        .getStackInSlot(ModEquipmentSlotType.VEST.getIndex())
        .getCapability(Capabilities.STORAGE)
        .isPresent();
  }

  /**
   * If we are waiting for another container GUI to open.
   */
  public boolean isTransitioning() {
    return this.transitioning;
  }

  @Override
  protected void renderLabels(MatrixStack matrixStack, int x, int y) {}

  @Override
  protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    this.renderBackground(matrixStack);
    this.minecraft.getTextureManager().bind(BACKGROUND);

    this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

    ItemStack gunStack = this.menu.getGunStack();
    gunStack.getCapability(Capabilities.GUN).ifPresent(gun -> {

      final int gunSlotX = this.leftPos + 122;
      final int gunSlotY = this.topPos + 26;

      this.blit(matrixStack, gunSlotX, gunSlotY, 176, 0, 22, 22);

      final boolean carriedItemAccepted =
          gun.isAcceptedPaintOrAttachment(this.inventory.getCarried());

      if ((!this.menu.isCraftingInventoryEmpty() && this.menu.isCraftable())
          || carriedItemAccepted) {
        // Green outline
        this.blit(matrixStack, gunSlotX, gunSlotY, 176, 22, 22, 22);
      } else if (!this.inventory.getCarried().isEmpty() && !carriedItemAccepted) {
        // Red outline
        this.blit(matrixStack, gunSlotX, gunSlotY, 176, 44, 22, 22);
      }
    });

    InventoryScreen.renderEntityInInventory(this.leftPos + 33, this.topPos + 72, 30,
        (this.leftPos + 51) - this.oldMouseX, (this.topPos + 75 - 50) - this.oldMouseY,
        this.minecraft.player);
  }
}
