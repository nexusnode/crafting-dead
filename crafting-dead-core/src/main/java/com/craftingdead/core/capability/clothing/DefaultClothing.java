package com.craftingdead.core.capability.clothing;

import java.util.Optional;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class DefaultClothing implements IClothing {

  private final boolean enhancedProtection;
  private final Optional<Integer> slownessAmplifier;
  private final boolean fireImmunity;
  private final ResourceLocation texture;

  public DefaultClothing() {
    this(false, Optional.empty(), false, TextureManager.RESOURCE_LOCATION_EMPTY);
  }

  public DefaultClothing(boolean enhancedProtection, Optional<Integer> slownessAmplifier,
      boolean fireImmunity, ResourceLocation texture) {
    this.enhancedProtection = enhancedProtection;
    this.slownessAmplifier = slownessAmplifier;
    this.fireImmunity = fireImmunity;
    this.texture = texture;
  }

  @Override
  public boolean hasEnhancedProtection() {
    return this.enhancedProtection;
  }

  @Override
  public Optional<Integer> getSlownessAmplifier() {
    return this.slownessAmplifier;
  }

  @Override
  public boolean hasFireImmunity() {
    return this.fireImmunity;
  }

  @Override
  public ResourceLocation getTexture(String skinType) {
    return this.texture;
  }
}
