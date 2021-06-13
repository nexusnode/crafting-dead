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
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

  /**
   * Adds hook for {@link CustomItemRenderer}.
   */
  @Inject(at = @At("HEAD"),
      method = "render(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IILnet/minecraft/client/renderer/model/IBakedModel;)V",
      cancellable = true)
  private void render(ItemStack itemStack, ItemCameraTransforms.TransformType transformType,
      boolean leftHanded, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedLight, int packedOverlay, IBakedModel bakedModel, CallbackInfo callbackInfo) {
    if (this.renderItem(null, itemStack, transformType, leftHanded,
        matrixStack, renderTypeBuffer, packedLight, packedOverlay)) {
      callbackInfo.cancel();
    }
  }

  /**
   * Adds hook for {@link CustomItemRenderer}.
   */
  @Inject(at = @At("HEAD"),
      method = "renderStatic(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;Lnet/minecraft/world/World;II)V",
      cancellable = true)
  private void renderStatic(@Nullable LivingEntity livingEntity, ItemStack itemStack,
      ItemCameraTransforms.TransformType transformType, boolean leftHanded, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, @Nullable World world, int packedLight, int packedOverlay,
      CallbackInfo callbackInfo) {
    if (this.renderItem(livingEntity, itemStack, transformType, leftHanded,
        matrixStack, renderTypeBuffer, packedLight, packedOverlay)) {
      callbackInfo.cancel();
    }
  }

  private boolean renderItem(@Nullable LivingEntity livingEntity,
      ItemStack itemStack,
      ItemCameraTransforms.TransformType transformType, boolean leftHanded, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    CustomItemRenderer itemRenderer =
        CraftingDead.getInstance().getClientDist().getItemRendererManager()
            .getItemRenderer(itemStack.getItem());
    if (itemRenderer != null && itemRenderer.handleRenderType(itemStack, transformType)) {
      itemRenderer.renderItem(itemStack, transformType, livingEntity, matrixStack,
          renderTypeBuffer, packedLight, packedOverlay);
      return true;
    }
    return false;
  }
}
