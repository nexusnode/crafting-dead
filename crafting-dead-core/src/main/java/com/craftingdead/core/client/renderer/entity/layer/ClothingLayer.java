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
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class ClothingLayer<T extends LivingEntity, M extends BipedModel<T>>
    extends LayerRenderer<T, M> {

  public ClothingLayer(IEntityRenderer<T, M> renderer) {
    super(renderer);
  }

  @Override
  public void render(MatrixStack stack, IRenderTypeBuffer buffers,
      int packedLight, T livingEntity, float p_225628_5_, float p_225628_6_,
      float partialTicks, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
    if (!livingEntity.isInvisible()) {
      livingEntity.getCapability(ModCapabilities.LIVING).ifPresent(living -> {
        String skinType = livingEntity instanceof ClientPlayerEntity
            ? ((ClientPlayerEntity) livingEntity).getSkinType()
            : "default";
        ItemStack clothingStack =
            living.getItemHandler().getStackInSlot(InventorySlotType.CLOTHING.getIndex());
        clothingStack
            .getCapability(ModCapabilities.CLOTHING)
            .ifPresent(clothing -> LayerRenderer
                .renderCutoutModel(this.getEntityModel(), clothing.getTexture(skinType), stack,
                    buffers,
                    packedLight, livingEntity, 1F, 1F, 1F));
      });
    }
  }
}
