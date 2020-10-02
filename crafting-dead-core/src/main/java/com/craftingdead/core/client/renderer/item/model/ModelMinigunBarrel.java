/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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

public class ModelMinigunBarrel extends Model {
  // fields

  ModelRenderer Shape11;
  ModelRenderer Shape12;
  ModelRenderer Shape13;
  ModelRenderer Shape14;
  ModelRenderer Shape15;
  ModelRenderer Shape16;
  ModelRenderer Shape17;
  ModelRenderer Shape18;
  ModelRenderer Shape19;

  ModelRenderer Shape29;
  ModelRenderer Shape30;
  ModelRenderer Shape31;
  ModelRenderer Shape32;
  ModelRenderer Shape33;
  ModelRenderer Shape34;

  public ModelMinigunBarrel() {
    super(RenderType::getEntityCutoutNoCull);

    textureWidth = 128;
    textureHeight = 64;

    Shape11 = new ModelRenderer(this, 26, 21);
    Shape11.addBox(0F, 0F, 0F, 2, 4, 4);
    Shape11.setRotationPoint(11F, 0.5F, -2.5F);
    Shape11.setTextureSize(128, 64);
    Shape11.mirror = true;
    setRotation(Shape11, 0F, 0F, 0F);
    Shape12 = new ModelRenderer(this, 26, 21);
    Shape12.addBox(0F, 0F, 0F, 2, 3, 5);
    Shape12.setRotationPoint(11F, 1F, -3F);
    Shape12.setTextureSize(128, 64);
    Shape12.mirror = true;
    setRotation(Shape12, 0F, 0F, 0F);
    Shape13 = new ModelRenderer(this, 26, 21);
    Shape13.addBox(0F, 0F, 0F, 2, 5, 3);
    Shape13.setRotationPoint(11F, 0F, -2F);
    Shape13.setTextureSize(128, 64);
    Shape13.mirror = true;
    setRotation(Shape13, 0F, 0F, 0F);
    Shape14 = new ModelRenderer(this, 0, 30);
    Shape14.addBox(0F, 0F, 0F, 19, 1, 1);
    Shape14.setRotationPoint(13F, 0.7F, -1.8F);
    Shape14.setTextureSize(128, 64);
    Shape14.mirror = true;
    setRotation(Shape14, 0F, 0F, 0F);
    Shape15 = new ModelRenderer(this, 0, 30);
    Shape15.addBox(0F, 0F, 0F, 19, 1, 1);
    Shape15.setRotationPoint(13F, 0.7F, -0.2F);
    Shape15.setTextureSize(128, 64);
    Shape15.mirror = true;
    setRotation(Shape15, 0F, 0F, 0F);
    Shape16 = new ModelRenderer(this, 0, 30);
    Shape16.addBox(0F, 0F, 0F, 19, 1, 1);
    Shape16.setRotationPoint(13F, 2F, 0.5F);
    Shape16.setTextureSize(128, 64);
    Shape16.mirror = true;
    setRotation(Shape16, 0F, 0F, 0F);
    Shape17 = new ModelRenderer(this, 0, 30);
    Shape17.addBox(0F, 0F, 0F, 19, 1, 1);
    Shape17.setRotationPoint(13F, 2F, -2.5F);
    Shape17.setTextureSize(128, 64);
    Shape17.mirror = true;
    setRotation(Shape17, 0F, 0F, 0F);
    Shape18 = new ModelRenderer(this, 0, 30);
    Shape18.addBox(0F, 0F, 0F, 19, 1, 1);
    Shape18.setRotationPoint(13F, 3.3F, -0.2F);
    Shape18.setTextureSize(128, 64);
    Shape18.mirror = true;
    setRotation(Shape18, 0F, 0F, 0F);
    Shape19 = new ModelRenderer(this, 0, 30);
    Shape19.addBox(0F, 0F, 0F, 19, 1, 1);
    Shape19.setRotationPoint(13F, 3.3F, -1.8F);
    Shape19.setTextureSize(128, 64);
    Shape19.mirror = true;
    setRotation(Shape19, 0F, 0F, 0F);
    Shape29 = new ModelRenderer(this, 0, 21);
    Shape29.addBox(0F, 0F, 0F, 1, 4, 4);
    Shape29.setRotationPoint(30F, 0.5F, -2.5F);
    Shape29.setTextureSize(128, 64);
    Shape29.mirror = true;
    setRotation(Shape29, 0F, 0F, 0F);
    Shape30 = new ModelRenderer(this, 0, 21);
    Shape30.addBox(0F, 0F, 0F, 1, 3, 5);
    Shape30.setRotationPoint(30F, 1F, -3F);
    Shape30.setTextureSize(128, 64);
    Shape30.mirror = true;
    setRotation(Shape30, 0F, 0F, 0F);
    Shape31 = new ModelRenderer(this, 0, 21);
    Shape31.addBox(0F, 0F, 0F, 1, 5, 3);
    Shape31.setRotationPoint(30F, 0F, -2F);
    Shape31.setTextureSize(128, 64);
    Shape31.mirror = true;
    setRotation(Shape31, 0F, 0F, 0F);
    Shape32 = new ModelRenderer(this, 0, 21);
    Shape32.addBox(0F, 0F, 0F, 1, 4, 4);
    Shape32.setRotationPoint(26F, 0.5F, -2.5F);
    Shape32.setTextureSize(128, 64);
    Shape32.mirror = true;
    setRotation(Shape32, 0F, 0F, 0F);
    Shape33 = new ModelRenderer(this, 0, 21);
    Shape33.addBox(0F, 0F, 0F, 1, 5, 3);
    Shape33.setRotationPoint(26F, 0F, -2F);
    Shape33.setTextureSize(128, 64);
    Shape33.mirror = true;
    setRotation(Shape33, 0F, 0F, 0F);
    Shape34 = new ModelRenderer(this, 0, 21);
    Shape34.addBox(0F, 0F, 0F, 1, 3, 5);
    Shape34.setRotationPoint(26F, 1F, -3F);
    Shape34.setTextureSize(128, 64);
    Shape34.mirror = true;
    setRotation(Shape34, 0F, 0F, 0F);
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue,
      float alpha) {
    Shape11.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape12.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape13.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape14.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape15.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape16.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape17.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape18.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape19.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape29.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape30.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape31.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape32.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape33.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape34.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}
