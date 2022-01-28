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
