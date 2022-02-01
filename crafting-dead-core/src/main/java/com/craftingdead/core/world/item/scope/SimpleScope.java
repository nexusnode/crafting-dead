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
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SimpleScope implements Scope {

  private final float zoomMultiplier;
  private final Optional<ResourceLocation> overlayTexture;
  private final int overlayTextureWidth;
  private final int overlayTextureHeight;
  private final ItemStack itemStack;

  public SimpleScope(ItemStack itemStack) {
    this(1.0F, null, 0, 0, itemStack);
  }

  public SimpleScope(float zoomMultiplier, ResourceLocation overlayTexture,
      int overlayTextureWidth, int overlayTextureHeight, ItemStack itemStack) {
    this.zoomMultiplier = zoomMultiplier;
    this.overlayTexture = Optional.ofNullable(overlayTexture);
    this.overlayTextureWidth = overlayTextureWidth;
    this.overlayTextureHeight = overlayTextureHeight;
    this.itemStack = itemStack;
  }

  @Override
  public boolean isScoping(LivingExtension<?, ?> living) {
    return living.getEntity().getUseItem() == this.itemStack
        && !(living instanceof PlayerExtension<?> player && player.isHandcuffed());
  }

  @Override
  public float getZoomMultiplier(LivingExtension<?, ?> living) {
    return this.zoomMultiplier;
  }

  @Override
  public Optional<ResourceLocation> getOverlayTexture(LivingExtension<?, ?> living) {
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
