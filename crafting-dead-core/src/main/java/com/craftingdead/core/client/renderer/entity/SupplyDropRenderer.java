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

package com.craftingdead.core.client.renderer.entity;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.renderer.entity.model.SupplyDropModel;
import com.craftingdead.core.entity.SupplyDropEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class SupplyDropRenderer extends EntityRenderer<SupplyDropEntity> {

  private final SupplyDropModel model = new SupplyDropModel();

  public SupplyDropRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void render(SupplyDropEntity entity, float entityYaw, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225623_6_) {

    matrixStack.translate(0, 1.51D, 0);
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));

    this.model.setRenderParachute(entity.fallDistance > 0 && !entity.isOnGround());

    IVertexBuilder vertexBuilder =
        renderTypeBuffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
    this.model.renderToBuffer(matrixStack, vertexBuilder, p_225623_6_, OverlayTexture.NO_OVERLAY, 1.0F,
        1.0F, 1.0F, 0.15F);
  }

  @Override
  public ResourceLocation getTextureLocation(SupplyDropEntity entity) {
    return new ResourceLocation(CraftingDead.ID, "textures/entity/supply_drop.png");
  }
}
