package com.craftingdead.mod.client.renderer.entity.model;

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
    super(RenderType::func_228640_c_);

    this.textureWidth = 64;
    this.textureHeight = 64;

    this.bipedHead = new ModelRenderer(this, 0, 0);
    this.bipedHead.func_228301_a_(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
    this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);

    this.bipedBody = new ModelRenderer(this, 16, 16);
    this.bipedBody.func_228301_a_(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
    this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

    if (smallArmsIn) {
      this.bipedLeftArm = new ModelRenderer(this, 32, 48);
      this.bipedLeftArm.func_228301_a_(-1.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);

      this.bipedRightArm = new ModelRenderer(this, 40, 16);
      this.bipedRightArm.func_228301_a_(-2.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);

      this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
      this.bipedLeftArmwear.func_228301_a_(-1.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F);
      this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);

      this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
      this.bipedRightArmwear.func_228301_a_(-2.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F);
      this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
    } else {
      this.bipedLeftArm = new ModelRenderer(this, 32, 48);
      this.bipedLeftArm.func_228301_a_(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

      this.bipedRightArm = new ModelRenderer(this, 40, 16);
      this.bipedRightArm.func_228301_a_(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

      this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
      this.bipedLeftArmwear.func_228301_a_(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
      this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);

      this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
      this.bipedRightArmwear.func_228301_a_(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
      this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
    }

    this.bipedRightLeg = new ModelRenderer(this, 0, 16);
    this.bipedRightLeg.func_228301_a_(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
    this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

    this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
    this.bipedLeftLeg.func_228301_a_(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
    this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

    this.bipedHeadwear = new ModelRenderer(this, 32, 0);
    this.bipedHeadwear.func_228301_a_(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
    this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);

    this.bipedBodyWear = new ModelRenderer(this, 16, 32);
    this.bipedBodyWear.func_228301_a_(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F);
    this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);

    this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
    this.bipedRightLegwear.func_228301_a_(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
    this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);

    this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
    this.bipedLeftLegwear.func_228301_a_(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
    this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);

    this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0);
    this.bipedDeadmau5Head.func_228301_a_(-3.0F, -6.0F, -1.0F, 6, 6, 1, 0.0F);

    this.bipedCape = new ModelRenderer(this, 0, 0);
    this.bipedCape.setTextureSize(64, 32);
    this.bipedCape.func_228301_a_(-5.0F, 0.0F, -1.0F, 10, 16, 1, 0.0F);
  }

  public void setLimbCount(int limbCount) {
    this.limbCount = limbCount;
  }

  @Override
  public void func_225598_a_(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int p_225598_3_,
      int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
    this.bipedHead
        .func_228309_a_(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_,
            p_225598_6_, p_225598_7_, p_225598_8_);
    this.bipedBody
        .func_228309_a_(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_,
            p_225598_6_, p_225598_7_, p_225598_8_);

    if (this.limbCount > 3) {
      this.bipedRightArm
          .func_228309_a_(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_,
              p_225598_6_, p_225598_7_, p_225598_8_);
    }

    if (this.limbCount > 2) {
      this.bipedLeftArm
          .func_228309_a_(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_,
              p_225598_6_, p_225598_7_, p_225598_8_);
    }

    if (this.limbCount > 1) {
      this.bipedRightLeg
          .func_228309_a_(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_,
              p_225598_6_, p_225598_7_, p_225598_8_);
    }

    if (this.limbCount > 0) {
      this.bipedLeftLeg
          .func_228309_a_(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_,
              p_225598_6_, p_225598_7_, p_225598_8_);
    }
  }
}
