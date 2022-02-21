/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.clothing;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.resources.ResourceLocation;

public class DefaultClothing implements Clothing {

  private final Multimap<Attribute, AttributeModifier> attributeModifiers;
  private final boolean fireImmunity;
  private final ResourceLocation texture;

  public DefaultClothing() {
    this(ImmutableMultimap.of(), false, TextureManager.INTENTIONAL_MISSING_TEXTURE);
  }

  public DefaultClothing(Multimap<Attribute, AttributeModifier> attributeModifiers,
      boolean fireImmunity, ResourceLocation texture) {
    this.attributeModifiers = attributeModifiers;
    this.fireImmunity = fireImmunity;
    this.texture = texture;
  }

  @Override
  public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
    return this.attributeModifiers;
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
