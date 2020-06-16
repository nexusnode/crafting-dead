package com.craftingdead.core.capability.scope;

import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IScope {

  boolean isAiming(Entity entity, ItemStack itemStack);

  float getFovModifier(Entity entity, ItemStack itemStack);

  Optional<ResourceLocation> getOverlayTexture(Entity entity, ItemStack itemStack);
  
  int getOverlayTextureWidth();
  
  int getOverlayTextureHeight();
}
