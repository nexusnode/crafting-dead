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

package com.craftingdead.survival.client.model;

import com.craftingdead.survival.world.entity.SupplyDrop;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class SupplyDropModel extends HierarchicalModel<SupplyDrop> {

  private final ModelPart root;
  public final ModelPart parachute;

  public SupplyDropModel(ModelPart root) {
    this.root = root;
    this.parachute = root.getChild("parachute");
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();

    root.addOrReplaceChild("shape1",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(0F, 0.01F, 0F, 32, 4, 32, true),
        PartPose.offset(-16F, 20F, -16F));

    root.addOrReplaceChild("shape2",
        CubeListBuilder.create()
            .texOffs(0, 38)
            .addBox(0F, 0F, 0F, 30, 18, 15, true),
        PartPose.offset(-15F, 2F, 0F));

    root.addOrReplaceChild("shape3",
        CubeListBuilder.create()
            .texOffs(0, 73)
            .addBox(0F, 0F, 0F, 14, 14, 14, true),
        PartPose.offset(0F, 6F, -15F));

    root.addOrReplaceChild("shape4",
        CubeListBuilder.create()
            .texOffs(0, 105)
            .addBox(0F, 0F, 0F, 11, 6, 6, true),
        PartPose.offset(-13F, 14F, -15F));

    root.addOrReplaceChild("shape5",
        CubeListBuilder.create()
            .texOffs(0, 119)
            .addBox(0F, 0F, 0F, 11, 6, 6, true),
        PartPose.offset(-13F, 14F, -7F));

    root.addOrReplaceChild("parachute",
        CubeListBuilder.create()
            .texOffs(0, 133)
            .addBox(0F, 0F, 0F, 40, 30, 40, true),
        PartPose.offset(-20F, -50F, -20F));

    return LayerDefinition.create(mesh, 256, 256);
  }

  @Override
  public ModelPart root() {
    return this.root;
  }

  @Override
  public void setupAnim(SupplyDrop entity, float limbSwing, float limbSwingAmount, float ageInTicks,
      float netHeadYaw, float headPitch) {}
}
