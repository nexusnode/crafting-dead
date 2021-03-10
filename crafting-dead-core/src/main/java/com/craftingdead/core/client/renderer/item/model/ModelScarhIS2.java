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

public class ModelScarhIS2 extends Model {

  ModelRenderer Shape1;
  ModelRenderer Shape2;
  ModelRenderer Shape3;
  ModelRenderer Shape4;
  ModelRenderer Shape5;

  public ModelScarhIS2() {
    super(RenderType::entityCutoutNoCull);

    texWidth = 64;
    texHeight = 32;

    Shape1 = new ModelRenderer(this, 10, 10);
    Shape1.addBox(0F, 0F, 0F, 2, 4, 2);
    Shape1.setPos(0F, 0F, 0F);
    Shape1.setTexSize(64, 32);
    Shape1.mirror = true;
    setRotation(Shape1, 0F, 0F, 0F);
    Shape2 = new ModelRenderer(this, 18, 6);
    Shape2.addBox(0F, 0F, 0F, 2, 2, 1);
    Shape2.setPos(0F, -2F, -1F);
    Shape2.setTexSize(64, 32);
    Shape2.mirror = true;
    setRotation(Shape2, 0F, 0F, 0F);
    Shape3 = new ModelRenderer(this, 10, 1);
    Shape3.addBox(0F, 0F, 0F, 2, 1, 2);
    Shape3.setPos(0F, -3F, 0F);
    Shape3.setTexSize(64, 32);
    Shape3.mirror = true;
    setRotation(Shape3, 0F, 0F, 0F);
    Shape4 = new ModelRenderer(this, 4, 6);
    Shape4.addBox(0F, 0F, 0F, 2, 2, 1);
    Shape4.setPos(0F, -2F, 2F);
    Shape4.setTexSize(64, 32);
    Shape4.mirror = true;
    setRotation(Shape4, 0F, 0F, 0F);
    Shape5 = new ModelRenderer(this, 12, 5);
    Shape5.addBox(0F, 0F, 0F, 0, 2, 2);
    Shape5.setPos(1F, -2F, 0F);
    Shape5.setTexSize(64, 32);
    Shape5.mirror = true;
    setRotation(Shape5, 0F, 0F, 0F);
  }

  @Override
  public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue,
      float alpha) {
    Shape1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.xRot = x;
    model.yRot = y;
    model.zRot = z;
  }
}
