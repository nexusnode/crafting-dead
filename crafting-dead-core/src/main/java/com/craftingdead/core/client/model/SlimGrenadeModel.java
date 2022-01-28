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
