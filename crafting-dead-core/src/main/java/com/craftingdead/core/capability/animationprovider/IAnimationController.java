package com.craftingdead.core.capability.animationprovider;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IAnimationController {

  void tick(LivingEntity livingEntity, ItemStack itemStack);

  void applyTransforms(LivingEntity livingEntity, ItemStack itemStack, String part,
      ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack,
      float partialTicks);
}
