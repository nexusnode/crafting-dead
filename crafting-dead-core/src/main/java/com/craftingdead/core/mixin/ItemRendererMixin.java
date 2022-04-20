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

package com.craftingdead.core.mixin;

import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.renderer.item.CustomItemRenderer;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

  /**
   * Adds hook for {@link CustomItemRenderer}.
   */
  @Inject(at = @At("HEAD"),
      method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
      cancellable = true)
  private void render(ItemStack itemStack, ItemTransforms.TransformType transformType,
      boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer,
      int packedLight, int packedOverlay, BakedModel bakedModel, CallbackInfo callbackInfo) {
    if (CraftingDead.getInstance().getClientDist().getItemRendererManager().renderItem(itemStack,
        transformType, null, matrixStack, renderTypeBuffer, packedLight, packedOverlay)) {
      callbackInfo.cancel();
    }
  }

  /**
   * Adds hook for {@link CustomItemRenderer}.
   */
  @Inject(at = @At("HEAD"),
      method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V",
      cancellable = true)
  private void renderStatic(@Nullable LivingEntity livingEntity, ItemStack itemStack,
      ItemTransforms.TransformType transformType, boolean leftHanded, PoseStack matrixStack,
      MultiBufferSource renderTypeBuffer, @Nullable Level world, int packedLight, int packedOverlay,
      int value,
      CallbackInfo callbackInfo) {

    final var living = livingEntity == null ? null
        : livingEntity.getCapability(LivingExtension.CAPABILITY).orElse(null);

    if (living instanceof PlayerExtension<?> player && player.isHandcuffed()
        || CraftingDead.getInstance().getClientDist().getItemRendererManager().renderItem(itemStack,
            transformType, living, matrixStack, renderTypeBuffer, packedLight, packedOverlay)) {
      callbackInfo.cancel();
    }
  }
}
