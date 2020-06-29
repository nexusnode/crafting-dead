package com.craftingdead.core.capability.clothing;

import java.util.Optional;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class DefaultClothing implements IClothing {

  private final int armorLevel;
  private final Optional<Integer> slownessAmplifier;
  private final boolean fireImmunity;
  private final ResourceLocation texture;

  public DefaultClothing() {
    this(0, Optional.empty(), false, TextureManager.RESOURCE_LOCATION_EMPTY);
  }

  public DefaultClothing(int armorLevel, Optional<Integer> slownessAmplifier, boolean fireImmunity,
      ResourceLocation texture) {
    this.armorLevel = armorLevel;
    this.slownessAmplifier = slownessAmplifier;
    this.fireImmunity = fireImmunity;
    this.texture = texture;
  }

  @Override
  public int getArmorLevel() {
    return this.armorLevel;
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
