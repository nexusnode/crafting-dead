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

package com.craftingdead.core.client.renderer.entity.layer;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.living.ILiving;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class ClothingLayer<T extends LivingEntity, M extends BipedModel<T>>
    extends AbstractClothingLayer<T, M> {

  public ClothingLayer(IEntityRenderer<T, M> renderer) {
    super(renderer);
  }

  @Override
  protected ResourceLocation getClothingTexture(LivingEntity livingEntity, String skinType) {
    // @formatter:off
    return livingEntity.getCapability(ModCapabilities.LIVING)
        .map(ILiving::getItemHandler)
        .map(itemHandler -> itemHandler.getStackInSlot(InventorySlotType.CLOTHING.getIndex()))
        .flatMap(clothingStack -> clothingStack.getCapability(ModCapabilities.CLOTHING).resolve())
        .map(clothing -> clothing.getTexture(skinType))
        .orElse(null);
    // @formatter:on  
  }
}
