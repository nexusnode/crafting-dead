/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
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
