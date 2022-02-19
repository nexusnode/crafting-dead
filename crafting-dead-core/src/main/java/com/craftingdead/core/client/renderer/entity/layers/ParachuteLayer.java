/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.renderer.entity.layers;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.model.geom.ModModelLayers;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ParachuteLayer<T extends LivingEntity, M extends EntityModel<T>>
    extends RenderLayer<T, M> {

  private static final ResourceLocation TEXTURE =
      new ResourceLocation(CraftingDead.ID, "textures/entity/parachute.png");

  private final ModelPart model;

  public ParachuteLayer(RenderLayerParent<T, M> entityRenderer, EntityModelSet entityModels) {
    super(entityRenderer);
    this.model = entityModels.bakeLayer(ModModelLayers.PARACHUTE);
  }

  @Override
  public void render(PoseStack poseStack, MultiBufferSource renderTypeBuffer, int packedLight,
      T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageTicks,
      float headYaw, float headPitch) {
    if (livingEntity.hasEffect(ModMobEffects.PARACHUTE.get())) {
      poseStack.pushPose();
      {
        poseStack.translate(0.0D, 0.0D, 0.125D);
        var vertexConsumer = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer,
            RenderType.armorCutoutNoCull(TEXTURE), false, false);
        this.model.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
      }
      poseStack.popPose();
    }
  }

  public static LayerDefinition createParachuteBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();

    var anchor = root.addOrReplaceChild("anchor",
        CubeListBuilder.create()
            .texOffs(48, 1)
            .addBox(-8.0F, -57.0F, -8.0F, 16.0F, 1.0F, 16.0F),
        PartPose.offset(0.0F, 24.0F, 0.0F));

    anchor.addOrReplaceChild("cube_r1",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-8.0F, -1.0F, -8.0F, 16.0F, 1.0F, 16.0F),
        PartPose.offsetAndRotation(-15.7048F, -54.626F, 0.0F, 0.0F, 0.0F, -0.1745F));

    anchor.addOrReplaceChild("cube_r2",
        CubeListBuilder.create()
            .texOffs(0, 17)
            .addBox(-8.0F, -1.0F, -8.0F, 16.0F, 1.0F, 16.0F),
        PartPose.offsetAndRotation(-30.9324F, -50.5458F, 0.0F, 0.0F, 0.0F, -0.3491F));

    anchor.addOrReplaceChild("cube_r3",
        CubeListBuilder.create()
            .texOffs(0, 51)
            .addBox(-0.4226F, -0.1075F, -0.1574F, 1.0F, 43.0F, 1.0F),
        PartPose.offsetAndRotation(-37.0F, -48.5F, -7.0F, 0.1309F, 0.0F, -0.8727F));

    anchor.addOrReplaceChild("cube_r4",
        CubeListBuilder.create()
            .texOffs(4, 51)
            .addBox(-0.4226F, -0.1075F, -0.8426F, 1.0F, 43.0F, 1.0F),
        PartPose.offsetAndRotation(-37.0F, -48.5F, 7.0F, -0.1309F, 0.0F, -0.8727F));

    anchor.addOrReplaceChild("cube_r5",
        CubeListBuilder.create()
            .texOffs(8, 51)
            .addBox(-0.5774F, -0.1075F, -0.8426F, 1.0F, 43.0F, 1.0F),
        PartPose.offsetAndRotation(37.0F, -48.5F, 7.0F, -0.1309F, 0.0F, 0.8727F));

    anchor.addOrReplaceChild("cube_r6",
        CubeListBuilder.create()
            .texOffs(0, 34)
            .addBox(-8.0F, -1.0F, -8.0F, 16.0F, 1.0F, 16.0F),
        PartPose.offsetAndRotation(15.7048F, -54.626F, 0.0F, 0.0F, 0.0F, 0.1745F));

    anchor.addOrReplaceChild("cube_r7",
        CubeListBuilder.create()
            .texOffs(12, 51)
            .addBox(-0.5774F, -0.1075F, -0.1574F, 1.0F, 43.0F, 1.0F),
        PartPose.offsetAndRotation(37.0F, -48.5F, -7.0F, 0.1309F, 0.0F, 0.8727F));

    anchor.addOrReplaceChild("cube_r8",
        CubeListBuilder.create()
            .texOffs(48, 48)
            .addBox(-8.0F, -1.0F, -8.0F, 16.0F, 1.0F, 16.0F),
        PartPose.offsetAndRotation(30.9324F, -50.5458F, 0.0F, 0.0F, 0.0F, 0.3491F));

    return LayerDefinition.create(mesh, 128, 128);
  }
}
