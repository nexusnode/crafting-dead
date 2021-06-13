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

package com.craftingdead.core.client.renderer.entity.layers;

import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.clothing.Clothing;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
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
    // Resolve optionals to nullable for better performance.
    LivingExtension<?, ?> livingExtension =
        livingEntity.getCapability(Capabilities.LIVING).orElse(null);
    if (livingExtension != null) {
      Clothing clothing =
          livingExtension.getItemHandler().getStackInSlot(ModEquipmentSlotType.CLOTHING.getIndex())
              .getCapability(Capabilities.CLOTHING)
              .orElse(null);
      if (clothing != null) {
        return clothing.getTexture(skinType);
      }
    }
    return null;
  }
}
