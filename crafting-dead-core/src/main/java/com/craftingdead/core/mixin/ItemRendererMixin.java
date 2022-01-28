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

package com.craftingdead.core.mixin;

import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.renderer.item.CustomItemRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
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
      MultiBufferSource renderTypeBuffer, @Nullable Level world, int packedLight, int packedOverlay, int value,
      CallbackInfo callbackInfo) {
    if (CraftingDead.getInstance().getClientDist().getItemRendererManager().renderItem(itemStack,
        transformType, livingEntity, matrixStack, renderTypeBuffer, packedLight, packedOverlay)) {
      callbackInfo.cancel();
    }
  }
}
