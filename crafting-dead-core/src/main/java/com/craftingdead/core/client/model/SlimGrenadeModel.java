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

package com.craftingdead.core.client.model;

import com.craftingdead.core.world.entity.grenade.Grenade;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class SlimGrenadeModel<T extends Grenade> extends HierarchicalModel<T> {

  private final ModelPart root;

  public SlimGrenadeModel(ModelPart root) {
    this.root = root;
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();

    var anchor = root.addOrReplaceChild("anchor", CubeListBuilder.create(),
        PartPose.offset(0.0F, -2.0F, 0.0F));

    anchor.addOrReplaceChild("body", CubeListBuilder.create().texOffs(12, 0)
        .addBox(-1.5F, -2.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
        .texOffs(8, 5).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
        .texOffs(0, 0).addBox(-1.5F, 3.5F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
        .texOffs(0, 5).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
        .texOffs(8, 11).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, -1.5F, 0.0F));

    var trigger = anchor.addOrReplaceChild("trigger",
        CubeListBuilder.create().texOffs(0, 11)
            .addBox(-0.5F, 1.2768F, 0.12F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(4, 16).addBox(-0.5F, -0.571F, -0.6454F, 1.0F, 1.0F, 1.0F,
                new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, -4.929F, 1.6454F));

    trigger.addOrReplaceChild("cube_r1",
        CubeListBuilder.create().texOffs(0, 16).addBox(-0.5F, -0.0652F, -0.3186F, 1.0F, 2.0F, 1.0F,
            new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(0.0F, -0.25F, -0.25F, 0.3927F, 0.0F, 0.0F));

    var lock = anchor
        .addOrReplaceChild("lock", CubeListBuilder.create(),
            PartPose.offset(1.1768F, -4.3232F, 0.0F));

    lock.addOrReplaceChild("cube_r2",
        CubeListBuilder.create().texOffs(4, 11).addBox(0.0F, 0.0607F, -1.0F, 0.0F, 2.0F, 2.0F,
            new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-0.75F, -0.75F, 0.0F, 0.0F, 0.0F, -0.7854F));

    return LayerDefinition.create(mesh, 32, 32);
  }

  @Override
  public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
      float netHeadYaw, float headPitch) {}

  @Override
  public ModelPart root() {
    return this.root;
  }
}
