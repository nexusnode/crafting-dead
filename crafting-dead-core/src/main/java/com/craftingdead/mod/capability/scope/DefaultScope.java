package com.craftingdead.mod.capability.scope;

import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class DefaultScope implements IScope {

  private final float fovModifier;
  private final Optional<ResourceLocation> overlayTexture;
  private final int overlayTextureWidth;
  private final int overlayTextureHeight;

  public DefaultScope() {
    this(1.0F, null, 0, 0);
  }

  public DefaultScope(float fovModifier, ResourceLocation overlayTexture, int overlayTextureWidth,
      int overlayTextureHeight) {
    this.fovModifier = fovModifier;
    this.overlayTexture = Optional.ofNullable(overlayTexture);
    this.overlayTextureWidth = overlayTextureWidth;
    this.overlayTextureHeight = overlayTextureHeight;
  }

  @Override
  public boolean isAiming(Entity entity, ItemStack itemStack) {
    return entity instanceof LivingEntity
        ? ((LivingEntity) entity).getActiveItemStack() == itemStack
        : false;
  }

  @Override
  public float getFovModifier(Entity entity, ItemStack itemStack) {
    return this.fovModifier;
  }

  @Override
  public Optional<ResourceLocation> getOverlayTexture(Entity entity, ItemStack itemStack) {
    return this.overlayTexture;
  }

  @Override
  public int getOverlayTextureWidth() {
    return this.overlayTextureWidth;
  }

  @Override
  public int getOverlayTextureHeight() {
    return this.overlayTextureHeight;
  }
}
