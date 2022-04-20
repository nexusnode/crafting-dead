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

package com.craftingdead.core.client.renderer.entity.grenade;

import com.craftingdead.core.client.model.geom.ModModelLayers;
import com.craftingdead.core.world.entity.grenade.Grenade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GrenadeRenderer extends EntityRenderer<Grenade> {

  private final ModelPart model;

  public GrenadeRenderer(EntityRendererProvider.Context context, ModelPart model) {
    super(context);
    this.model = model;
  }

  @Override
  public void render(Grenade entity, float entityYaw, float partialTick,
      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

    float rotation = entity.getTotalTicksInAir();
    if (!entity.hasStoppedMoving()) {
      rotation += partialTick;
    }
    rotation /= 4.0F;

    var yTranslation = Math.min(rotation / Mth.HALF_PI, 1.0F);
    yTranslation /= 10.0D;
    poseStack.translate(0.0D, yTranslation, 0.0D);
    poseStack.mulPose(Vector3f.XP.rotation(rotation));

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

  public static EntityRendererProvider<Grenade> cylinder() {
    return forModel(ModModelLayers.CYLINDER_GRENADE);
  }

  public static EntityRendererProvider<Grenade> slim() {
    return forModel(ModModelLayers.SLIM_GRENADE);
  }

  public static EntityRendererProvider<Grenade> frag() {
    return forModel(ModModelLayers.FRAG_GRENADE);
  }

  public static EntityRendererProvider<Grenade> forModel(
      ModelLayerLocation modelLocation) {
    return context -> new GrenadeRenderer(context, context.bakeLayer(modelLocation));
  }
}
