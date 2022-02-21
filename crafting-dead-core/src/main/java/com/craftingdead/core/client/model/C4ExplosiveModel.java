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

import com.craftingdead.core.world.entity.grenade.C4Explosive;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class C4ExplosiveModel extends HierarchicalModel<C4Explosive> {

  private final ModelPart root;

  public C4ExplosiveModel(ModelPart root) {
    this.root = root;
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    mesh.getRoot()
        .addOrReplaceChild("anchor",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-2.0F, -1.0F, -3.3333F, 4.0F, 2.0F, 6.0F)
                .texOffs(18, 8)
                .addBox(2.0F, -0.5F, -0.3333F, 1.0F, 1.0F, 2.0F)
                .texOffs(0, 8)
                .addBox(-2.5F, 0.0F, 1.1667F, 6.0F, 0.0F, 3.0F),
            PartPose.offset(0.0F, 0.5F, 0.3333F));
    return LayerDefinition.create(mesh, 32, 32);
  }

  @Override
  public ModelPart root() {
    return this.root;
  }

  @Override
  public void setupAnim(C4Explosive entity, float limbSwing, float limbSwingAmount,
      float ageInTicks, float netHeadYaw, float headPitch) {}
}
