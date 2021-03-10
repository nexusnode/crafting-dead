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

package com.craftingdead.core.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class FragGrenadeModel extends Model {

  private final ModelRenderer shape1;
  private final ModelRenderer shape2;
  private final ModelRenderer shape3;
  private final ModelRenderer shape5;
  private final ModelRenderer shape4;
  private final ModelRenderer shape6;
  private final ModelRenderer shape7;
  private final ModelRenderer shape8;
  private final ModelRenderer shape9;
  private final ModelRenderer shape10;

  public FragGrenadeModel() {
    super(RenderType::entityCutoutNoCull);
    this.texWidth = 64;
    this.texHeight = 32;

    this.shape1 = new ModelRenderer(this, 0, 0);
    this.shape1.addBox(0F, 0F, 0F, 4, 4, 4);
    this.shape1.setPos(0F, 0F, 0F);
    this.shape1.setTexSize(64, 32);
    this.shape1.mirror = true;
    this.setRotation(this.shape1, 0F, 0F, 0F);
    this.shape2 = new ModelRenderer(this, 0, 0);
    this.shape2.addBox(0F, 0F, 0F, 1, 3, 3);
    this.shape2.setPos(-0.5F, 0.5F, 0.5F);
    this.shape2.setTexSize(64, 32);
    this.shape2.mirror = true;
    this.setRotation(this.shape2, 0F, 0F, 0F);
    this.shape3 = new ModelRenderer(this, 0, 0);
    this.shape3.addBox(0F, 0F, 0F, 3, 3, 1);
    this.shape3.setPos(0.5F, 0.5F, -0.5F);
    this.shape3.setTexSize(64, 32);
    this.shape3.mirror = true;
    this.setRotation(this.shape3, 0F, 0F, 0F);
    this.shape4 = new ModelRenderer(this, 0, 0);
    this.shape4.addBox(0F, 0F, 0F, 1, 3, 3);
    this.shape4.setPos(3.5F, 0.5F, 0.5F);
    this.shape4.setTexSize(64, 32);
    this.shape4.mirror = true;
    this.setRotation(this.shape4, 0F, 0F, 0F);
    this.shape5 = new ModelRenderer(this, 0, 0);
    this.shape5.addBox(0F, 0F, 0F, 3, 3, 1);
    this.shape5.setPos(0.5F, 0.5F, 3.5F);
    this.shape5.setTexSize(64, 32);
    this.shape5.mirror = true;
    this.setRotation(this.shape5, 0F, 0F, 0F);
    this.shape6 = new ModelRenderer(this, 0, 0);
    this.shape6.addBox(0F, 0F, 0F, 3, 1, 3);
    this.shape6.setPos(0.5F, 3.5F, 0.5F);
    this.shape6.setTexSize(64, 32);
    this.shape6.mirror = true;
    this.setRotation(this.shape6, 0F, 0F, 0F);
    this.shape7 = new ModelRenderer(this, 11, 10);
    this.shape7.addBox(0F, 0F, 0F, 3, 1, 3);
    this.shape7.setPos(0.5F, -1F, 0.5F);
    this.shape7.setTexSize(64, 32);
    this.shape7.mirror = true;
    this.setRotation(this.shape7, 0F, 0F, 0F);
    this.shape8 = new ModelRenderer(this, 0, 10);
    this.shape8.addBox(0F, 0F, 0F, 2, 2, 2);
    this.shape8.setPos(1F, -3F, 1F);
    this.shape8.setTexSize(64, 32);
    this.shape8.mirror = true;
    this.setRotation(this.shape8, 0F, 0F, 0F);
    this.shape9 = new ModelRenderer(this, 0, 15);
    this.shape9.addBox(0F, 0F, 0F, 2, 1, 1);
    this.shape9.setPos(3F, -3F, 1.5F);
    this.shape9.setTexSize(64, 32);
    this.shape9.mirror = true;
    this.setRotation(this.shape9, 0F, 0F, 0.6457718F);
    this.shape10 = new ModelRenderer(this, 0, 18);
    this.shape10.addBox(0F, 0F, 0F, 4, 1, 1);
    this.shape10.setPos(4.5F, -1.9F, 1.5F);
    this.shape10.setTexSize(64, 32);
    this.shape10.mirror = true;
    this.setRotation(this.shape10, 0F, 0F, 1.047198F);
  }

  @Override
  public void renderToBuffer(MatrixStack matrix, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue, float alpha) {
    this.shape1.render(matrix, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    this.shape2.render(matrix, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    this.shape3.render(matrix, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    this.shape5.render(matrix, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    this.shape4.render(matrix, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    this.shape6.render(matrix, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    this.shape7.render(matrix, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    this.shape8.render(matrix, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    this.shape9.render(matrix, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    this.shape10.render(matrix, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.xRot = x;
    model.yRot = y;
    model.zRot = z;
  }
}
