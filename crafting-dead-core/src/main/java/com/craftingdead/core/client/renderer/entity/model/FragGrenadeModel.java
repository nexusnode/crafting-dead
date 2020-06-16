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
    super(RenderType::getEntityCutoutNoCull);
    this.textureWidth = 64;
    this.textureHeight = 32;

    this.shape1 = new ModelRenderer(this, 0, 0);
    this.shape1.addCuboid(0F, 0F, 0F, 4, 4, 4);
    this.shape1.setRotationPoint(0F, 0F, 0F);
    this.shape1.setTextureSize(64, 32);
    this.shape1.mirror = true;
    this.setRotation(this.shape1, 0F, 0F, 0F);
    this.shape2 = new ModelRenderer(this, 0, 0);
    this.shape2.addCuboid(0F, 0F, 0F, 1, 3, 3);
    this.shape2.setRotationPoint(-0.5F, 0.5F, 0.5F);
    this.shape2.setTextureSize(64, 32);
    this.shape2.mirror = true;
    this.setRotation(this.shape2, 0F, 0F, 0F);
    this.shape3 = new ModelRenderer(this, 0, 0);
    this.shape3.addCuboid(0F, 0F, 0F, 3, 3, 1);
    this.shape3.setRotationPoint(0.5F, 0.5F, -0.5F);
    this.shape3.setTextureSize(64, 32);
    this.shape3.mirror = true;
    this.setRotation(this.shape3, 0F, 0F, 0F);
    this.shape4 = new ModelRenderer(this, 0, 0);
    this.shape4.addCuboid(0F, 0F, 0F, 1, 3, 3);
    this.shape4.setRotationPoint(3.5F, 0.5F, 0.5F);
    this.shape4.setTextureSize(64, 32);
    this.shape4.mirror = true;
    this.setRotation(this.shape4, 0F, 0F, 0F);
    this.shape5 = new ModelRenderer(this, 0, 0);
    this.shape5.addCuboid(0F, 0F, 0F, 3, 3, 1);
    this.shape5.setRotationPoint(0.5F, 0.5F, 3.5F);
    this.shape5.setTextureSize(64, 32);
    this.shape5.mirror = true;
    this.setRotation(this.shape5, 0F, 0F, 0F);
    this.shape6 = new ModelRenderer(this, 0, 0);
    this.shape6.addCuboid(0F, 0F, 0F, 3, 1, 3);
    this.shape6.setRotationPoint(0.5F, 3.5F, 0.5F);
    this.shape6.setTextureSize(64, 32);
    this.shape6.mirror = true;
    this.setRotation(this.shape6, 0F, 0F, 0F);
    this.shape7 = new ModelRenderer(this, 11, 10);
    this.shape7.addCuboid(0F, 0F, 0F, 3, 1, 3);
    this.shape7.setRotationPoint(0.5F, -1F, 0.5F);
    this.shape7.setTextureSize(64, 32);
    this.shape7.mirror = true;
    this.setRotation(this.shape7, 0F, 0F, 0F);
    this.shape8 = new ModelRenderer(this, 0, 10);
    this.shape8.addCuboid(0F, 0F, 0F, 2, 2, 2);
    this.shape8.setRotationPoint(1F, -3F, 1F);
    this.shape8.setTextureSize(64, 32);
    this.shape8.mirror = true;
    this.setRotation(this.shape8, 0F, 0F, 0F);
    this.shape9 = new ModelRenderer(this, 0, 15);
    this.shape9.addCuboid(0F, 0F, 0F, 2, 1, 1);
    this.shape9.setRotationPoint(3F, -3F, 1.5F);
    this.shape9.setTextureSize(64, 32);
    this.shape9.mirror = true;
    this.setRotation(this.shape9, 0F, 0F, 0.6457718F);
    this.shape10 = new ModelRenderer(this, 0, 18);
    this.shape10.addCuboid(0F, 0F, 0F, 4, 1, 1);
    this.shape10.setRotationPoint(4.5F, -1.9F, 1.5F);
    this.shape10.setTextureSize(64, 32);
    this.shape10.mirror = true;
    this.setRotation(this.shape10, 0F, 0F, 1.047198F);
  }

  @Override
  public void render(MatrixStack matrix, IVertexBuilder vertexBuilder, int p_225598_3_,
      int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
    this.shape1.render(matrix, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
        p_225598_7_, p_225598_8_);
    this.shape2.render(matrix, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
        p_225598_7_, p_225598_8_);
    this.shape3.render(matrix, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
        p_225598_7_, p_225598_8_);
    this.shape5.render(matrix, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
        p_225598_7_, p_225598_8_);
    this.shape4.render(matrix, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
        p_225598_7_, p_225598_8_);
    this.shape6.render(matrix, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
        p_225598_7_, p_225598_8_);
    this.shape7.render(matrix, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
        p_225598_7_, p_225598_8_);
    this.shape8.render(matrix, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
        p_225598_7_, p_225598_8_);
    this.shape9.render(matrix, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
        p_225598_7_, p_225598_8_);
    this.shape10.render(matrix, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
        p_225598_7_, p_225598_8_);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}
