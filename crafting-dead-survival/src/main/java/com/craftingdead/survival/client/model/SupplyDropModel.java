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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SupplyDropModel extends Model {

  private final ModelRenderer Shape1;
  private final ModelRenderer Shape2;
  private final ModelRenderer Shape3;
  private final ModelRenderer Shape4;
  private final ModelRenderer Shape5;
  private final ModelRenderer parachute;

  private boolean renderParachute;

  public SupplyDropModel() {
    super(RenderType::entityCutoutNoCull);
    this.texWidth = 256;
    this.texHeight = 256;

    this.Shape1 = new ModelRenderer(this, 0, 0);
    this.Shape1.addBox(0F, 0.01F, 0F, 32, 4, 32, true);
    this.Shape1.setPos(-16F, 20F, -16F);
    this.Shape1.setTexSize(256, 256);
    this.Shape2 = new ModelRenderer(this, 0, 38);
    this.Shape2.addBox(0F, 0F, 0F, 30, 18, 15, true);
    this.Shape2.setPos(-15F, 2F, 0F);
    this.Shape2.setTexSize(256, 256);
    this.Shape3 = new ModelRenderer(this, 0, 73);
    this.Shape3.addBox(0F, 0F, 0F, 14, 14, 14, true);
    this.Shape3.setPos(0F, 6F, -15F);
    this.Shape3.setTexSize(256, 256);
    this.Shape4 = new ModelRenderer(this, 0, 105);
    this.Shape4.addBox(0F, 0F, 0F, 11, 6, 6, true);
    this.Shape4.setPos(-13F, 14F, -15F);
    this.Shape4.setTexSize(256, 256);
    this.Shape5 = new ModelRenderer(this, 0, 119);
    this.Shape5.addBox(0F, 0F, 0F, 11, 6, 6, true);
    this.Shape5.setPos(-13F, 14F, -7F);
    this.Shape5.setTexSize(256, 256);
    this.parachute = new ModelRenderer(this, 0, 133);
    this.parachute.addBox(0F, 0F, 0F, 40, 30, 40, true);
    this.parachute.setPos(-20F, -50F, -20F);
    this.parachute.setTexSize(256, 256);
  }

  public void setRenderParachute(boolean renderParachute) {
    this.renderParachute = renderParachute;
  }

  @Override
  public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float greeen, float blue, float alpha) {
    this.Shape1
        .render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, greeen,
            blue, alpha);
    this.Shape2
        .render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, greeen,
            blue, alpha);
    this.Shape3
        .render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, greeen,
            blue, alpha);
    this.Shape4
        .render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, greeen,
            blue, alpha);
    this.Shape5
        .render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, greeen,
            blue, alpha);

    if (this.renderParachute) {
      this.parachute
          .render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, greeen,
              blue, alpha);
    }
  }
}
