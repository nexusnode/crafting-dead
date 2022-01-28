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

package com.craftingdead.survival.client.renderer.entity;

import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.client.model.SupplyDropModel;
import com.craftingdead.survival.client.model.geom.SurvivalModelLayers;
import com.craftingdead.survival.world.entity.SupplyDrop;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SupplyDropRenderer extends EntityRenderer<SupplyDrop> {

  private final SupplyDropModel model;

  public SupplyDropRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.model = new SupplyDropModel(context.bakeLayer(SurvivalModelLayers.SUPPLY_DROP));
  }

  @Override
  public void render(SupplyDrop entity, float entityYaw, float partialTicks,
      PoseStack poseStack, MultiBufferSource bufferSource, int p_225623_6_) {

    poseStack.translate(0, 1.51D, 0);
    poseStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));

    this.model.parachute.visible = entity.fallDistance > 0 && !entity.isOnGround();

    var vertexConsumer =
        bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
    this.model.renderToBuffer(poseStack, vertexConsumer, p_225623_6_, OverlayTexture.NO_OVERLAY,
        1.0F, 1.0F, 1.0F, 0.15F);
  }

  @Override
  public ResourceLocation getTextureLocation(SupplyDrop entity) {
    return new ResourceLocation(CraftingDeadSurvival.ID, "textures/entity/supply_drop.png");
  }
}
