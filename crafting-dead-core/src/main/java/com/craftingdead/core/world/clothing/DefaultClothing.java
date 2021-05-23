/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.world.clothing;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;

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
