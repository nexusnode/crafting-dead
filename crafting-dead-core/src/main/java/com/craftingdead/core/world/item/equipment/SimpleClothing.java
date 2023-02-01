package com.craftingdead.core.world.item.equipment;

import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record SimpleClothing(
    Multimap<Attribute, AttributeModifier> attributeModifiers,
    boolean fireImmunity,
    boolean enhancesSwimming,
    ResourceLocation texture) implements Clothing {

  @Override
  public ResourceLocation getTexture(String skinType) {
    return this.texture;
  }
}
