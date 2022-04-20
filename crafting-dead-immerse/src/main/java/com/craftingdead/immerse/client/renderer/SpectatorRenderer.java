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

package com.craftingdead.immerse.client.renderer;

import com.craftingdead.immerse.mixin.ItemInHandRendererAccessor;
import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraftforge.client.ForgeHooksClient;

public class SpectatorRenderer {

  private final Minecraft minecraft = Minecraft.getInstance();

  private float prevEquippedProgressMainHand;
  private float equippedProgressMainHand;

  private float prevEquippedProgressOffHand;
  private float equippedProgressOffHand;

  private ItemStack itemStackOffHand = ItemStack.EMPTY;
  private ItemStack itemStackMainHand = ItemStack.EMPTY;

  public void renderItemInFirstPerson(float partialTicks, PoseStack matrixStack,
      MultiBufferSource.BufferSource renderTypeBuffer, AbstractClientPlayer playerEntity,
      int packetLight) {

    float swingProgress = playerEntity.getAttackAnim(partialTicks);
    InteractionHand hand =
        MoreObjects.firstNonNull(playerEntity.swingingArm, InteractionHand.MAIN_HAND);
    float rotationPitch = Mth.lerp(partialTicks, playerEntity.xRotO,
        playerEntity.getXRot());

    ItemStack heldStack = playerEntity.getMainHandItem();
    ItemStack offStack = playerEntity.getOffhandItem();

    boolean renderRightHand = true;
    boolean renderLeftHand = true;
    if (playerEntity.isUsingItem()) {
      ItemStack itemstack = playerEntity.getUseItem();
      if (itemstack.getItem() instanceof ProjectileWeaponItem) {
        renderRightHand = playerEntity.getUsedItemHand() == InteractionHand.MAIN_HAND;
        renderLeftHand = !renderRightHand;
      }

      InteractionHand activeHand = playerEntity.getUsedItemHand();
      if (activeHand == InteractionHand.MAIN_HAND) {
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

    final ItemInHandRendererAccessor firstPersonRenderer =
        (ItemInHandRendererAccessor) this.minecraft.getItemInHandRenderer();

    if (renderRightHand) {
      float rightHandSwingProgress = hand == InteractionHand.MAIN_HAND ? swingProgress : 0.0F;
      float equippedProgress =
          1.0F - Mth.lerp(partialTicks, this.prevEquippedProgressMainHand,
              this.equippedProgressMainHand);
      if (!ForgeHooksClient.renderSpecificFirstPersonHand(InteractionHand.MAIN_HAND,
          matrixStack, renderTypeBuffer, packetLight, partialTicks, rotationPitch,
          rightHandSwingProgress, equippedProgress,
          this.itemStackMainHand))
        firstPersonRenderer
            .invokeRenderArmWithItem(playerEntity, partialTicks, rotationPitch,
                InteractionHand.MAIN_HAND, rightHandSwingProgress,
                this.itemStackMainHand, equippedProgress, matrixStack, renderTypeBuffer,
                packetLight);
    }

    if (renderLeftHand) {
      float leftHandSwingProgress = hand == InteractionHand.OFF_HAND ? swingProgress : 0.0F;
      float equippedProgress =
          1.0F - Mth.lerp(partialTicks, this.prevEquippedProgressOffHand,
              this.equippedProgressOffHand);
      if (!ForgeHooksClient.renderSpecificFirstPersonHand(InteractionHand.OFF_HAND,
          matrixStack, renderTypeBuffer, packetLight, partialTicks, rotationPitch,
          leftHandSwingProgress, equippedProgress,
          this.itemStackOffHand))
        firstPersonRenderer.invokeRenderArmWithItem(playerEntity, partialTicks,
            rotationPitch,
            InteractionHand.OFF_HAND, leftHandSwingProgress,
            this.itemStackOffHand, equippedProgress, matrixStack, renderTypeBuffer, packetLight);
    }

    renderTypeBuffer.endBatch();
  }

  public void tick(AbstractClientPlayer playerEntity) {
    this.prevEquippedProgressMainHand = this.equippedProgressMainHand;
    this.prevEquippedProgressOffHand = this.equippedProgressOffHand;

    ItemStack heldStack = playerEntity.getMainHandItem();
    ItemStack offStack = playerEntity.getOffhandItem();

    if (ItemStack.matches(this.itemStackMainHand, heldStack)) {
      this.itemStackMainHand = heldStack;
    }

    if (ItemStack.matches(this.itemStackOffHand, offStack)) {
      this.itemStackOffHand = offStack;
    }


    float cooledAttackStrength = playerEntity.getAttackStrengthScale(1.0F);
    boolean requipMainHand = ForgeHooksClient.shouldCauseReequipAnimation(
        this.itemStackMainHand, heldStack, playerEntity.getInventory().selected);
    boolean requipOffHand = ForgeHooksClient
        .shouldCauseReequipAnimation(this.itemStackOffHand, offStack, -1);

    if (!requipMainHand && this.itemStackMainHand != heldStack) {
      this.itemStackMainHand = heldStack;
    }

    if (!requipOffHand && this.itemStackOffHand != offStack) {
      this.itemStackOffHand = offStack;
    }

    this.equippedProgressMainHand += Mth
        .clamp((!requipMainHand ? cooledAttackStrength * cooledAttackStrength * cooledAttackStrength
            : 0.0F) - this.equippedProgressMainHand, -0.4F, 0.4F);
    this.equippedProgressOffHand +=
        Mth.clamp((!requipOffHand ? 1 : 0) - this.equippedProgressOffHand, -0.4F, 0.4F);


    if (this.equippedProgressMainHand < 0.1F) {
      this.itemStackMainHand = heldStack;
    }

    if (this.equippedProgressOffHand < 0.1F) {
      this.itemStackOffHand = offStack;
    }
  }
}
