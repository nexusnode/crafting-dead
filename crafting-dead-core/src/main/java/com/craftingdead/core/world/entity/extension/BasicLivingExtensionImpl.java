package com.craftingdead.core.world.entity.extension;

import net.minecraft.world.entity.LivingEntity;

final class BasicLivingExtensionImpl extends BaseLivingExtension<LivingEntity, LivingHandler>
    implements BasicLivingExtension {

  BasicLivingExtensionImpl(LivingEntity entity) {
    super(entity);
  }
}
