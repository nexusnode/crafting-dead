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

package com.craftingdead.immerse.client.renderer;

import com.craftingdead.immerse.mixin.FirstPersonRendererAccessor;
import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;

public class SpectatorRenderer {

  private final Minecraft minecraft = Minecraft.getInstance();

  private float prevEquippedProgressMainHand;
  private float equippedProgressMainHand;

  private float prevEquippedProgressOffHand;
  private float equippedProgressOffHand;

  private ItemStack itemStackOffHand = ItemStack.EMPTY;
  private ItemStack itemStackMainHand = ItemStack.EMPTY;

  public void renderItemInFirstPerson(float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer.Impl renderTypeBuffer, AbstractClientPlayerEntity playerEntity,
      int packetLight) {

    float swingProgress = playerEntity.getSwingProgress(partialTicks);
    Hand hand = MoreObjects.firstNonNull(playerEntity.swingingHand, Hand.MAIN_HAND);
    float rotationPitch = MathHelper.lerp(partialTicks, playerEntity.prevRotationPitch,
        playerEntity.rotationPitch);

    ItemStack heldStack = playerEntity.getHeldItemMainhand();
    ItemStack offStack = playerEntity.getHeldItemOffhand();

    boolean renderRightHand = true;
    boolean renderLeftHand = true;
    if (playerEntity.isHandActive()) {
      ItemStack itemstack = playerEntity.getActiveItemStack();
      if (itemstack.getItem() instanceof ShootableItem) {
        renderRightHand = playerEntity.getActiveHand() == Hand.MAIN_HAND;
        renderLeftHand = !renderRightHand;
      }

      Hand activeHand = playerEntity.getActiveHand();
      if (activeHand == Hand.MAIN_HAND) {
        if (offStack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(offStack)) {
          renderLeftHand = false;
        }
      }
    } else {
      if (heldStack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(heldStack)) {
        renderLeftHand = !renderRightHand;
      }

      if (offStack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(offStack)) {
        renderRightHand = !heldStack.isEmpty();
        renderLeftHand = !renderRightHand;
      }
    }

    final FirstPersonRendererAccessor firstPersonRenderer =
        (FirstPersonRendererAccessor) this.minecraft.getFirstPersonRenderer();

    if (renderRightHand) {
      float rightHandSwingProgress = hand == Hand.MAIN_HAND ? swingProgress : 0.0F;
      float equippedProgress =
          1.0F - MathHelper.lerp(partialTicks, this.prevEquippedProgressMainHand,
              this.equippedProgressMainHand);
      if (!ForgeHooksClient.renderSpecificFirstPersonHand(Hand.MAIN_HAND,
          matrixStack, renderTypeBuffer, packetLight, partialTicks, rotationPitch,
          rightHandSwingProgress, equippedProgress,
          this.itemStackMainHand))
        firstPersonRenderer
            .invokeRenderItemInFirstPerson(playerEntity, partialTicks, rotationPitch,
                Hand.MAIN_HAND, rightHandSwingProgress,
                this.itemStackMainHand, equippedProgress, matrixStack, renderTypeBuffer,
                packetLight);
    }

    if (renderLeftHand) {
      float leftHandSwingProgress = hand == Hand.OFF_HAND ? swingProgress : 0.0F;
      float equippedProgress =
          1.0F - MathHelper.lerp(partialTicks, this.prevEquippedProgressOffHand,
              this.equippedProgressOffHand);
      if (!ForgeHooksClient.renderSpecificFirstPersonHand(Hand.OFF_HAND,
          matrixStack, renderTypeBuffer, packetLight, partialTicks, rotationPitch,
          leftHandSwingProgress, equippedProgress,
          this.itemStackOffHand))
        firstPersonRenderer.invokeRenderItemInFirstPerson(playerEntity, partialTicks,
            rotationPitch,
            Hand.OFF_HAND, leftHandSwingProgress,
            this.itemStackOffHand, equippedProgress, matrixStack, renderTypeBuffer, packetLight);
    }

    renderTypeBuffer.finish();
  }

  public void tick(AbstractClientPlayerEntity playerEntity) {
    this.prevEquippedProgressMainHand = this.equippedProgressMainHand;
    this.prevEquippedProgressOffHand = this.equippedProgressOffHand;

    ItemStack heldStack = playerEntity.getHeldItemMainhand();
    ItemStack offStack = playerEntity.getHeldItemOffhand();

    if (ItemStack.areItemStacksEqual(this.itemStackMainHand, heldStack)) {
      this.itemStackMainHand = heldStack;
    }

    if (ItemStack.areItemStacksEqual(this.itemStackOffHand, offStack)) {
      this.itemStackOffHand = offStack;
    }


    float cooledAttackStrength = playerEntity.getCooledAttackStrength(1.0F);
    boolean requipMainHand = ForgeHooksClient.shouldCauseReequipAnimation(
        this.itemStackMainHand, heldStack, playerEntity.inventory.currentItem);
    boolean requipOffHand = ForgeHooksClient
        .shouldCauseReequipAnimation(this.itemStackOffHand, offStack, -1);

    if (!requipMainHand && this.itemStackMainHand != heldStack) {
      this.itemStackMainHand = heldStack;
    }

    if (!requipOffHand && this.itemStackOffHand != offStack) {
      this.itemStackOffHand = offStack;
    }

    this.equippedProgressMainHand += MathHelper
        .clamp((!requipMainHand ? cooledAttackStrength * cooledAttackStrength * cooledAttackStrength
            : 0.0F) - this.equippedProgressMainHand, -0.4F, 0.4F);
    this.equippedProgressOffHand +=
        MathHelper.clamp((!requipOffHand ? 1 : 0) - this.equippedProgressOffHand, -0.4F, 0.4F);


    if (this.equippedProgressMainHand < 0.1F) {
      this.itemStackMainHand = heldStack;
    }

    if (this.equippedProgressOffHand < 0.1F) {
      this.itemStackOffHand = offStack;
    }
  }
}
