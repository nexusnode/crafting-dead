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
