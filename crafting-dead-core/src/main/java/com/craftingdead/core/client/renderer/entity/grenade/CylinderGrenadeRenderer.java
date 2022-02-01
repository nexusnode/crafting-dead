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

package com.craftingdead.core.client.renderer.entity.grenade;

import com.craftingdead.core.client.model.geom.ModModelLayers;
import com.craftingdead.core.world.entity.grenade.Grenade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CylinderGrenadeRenderer extends EntityRenderer<Grenade> {

  private final ModelPart model;

  public CylinderGrenadeRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.model = context.bakeLayer(ModModelLayers.CYLINDER_GRENADE);
  }

  @Override
  public void render(Grenade entity, float entityYaw, float partialTicks,
      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

    float totalTicks = entity.getTotalTicksInAir();
    if (!entity.isStoppedInGround()) {
      totalTicks += partialTicks;
    }

    poseStack.mulPose(Vector3f.XP.rotation(totalTicks / 3.25F));

    var vertexConsumer =
        bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));
    this.model.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY,
        1.0F, 1.0F, 1.0F, 0.15F);
  }

  @Override
  public ResourceLocation getTextureLocation(Grenade entity) {
    return new ResourceLocation(entity.getType().getRegistryName().getNamespace(),
        "textures/entity/grenade/" + entity.getType().getRegistryName().getPath() + ".png");
  }
}
