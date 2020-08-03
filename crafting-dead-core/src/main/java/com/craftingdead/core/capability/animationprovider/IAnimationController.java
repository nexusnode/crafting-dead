package com.craftingdead.core.capability.animationprovider;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IAnimationController {

  void tick(LivingEntity livingEntity, ItemStack itemStack);

  void applyTransforms(LivingEntity livingEntity, ItemStack itemStack, String part,
      MatrixStack matrixStack);
}
