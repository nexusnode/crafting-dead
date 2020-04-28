package com.craftingdead.mod.client.renderer.entity.model;

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
    super(RenderType::getEntityCutoutNoCull);
    this.textureWidth = 256;
    this.textureHeight = 256;

    this.Shape1 = new ModelRenderer(this, 0, 0);
    this.Shape1.addCuboid(0F, 0.01F, 0F, 32, 4, 32, true);
    this.Shape1.setRotationPoint(-16F, 20F, -16F);
    this.Shape1.setTextureSize(256, 256);
    this.Shape2 = new ModelRenderer(this, 0, 38);
    this.Shape2.addCuboid(0F, 0F, 0F, 30, 18, 15, true);
    this.Shape2.setRotationPoint(-15F, 2F, 0F);
    this.Shape2.setTextureSize(256, 256);
    this.Shape3 = new ModelRenderer(this, 0, 73);
    this.Shape3.addCuboid(0F, 0F, 0F, 14, 14, 14, true);
    this.Shape3.setRotationPoint(0F, 6F, -15F);
    this.Shape3.setTextureSize(256, 256);
    this.Shape4 = new ModelRenderer(this, 0, 105);
    this.Shape4.addCuboid(0F, 0F, 0F, 11, 6, 6, true);
    this.Shape4.setRotationPoint(-13F, 14F, -15F);
    this.Shape4.setTextureSize(256, 256);
    this.Shape5 = new ModelRenderer(this, 0, 119);
    this.Shape5.addCuboid(0F, 0F, 0F, 11, 6, 6, true);
    this.Shape5.setRotationPoint(-13F, 14F, -7F);
    this.Shape5.setTextureSize(256, 256);
    this.parachute = new ModelRenderer(this, 0, 133);
    this.parachute.addCuboid(0F, 0F, 0F, 40, 30, 40, true);
    this.parachute.setRotationPoint(-20F, -50F, -20F);
    this.parachute.setTextureSize(256, 256);
  }

  public void setRenderParachute(boolean renderParachute) {
    this.renderParachute = renderParachute;
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int p_225598_3_,
      int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
    this.Shape1
        .render(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);
    this.Shape2
        .render(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);
    this.Shape3
        .render(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);
    this.Shape4
        .render(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);
    this.Shape5
        .render(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);

    if (this.renderParachute) {
      this.parachute
          .render(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
              p_225598_7_, p_225598_8_);
    }
  }
}
