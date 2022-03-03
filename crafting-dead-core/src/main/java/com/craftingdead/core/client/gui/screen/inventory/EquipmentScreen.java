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

package com.craftingdead.core.client.gui.screen.inventory;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.gui.SimpleButton;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.OpenStorageMessage;
import com.craftingdead.core.world.inventory.EquipmentMenu;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.core.world.inventory.storage.Storage;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.skin.Paint;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class EquipmentScreen extends EffectRenderingInventoryScreen<EquipmentMenu> {

  private static final ResourceLocation BACKGROUND =
      new ResourceLocation(CraftingDead.ID, "textures/gui/container/equipment.png");

  private static final Component ARROW = new TextComponent(">");

  private int oldMouseX;
  private int oldMouseY;

  private Button backpackButton;
  private Button vestButton;

  private boolean transitioning = false;

  public EquipmentScreen(EquipmentMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  public void init() {
    super.init();
    this.backpackButton =
        new SimpleButton(this.leftPos + 98, this.topPos + 7, 10, 17, ARROW, (button) -> {
          NetworkChannel.PLAY.getSimpleChannel()
              .sendToServer(new OpenStorageMessage(ModEquipmentSlot.BACKPACK));
          this.transitioning = true;
        });
    this.addRenderableWidget(this.backpackButton);
    this.vestButton =
        new SimpleButton(this.leftPos + 98, this.topPos + 61, 10, 17, ARROW, (button) -> {
          NetworkChannel.PLAY.getSimpleChannel()
              .sendToServer(new OpenStorageMessage(ModEquipmentSlot.VEST));
          this.transitioning = true;
        });
    this.addRenderableWidget(this.vestButton);
    this.refreshButtonStatus();
  }

  @Override
  public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderTooltip(matrixStack, mouseX, mouseY);
    this.oldMouseX = mouseX;
    this.oldMouseY = mouseY;
  }

  @Override
  protected void containerTick() {
    super.containerTick();
    this.refreshButtonStatus();
  }

  private void refreshButtonStatus() {
    this.backpackButton.active = this.menu
        .getItemHandler()
        .getStackInSlot(ModEquipmentSlot.BACKPACK.getIndex())
        .getCapability(Storage.CAPABILITY)
        .isPresent();
    this.vestButton.active = this.menu
        .getItemHandler()
        .getStackInSlot(ModEquipmentSlot.VEST.getIndex())
        .getCapability(Storage.CAPABILITY)
        .isPresent();
  }

  /**
   * If we are waiting for another container GUI to open.
   */
  public boolean isTransitioning() {
    return this.transitioning;
  }

  @Override
  protected void renderLabels(PoseStack matrixStack, int x, int y) {}

  @Override
  protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    this.renderBackground(matrixStack);
    RenderSystem.setShaderTexture(0, BACKGROUND);

    this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

    ItemStack gunStack = this.menu.getGunStack();
    gunStack.getCapability(Gun.CAPABILITY).ifPresent(gun -> {

      final int gunSlotX = this.leftPos + 124;
      final int gunSlotY = this.topPos + 32;

      this.blit(matrixStack, gunSlotX, gunSlotY, 176, 0, 22, 22);

      final boolean carriedItemAccepted = gun.isAcceptedAttachment(this.menu.getCarried())
          || Paint.isValid(this.menu.getGunStack(), this.menu.getCarried());

      if ((!this.menu.isCraftingInventoryEmpty() && this.menu.isCraftable())
          || (!this.menu.getCarried().isEmpty() && carriedItemAccepted)) {
        // Green outline
        this.blit(matrixStack, gunSlotX, gunSlotY, 176, 22, 22, 22);
      } else if (!this.menu.getCarried().isEmpty() && !carriedItemAccepted) {
        // Red outline
        this.blit(matrixStack, gunSlotX, gunSlotY, 176, 44, 22, 22);
      }
    });

    InventoryScreen.renderEntityInInventory(this.leftPos + 33, this.topPos + 72, 30,
        (this.leftPos + 51) - this.oldMouseX, (this.topPos + 75 - 50) - this.oldMouseY,
        this.minecraft.player);
  }
}
