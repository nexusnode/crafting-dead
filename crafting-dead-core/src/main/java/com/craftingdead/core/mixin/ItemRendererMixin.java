package com.craftingdead.core.mixin;

import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.rendererprovider.IRendererProvider;
import com.craftingdead.core.client.renderer.item.IItemRenderer;
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

  @Inject(at = @At("HEAD"),
      method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IILnet/minecraft/client/renderer/model/IBakedModel;)V",
      cancellable = true)
  private void renderItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType,
      boolean leftHanded, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedLight, int packedOverlay, IBakedModel bakedModel, CallbackInfo callbackInfo) {
    if (this.renderItem(null, itemStack, transformType, leftHanded,
        matrixStack, renderTypeBuffer, packedLight, packedOverlay)) {
      callbackInfo.cancel();
    }
  }

  @Inject(at = @At("HEAD"),
      method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;Lnet/minecraft/world/World;II)V",
      cancellable = true)
  private void renderItem(@Nullable LivingEntity livingEntity, ItemStack itemStack,
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
    IItemRenderer itemRenderer = itemStack.getCapability(ModCapabilities.RENDERER_PROVIDER)
        .map(IRendererProvider::getItemRenderer)
        .filter(renderer -> renderer.handleRenderType(itemStack, transformType)).orElse(null);
    if (itemRenderer == null) {
      return false;
    }
    itemRenderer.renderItem(itemStack, transformType, livingEntity, matrixStack, renderTypeBuffer,
        packedLight, packedOverlay);
    return true;
  }
}
