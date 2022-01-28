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

import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import com.craftingdead.core.world.item.clothing.Clothing;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ClothingLayer<T extends LivingEntity, M extends HumanoidModel<T>>
    extends AbstractClothingLayer<T, M> {

  public ClothingLayer(RenderLayerParent<T, M> renderer) {
    super(renderer);
  }

  @Override
  protected ResourceLocation getClothingTexture(LivingEntity livingEntity, String skinType) {
    // Resolve optionals to nullable for better performance.
    var livingExtension = livingEntity.getCapability(LivingExtension.CAPABILITY).orElse(null);
    if (livingExtension != null) {
      var clothing =
          livingExtension.getItemHandler().getStackInSlot(ModEquipmentSlotType.CLOTHING.getIndex())
              .getCapability(Clothing.CAPABILITY)
              .orElse(null);
      if (clothing != null) {
        return clothing.getTexture(skinType);
      }
    }
    return null;
  }
}
