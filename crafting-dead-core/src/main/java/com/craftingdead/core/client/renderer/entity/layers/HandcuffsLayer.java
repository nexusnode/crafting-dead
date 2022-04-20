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

package com.craftingdead.core.client.renderer.entity.layers;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.model.geom.ModModelLayers;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;

public class HandcuffsLayer<T extends Player, M extends EntityModel<T> & ArmedModel>
    extends RenderLayer<T, M> {

  private static final ResourceLocation TEXTURE_LOCATION =
      new ResourceLocation(CraftingDead.ID, "textures/entity/handcuffs.png");

  private final ModelPart left;
  private final ModelPart right;
  private final ModelPart middle;

  public HandcuffsLayer(RenderLayerParent<T, M> parent, EntityModelSet entityModels) {
    super(parent);
    var root = entityModels.bakeLayer(ModModelLayers.HANDCUFFS);
    this.left = root.getChild("left");
    this.right = root.getChild("right");
    this.middle = root.getChild("middle");
  }

  @Override
  public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
      T livingEntity, float limbSwing, float limbSwingAmount,
      float partialTicks, float ageTicks, float headYaw, float headPitch) {
    final var player = PlayerExtension.get(livingEntity);
    if (player != null && player.isHandcuffed()) {
      final var vertexConsumer =
          bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_LOCATION));
      poseStack.pushPose();
      {
        final var scale = 1.4F;
        poseStack.scale(scale, scale, scale);

        poseStack.pushPose();
        {
          this.getParentModel().translateToHand(HumanoidArm.RIGHT, poseStack);
          poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
          poseStack.translate(-0.32D, -0.06D, -0.4D);
          this.right.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();

        poseStack.pushPose();
        {
          this.getParentModel().translateToHand(HumanoidArm.LEFT, poseStack);
          poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
          poseStack.translate(0.07D, -0.06D, -0.4D);
          this.left.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();

        poseStack.pushPose();
        {
          poseStack.translate(-0.22D, 0.34D, 0.14D);
          poseStack.scale(1.3F, 1.0F, 1.0F);
          this.middle.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();

      }
      poseStack.popPose();
    }
  }

  public static LayerDefinition createHandcuffsBodyLayer() {
    final var mesh = new MeshDefinition();
    final var root = mesh.getRoot();
    root.addOrReplaceChild("middle",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .mirror()
            .addBox(0.0F, 0.0F, 0.0F, 4.0F, 1.0F, 1.0F)
            .mirror(false),
        PartPose.offset(0.0F, 0.5F, 0.0F));
    root.addOrReplaceChild("left",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .mirror()
            .addBox(-1.0F, -24.0F, 0.0F, 1.0F, 2.0F, 1.0F)
            .mirror(false)
            .texOffs(0, 0)
            .mirror()
            .addBox(-3.0F, -22.0F, 0.0F, 2.0F, 1.0F, 1.0F)
            .mirror(false)
            .texOffs(0, 0)
            .mirror()
            .addBox(-3.0F, -25.0F, 0.0F, 2.0F, 1.0F, 1.0F)
            .mirror(false)
            .texOffs(0, 0)
            .mirror()
            .addBox(-4.0F, -24.0F, 0.0F, 1.0F, 2.0F, 1.0F)
            .mirror(false),
        PartPose.offset(0.0F, 24.0F, 0.0F));
    root.addOrReplaceChild("right",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .mirror()
            .addBox(5.0F, -22.0F, 0.0F, 2.0F, 1.0F, 1.0F)
            .mirror(false)
            .texOffs(0, 0)
            .mirror()
            .addBox(7.0F, -24.0F, 0.0F, 1.0F, 2.0F, 1.0F)
            .mirror(false)
            .texOffs(0, 0)
            .mirror()
            .addBox(5.0F, -25.0F, 0.0F, 2.0F, 1.0F, 1.0F)
            .mirror(false)
            .texOffs(0, 0)
            .mirror()
            .addBox(4.0F, -24.0F, 0.0F, 1.0F, 2.0F, 1.0F)
            .mirror(false),
        PartPose.offset(0.0F, 24.0F, 0.0F));
    return LayerDefinition.create(mesh, 64, 32);
  }
}
