/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

package com.craftingdead.survival.client.model;

import com.craftingdead.survival.world.entity.grenade.PipeBomb;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class PipeBombModel extends HierarchicalModel<PipeBomb> {

  private final ModelPart root;

  public PipeBombModel(ModelPart root) {
    this.root = root;
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();

    var anchor = root.addOrReplaceChild("anchor", CubeListBuilder.create(),
        PartPose.offset(0.0F, 4.0F, 0.0F));

    var body = anchor.addOrReplaceChild("body",
        CubeListBuilder.create()
            .texOffs(8, 4)
            .addBox(-2.25F, -6.0F, 0.0F, 4.0F, 3.0F, 0.0F)
            .texOffs(12, 0)
            .addBox(-1.5F, -3.0F, -1.5F, 3.0F, 1.0F, 3.0F)
            .texOffs(0, 4)
            .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 5.0F, 2.0F)
            .texOffs(0, 0)
            .addBox(-1.5F, 3.0F, -1.5F, 3.0F, 1.0F, 3.0F)
            .texOffs(0, 11)
            .addBox(-1.5F, -1.5F, -0.25F, 1.0F, 2.0F, 1.0F)
            .texOffs(12, 11)
            .addBox(-0.25F, -0.75F, -1.25F, 1.0F, 1.0F, 1.0F),
        PartPose.offset(0.0F, 0.0F, 0.0F));

    body.addOrReplaceChild("light_r1",
        CubeListBuilder.create().texOffs(8, 11)
            .addBox(-1.9023F, -2.1563F, -0.4735F, 1.0F, 1.0F, 1.0F)
            .texOffs(4, 11)
            .addBox(-1.5023F, -1.1563F, -0.4735F, 1.0F, 2.0F, 1.0F),
        PartPose.offsetAndRotation(0.0104F, 0.4063F, -0.0271F, 0.0F, -0.7854F, 0.0F));

    return LayerDefinition.create(mesh, 32, 32);
  }

  @Override
  public ModelPart root() {
    return this.root;
  }

  @Override
  public void setupAnim(PipeBomb entity, float limbSwing, float limbSwingAmount, float ageInTicks,
      float netHeadYaw, float headPitch) {}
}
