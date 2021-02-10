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

package com.craftingdead.core.client.renderer.item.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelAKMIS2 extends Model {

  ModelRenderer Shape1;
  ModelRenderer Shape2;
  ModelRenderer Shape3;
  ModelRenderer Shape4;
  ModelRenderer Shape5;
  ModelRenderer Shape6;

  public ModelAKMIS2() {
    super(RenderType::getEntityCutoutNoCull);

    textureWidth = 64;
    textureHeight = 32;

    Shape1 = new ModelRenderer(this, 11, 13);
    Shape1.addBox(0F, 0F, 0F, 4, 8, 4);
    Shape1.setRotationPoint(0F, 0F, 0F);
    Shape1.setTextureSize(64, 32);
    Shape1.mirror = true;
    setRotation(Shape1, 0F, 0F, 0F);
    Shape2 = new ModelRenderer(this, 11, 8);
    Shape2.addBox(0F, 0F, 0F, 4, 3, 1);
    Shape2.setRotationPoint(0F, -1.5F, -1F);
    Shape2.setTextureSize(64, 32);
    Shape2.mirror = true;
    setRotation(Shape2, 0.3490659F, 0F, 0F);
    Shape3 = new ModelRenderer(this, 11, 8);
    Shape3.addBox(0F, 0F, -1F, 4, 3, 1);
    Shape3.setRotationPoint(0F, -1.5F, 5F);
    Shape3.setTextureSize(64, 32);
    Shape3.mirror = true;
    setRotation(Shape3, -0.3490659F, 0F, 0F);
    Shape4 = new ModelRenderer(this, 22, 8);
    Shape4.addBox(0F, -2F, 0F, 4, 2, 1);
    Shape4.setRotationPoint(0F, -1.5F, -1F);
    Shape4.setTextureSize(64, 32);
    Shape4.mirror = true;
    setRotation(Shape4, -0.3490659F, 0F, 0F);
    Shape5 = new ModelRenderer(this, 22, 8);
    Shape5.addBox(0F, -2F, -1F, 4, 2, 1);
    Shape5.setRotationPoint(0F, -1.5F, 5F);
    Shape5.setTextureSize(64, 32);
    Shape5.mirror = true;
    setRotation(Shape5, 0.3490659F, 0F, 0F);
    Shape6 = new ModelRenderer(this, 11, 4);
    Shape6.addBox(0F, 0F, 0F, 4, 2, 1);
    Shape6.setRotationPoint(0F, -2F, 1.5F);
    Shape6.setTextureSize(64, 32);
    Shape6.mirror = true;
    setRotation(Shape6, 0F, 0F, 0F);
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue,
      float alpha) {
    Shape1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape2.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape3.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape4.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape5.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape6.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}
