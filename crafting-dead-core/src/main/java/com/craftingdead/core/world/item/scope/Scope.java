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

package com.craftingdead.core.world.item.scope;

import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public interface Scope {

  Capability<Scope> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  boolean isScoping(Entity entity);

  float getZoomMultiplier(Entity entity);

  Optional<ResourceLocation> getOverlayTexture(Entity entity);

  int getOverlayTextureWidth();

  int getOverlayTextureHeight();

  default boolean shouldReduceMouseSensitivity(Entity entity) {
    return this.getZoomMultiplier(entity) >= 5.0F;
  }
}
