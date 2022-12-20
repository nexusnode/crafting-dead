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

package com.craftingdead.core.client.gui.screen.inventory;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.client.gui.widget.button.CompositeButton;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.OpenStorageMessage;
import com.craftingdead.core.world.inventory.EquipmentMenu;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.core.world.inventory.storage.Storage;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.skin.Paint;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class EquipmentScreen extends EffectRenderingInventoryScreen<EquipmentMenu> {


    private static final ResourceLocation BACKGROUND =
            new ResourceLocation(CraftingDead.ID, "textures/gui/container/equipment.png");

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
        this.vestButton = CompositeButton.button(this.leftPos + 95, this.topPos + 44, 12, 16,
                        BACKGROUND)
                .setAtlasPos(196, 224)
                .setHoverAtlasPos(196, 240)
                .setInactiveAtlasPos(183, 240)
                .setAction((button) -> {
                    NetworkChannel.PLAY.getSimpleChannel()
                            .sendToServer(new OpenStorageMessage(ModEquipmentSlot.VEST));
                    this.transitioning = true;
                }).build();
        this.addRenderableWidget(this.vestButton);
        this.backpackButton = CompositeButton.button(this.leftPos + 95, this.topPos + 62, 12, 16,
                        BACKGROUND)
                .setAtlasPos(196, 224)
                .setHoverAtlasPos(196, 240)
                .setInactiveAtlasPos(183, 240)
                .setAction((button) -> {
                    NetworkChannel.PLAY.getSimpleChannel()
                            .sendToServer(new OpenStorageMessage(ModEquipmentSlot.BACKPACK));
                    this.transitioning = true;
                }).build();
        this.addRenderableWidget(this.backpackButton);
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
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.renderBackground(poseStack);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Hide the icon of equipment slots if they have an item.
        // Starting at index 35 to skip the player inventory/
        for (int i = 35; i < this.menu.slots.size(); i++) {
            var slot = this.menu.slots.get(i);
            if (slot.hasItem()) {
                this.blit(poseStack, slot.x + this.leftPos, slot.y + this.topPos, 8, 141, 16, 16);
            }
        }

        ItemStack gunStack = this.menu.getGunStack();
        gunStack.getCapability(Gun.CAPABILITY).ifPresent(gun -> {

            final int gunSlotX = this.leftPos + 135;
            final int gunSlotY = this.topPos + 26;

            final boolean carriedItemAccepted = gun.isAcceptedAttachment(this.menu.getCarried())
                    || Paint.isValid(this.menu.getGunStack(), this.menu.getCarried());

            if ((!this.menu.isCraftingInventoryEmpty() && this.menu.isCraftable())
                    || (!this.menu.getCarried().isEmpty() && carriedItemAccepted)) {
                // Green outline
                this.blit(poseStack, gunSlotX, gunSlotY, 165, 238, 16, 16);
            } else if (!this.menu.getCarried().isEmpty() && !carriedItemAccepted) {
                // Red outline
                this.blit(poseStack, gunSlotX, gunSlotY, 147, 238, 16, 16);
            }
        });

        InventoryScreen.renderEntityInInventory(this.leftPos + 51, this.topPos + 72, 30,
                (this.leftPos + 51) - this.oldMouseX, (this.topPos + 75 - 50) - this.oldMouseY,
                this.minecraft.player);
    }

    @Override
    public boolean keyPressed(int p_97765_, int p_97766_, int p_97767_) {
        InputConstants.Key mouseKey = InputConstants.getKey(p_97765_, p_97766_);
        if (ClientDist.OPEN_EQUIPMENT_MENU.isActiveAndMatches(mouseKey)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(p_97765_, p_97766_, p_97767_);
    }
}
