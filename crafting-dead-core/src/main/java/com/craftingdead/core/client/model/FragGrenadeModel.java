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

package com.craftingdead.core.client.model;

import com.craftingdead.core.world.entity.grenade.FragGrenade;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class FragGrenadeModel extends HierarchicalModel<FragGrenade> {

  private final ModelPart root;

  public FragGrenadeModel(ModelPart root) {
    this.root = root;
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();

    var anchor = root.addOrReplaceChild("anchor", CubeListBuilder.create(),
        PartPose.offset(0.0F, 2.0F, 0.0F));

    anchor.addOrReplaceChild("body",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F)
            .texOffs(12, 8)
            .addBox(-1.5F, 2.0F, -1.5F, 3.0F, 1.0F, 3.0F)
            .texOffs(0, 8)
            .addBox(-1.5F, -3.0F, -1.5F, 3.0F, 1.0F, 3.0F)
            .texOffs(0, 12)
            .addBox(-0.5F, -5.0F, -1.0F, 1.0F, 2.0F, 2.0F)
            .texOffs(4, 16)
            .addBox(-0.5F, -5.0F, 1.0F, 1.0F, 1.0F, 1.0F),
        PartPose.offset(0.0F, 0.0F, 0.0F));

    var trigger = anchor.addOrReplaceChild("trigger",
        CubeListBuilder.create()
            .texOffs(10, 12)
            .addBox(-0.5F, 2.3472F, 0.518F, 1.0F, 3.0F, 1.0F),
        PartPose.offset(0.0F, -4.5756F, 1.6301F));

    trigger.addOrReplaceChild("cube_r1",
        CubeListBuilder.create()
            .texOffs(0, 16)
            .addBox(-0.5F, -0.2505F, -0.4958F, 1.0F, 3.0F, 1.0F),
        PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

    var lock = anchor.addOrReplaceChild("lock", CubeListBuilder.create(),
        PartPose.offset(1.1768F, -3.8232F, 0.0F));

    lock.addOrReplaceChild("cube_r2",
        CubeListBuilder.create()
            .texOffs(6, 12)
            .addBox(0.0F, -0.75F, -1.0F, 0.0F, 2.0F, 2.0F),
        PartPose.offsetAndRotation(-0.1768F, -0.1768F, 0.0F, 0.0F, 0.0F, -0.7854F));

    return LayerDefinition.create(mesh, 32, 32);
  }

  @Override
  public ModelPart root() {
    return this.root;
  }

  @Override
  public void setupAnim(FragGrenade entity, float limbSwing, float limbSwingAmount,
      float ageInTicks, float netHeadYaw, float headPitch) {}
}
