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
