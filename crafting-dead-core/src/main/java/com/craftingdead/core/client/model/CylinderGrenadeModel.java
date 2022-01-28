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
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class CylinderGrenadeModel<T extends Grenade> extends HierarchicalModel<T> {

  private final ModelPart root;

  public CylinderGrenadeModel(ModelPart root) {
    this.root = root;
  }

  public static LayerDefinition createBodyLayer() {
    var mesh = new MeshDefinition();
    var root = mesh.getRoot();

    var anchor = root.addOrReplaceChild("anchor", CubeListBuilder.create(),
        PartPose.offset(0.5F, -2.0F, 0.0F));

    anchor.addOrReplaceChild("body",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-2.0F, -5.0F, -2.0F, 3.0F, 7.0F, 3.0F)
            .texOffs(0, 10)
            .addBox(-1.0F, -7.0F, -1.5F, 1.0F, 2.0F, 2.0F),
        PartPose.offset(0.5F, 2.0F, 1.0F));

    var trigger = anchor.addOrReplaceChild("trigger",
        CubeListBuilder.create()
            .texOffs(6, 10)
            .addBox(-0.5F, 1.2768F, 0.37F, 1.0F, 4.0F, 1.0F)
            .texOffs(8, 15)
            .addBox(-0.5F, -0.571F, -0.3954F, 1.0F, 1.0F, 1.0F),
        PartPose.offset(0.0F, -4.429F, 1.8954F));

    trigger.addOrReplaceChild("cube_r1",
        CubeListBuilder.create()
            .texOffs(4, 15)
            .addBox(-0.5F, -0.1608F, -0.5495F, 1.0F, 2.0F, 1.0F),
        PartPose.offsetAndRotation(0.0F, -0.25F, 0.25F, 0.3927F, 0.0F, 0.0F));

    var lock = anchor.addOrReplaceChild("Lock", CubeListBuilder.create(),
        PartPose.offset(1.1768F, -3.8232F, 0.5F));

    lock.addOrReplaceChild("cube_r2",
        CubeListBuilder.create()
            .texOffs(0, 15)
            .addBox(0.0F, 0.0607F, -1.0F, 0.0F, 2.0F, 2.0F),
        PartPose.offsetAndRotation(-0.75F, -0.75F, 0.0F, 0.0F, 0.0F, -0.7854F));

    return LayerDefinition.create(mesh, 32, 32);
  }

  @Override
  public ModelPart root() {
    return this.root;
  }

  @Override
  public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
      float netHeadYaw, float headPitch) {}
}
