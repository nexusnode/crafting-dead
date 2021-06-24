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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ParachuteModel extends EntityModel<Entity> {

  private final ModelRenderer bb_main;
  private final ModelRenderer cube_r1;
  private final ModelRenderer cube_r2;
  private final ModelRenderer cube_r3;
  private final ModelRenderer cube_r4;
  private final ModelRenderer cube_r5;
  private final ModelRenderer cube_r6;
  private final ModelRenderer cube_r7;
  private final ModelRenderer cube_r8;

  public ParachuteModel() {
    texWidth = 128;
    texHeight = 128;

    bb_main = new ModelRenderer(this);
    bb_main.setPos(0.0F, 24.0F, 0.0F);
    bb_main.texOffs(48, 1).addBox(-8.0F, -57.0F, -8.0F, 16.0F, 1.0F, 16.0F, 0.0F, false);

    cube_r1 = new ModelRenderer(this);
    cube_r1.setPos(-15.7048F, -54.626F, 0.0F);
    bb_main.addChild(cube_r1);
    setRotationAngle(cube_r1, 0.0F, 0.0F, -0.1745F);
    cube_r1.texOffs(0, 0).addBox(-8.0F, -1.0F, -8.0F, 16.0F, 1.0F, 16.0F, 0.0F, false);

    cube_r2 = new ModelRenderer(this);
    cube_r2.setPos(-30.9324F, -50.5458F, 0.0F);
    bb_main.addChild(cube_r2);
    setRotationAngle(cube_r2, 0.0F, 0.0F, -0.3491F);
    cube_r2.texOffs(0, 17).addBox(-8.0F, -1.0F, -8.0F, 16.0F, 1.0F, 16.0F, 0.0F, false);

    cube_r3 = new ModelRenderer(this);
    cube_r3.setPos(-37.0F, -48.5F, -7.0F);
    bb_main.addChild(cube_r3);
    setRotationAngle(cube_r3, 0.1309F, 0.0F, -0.8727F);
    cube_r3.texOffs(0, 51).addBox(-0.4226F, -0.1075F, -0.1574F, 1.0F, 43.0F, 1.0F, 0.0F,
        false);

    cube_r4 = new ModelRenderer(this);
    cube_r4.setPos(-37.0F, -48.5F, 7.0F);
    bb_main.addChild(cube_r4);
    setRotationAngle(cube_r4, -0.1309F, 0.0F, -0.8727F);
    cube_r4.texOffs(4, 51).addBox(-0.4226F, -0.1075F, -0.8426F, 1.0F, 43.0F, 1.0F, 0.0F,
        false);

    cube_r5 = new ModelRenderer(this);
    cube_r5.setPos(37.0F, -48.5F, 7.0F);
    bb_main.addChild(cube_r5);
    setRotationAngle(cube_r5, -0.1309F, 0.0F, 0.8727F);
    cube_r5.texOffs(8, 51).addBox(-0.5774F, -0.1075F, -0.8426F, 1.0F, 43.0F, 1.0F, 0.0F,
        false);

    cube_r6 = new ModelRenderer(this);
    cube_r6.setPos(15.7048F, -54.626F, 0.0F);
    bb_main.addChild(cube_r6);
    setRotationAngle(cube_r6, 0.0F, 0.0F, 0.1745F);
    cube_r6.texOffs(0, 34).addBox(-8.0F, -1.0F, -8.0F, 16.0F, 1.0F, 16.0F, 0.0F, false);

    cube_r7 = new ModelRenderer(this);
    cube_r7.setPos(37.0F, -48.5F, -7.0F);
    bb_main.addChild(cube_r7);
    setRotationAngle(cube_r7, 0.1309F, 0.0F, 0.8727F);
    cube_r7.texOffs(12, 51).addBox(-0.5774F, -0.1075F, -0.1574F, 1.0F, 43.0F, 1.0F, 0.0F,
        false);

    cube_r8 = new ModelRenderer(this);
    cube_r8.setPos(30.9324F, -50.5458F, 0.0F);
    bb_main.addChild(cube_r8);
    setRotationAngle(cube_r8, 0.0F, 0.0F, 0.3491F);
    cube_r8.texOffs(48, 48).addBox(-8.0F, -1.0F, -8.0F, 16.0F, 1.0F, 16.0F, 0.0F, false);
  }

  @Override
  public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
      float ageInTicks, float netHeadYaw, float headPitch) {}

  @Override
  public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight,
      int packedOverlay, float red, float green, float blue, float alpha) {
    bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
  }

  public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
    modelRenderer.xRot = x;
    modelRenderer.yRot = y;
    modelRenderer.zRot = z;
  }
}
