package com.craftingdead.core.world.entity.extension;

import net.minecraft.world.entity.LivingEntity;

public sealed interface BasicLivingExtension
    extends LivingExtension<LivingEntity, LivingHandler> permits BasicLivingExtensionImpl {

  static BasicLivingExtension create(LivingEntity entity) {
    return new BasicLivingExtensionImpl(entity);
  }
}
