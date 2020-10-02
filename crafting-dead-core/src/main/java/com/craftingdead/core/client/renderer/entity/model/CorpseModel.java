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
package com.craftingdead.core.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class CorpseModel extends Model {

  private final ModelRenderer bipedHead;

  private final ModelRenderer bipedBody;

  private final ModelRenderer bipedRightArm;
  private final ModelRenderer bipedLeftArm;

  private final ModelRenderer bipedRightLeg;
  private final ModelRenderer bipedLeftLeg;

  private final ModelRenderer bipedHeadwear;

  private final ModelRenderer bipedBodyWear;

  private final ModelRenderer bipedRightArmwear;
  private final ModelRenderer bipedLeftArmwear;

  private final ModelRenderer bipedRightLegwear;
  private final ModelRenderer bipedLeftLegwear;

  private final ModelRenderer bipedCape;

  private final ModelRenderer bipedDeadmau5Head;

  private int limbCount;

  public CorpseModel(boolean smallArmsIn) {
    super(RenderType::getEntityCutoutNoCull);

    this.textureWidth = 64;
    this.textureHeight = 64;

    this.bipedHead = new ModelRenderer(this, 0, 0);
    this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
    this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);

    this.bipedBody = new ModelRenderer(this, 16, 16);
    this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
    this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

    if (smallArmsIn) {
      this.bipedLeftArm = new ModelRenderer(this, 32, 48);
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);

      this.bipedRightArm = new ModelRenderer(this, 40, 16);
      this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);

      this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
      this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F);
      this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);

      this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
      this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F);
      this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
    } else {
      this.bipedLeftArm = new ModelRenderer(this, 32, 48);
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

      this.bipedRightArm = new ModelRenderer(this, 40, 16);
      this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

      this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
      this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
      this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);

      this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
      this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
      this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
    }

    this.bipedRightLeg = new ModelRenderer(this, 0, 16);
    this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
    this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

    this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
    this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
    this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

    this.bipedHeadwear = new ModelRenderer(this, 32, 0);
    this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
    this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);

    this.bipedBodyWear = new ModelRenderer(this, 16, 32);
    this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F);
    this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);

    this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
    this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
    this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);

    this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
    this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
    this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);

    this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0);
    this.bipedDeadmau5Head.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, 0.0F);

    this.bipedCape = new ModelRenderer(this, 0, 0);
    this.bipedCape.setTextureSize(64, 32);
    this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, 0.0F);
  }

  public void setLimbCount(int limbCount) {
    this.limbCount = limbCount;
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue, float alpha) {
    this.bipedHead
        .render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
            blue, alpha);
    this.bipedBody
        .render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
            blue, alpha);

    if (this.limbCount > 3) {
      this.bipedRightArm
          .render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
              blue, alpha);
    }

    if (this.limbCount > 2) {
      this.bipedLeftArm
          .render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
              blue, alpha);
    }

    if (this.limbCount > 1) {
      this.bipedRightLeg
          .render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
              blue, alpha);
    }

    if (this.limbCount > 0) {
      this.bipedLeftLeg
          .render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
              blue, alpha);
    }
  }
}
