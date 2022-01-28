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

import javax.annotation.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractClothingLayer<T extends LivingEntity, M extends HumanoidModel<T>>
    extends RenderLayer<T, M> {

  public AbstractClothingLayer(RenderLayerParent<T, M> renderer) {
    super(renderer);
  }

  @Nullable
  protected abstract ResourceLocation getClothingTexture(LivingEntity livingEntity,
      String skinType);

  @Override
  public void render(PoseStack matrixStack, MultiBufferSource renderTypeBuffer,
      int packedLight, T livingEntity, float limbSwing, float limbSwingAmount,
      float partialTicks, float ageTicks, float headYaw, float headPitch) {
    Minecraft minecraft = Minecraft.getInstance();
    boolean invisible = livingEntity.isInvisible();
    boolean partiallyVisible =
        livingEntity.isInvisible() && !livingEntity.isInvisibleTo(minecraft.player);
    if (partiallyVisible || !invisible) {
      String skinType = livingEntity instanceof LocalPlayer
          ? ((LocalPlayer) livingEntity).getModelName()
          : "default";
      ResourceLocation texture = this.getClothingTexture(livingEntity, skinType);
      if (texture != null) {
        RenderType renderType = partiallyVisible ? RenderType.itemEntityTranslucentCull(texture)
            : this.getParentModel().renderType(texture);
        this.getParentModel().renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(renderType),
            packedLight, LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F), 1.0F, 1.0F, 1.0F,
            partiallyVisible ? 0.15F : 1.0F);
      }
    }
  }
}
