package com.craftingdead.mod.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SlimGrenadeModel extends Model {

  private final ModelRenderer shape1;
  private final ModelRenderer shape2;
  private final ModelRenderer shape3;
  private final ModelRenderer shape5;
  private final ModelRenderer shape4;
  private final ModelRenderer shape6;
  private final ModelRenderer shape7;
  private final ModelRenderer shape8;
  private final ModelRenderer shape9;

  public SlimGrenadeModel() {
    super(RenderType::getEntityCutoutNoCull);
    this.textureWidth = 64;
    this.textureHeight = 32;

    this.shape1 = new ModelRenderer(this, 17, 16);
    this.shape1.addCuboid(0F, 0F, 0F, 3, 6, 3);
    this.shape1.setRotationPoint(0F, 0F, 0F);
    this.shape1.setTextureSize(64, 32);
    this.shape1.mirror = true;
    this.setRotation(shape1, 0F, 0F, 0F);
    this.shape2 = new ModelRenderer(this, 17, 26);
    this.shape2.addCuboid(0F, 0F, 0F, 4, 2, 4);
    this.shape2.setRotationPoint(-0.5F, 6F, -0.5F);
    this.shape2.setTextureSize(64, 32);
    this.shape2.mirror = true;
    this.setRotation(shape2, 0F, 0F, 0F);
    this.shape3 = new ModelRenderer(this, 17, 9);
    this.shape3.addCuboid(0F, 0F, 0F, 4, 2, 4);
    this.shape3.setRotationPoint(-0.5F, -2F, -0.5F);
    this.shape3.setTextureSize(64, 32);
    this.shape3.mirror = true;
    this.setRotation(shape3, 0F, 0F, 0F);
    this.shape5 = new ModelRenderer(this, 17, 3);
    this.shape5.addCuboid(0F, 0F, 0F, 2, 3, 2);
    this.shape5.setRotationPoint(0.5F, -5F, 0.5F);
    this.shape5.setTextureSize(64, 32);
    this.shape5.mirror = true;
    this.setRotation(shape5, 0F, 0F, 0F);
    this.shape4 = new ModelRenderer(this, 26, 4);
    this.shape4.addCuboid(0F, 0F, 0F, 1, 2, 2);
    this.shape4.setRotationPoint(2.5F, -5F, 0.5F);
    this.shape4.setTextureSize(64, 32);
    this.shape4.mirror = true;
    this.setRotation(shape4, 0F, 0F, 0F);
    this.shape6 = new ModelRenderer(this, 36, 9);
    this.shape6.addCuboid(0F, 0F, 0F, 4, 1, 2);
    this.shape6.setRotationPoint(3.5F, -5F, 0.5F);
    this.shape6.setTextureSize(64, 32);
    this.shape6.mirror = true;
    this.setRotation(shape6, 0F, 0F, 1.064651F);
    this.shape7 = new ModelRenderer(this, 36, 13);
    this.shape7.addCuboid(0F, 0F, 0F, 1, 5, 2);
    this.shape7.setRotationPoint(4.45F, -1.5F, 0.5F);
    this.shape7.setTextureSize(64, 32);
    this.shape7.mirror = true;
    this.setRotation(shape7, 0F, 0F, 0F);
    this.shape8 = new ModelRenderer(this, 36, 21);
    this.shape8.addCuboid(0F, 0F, 0F, 1, 1, 2);
    this.shape8.setRotationPoint(4F, 2.5F, 0.5F);
    this.shape8.setTextureSize(64, 32);
    this.shape8.mirror = true;
    this.setRotation(shape8, 0F, 0F, 0F);
    this.shape9 = new ModelRenderer(this, 10, 3);
    this.shape9.addCuboid(0F, 0F, 0F, 3, 3, 0);
    this.shape9.setRotationPoint(0F, -5F, 0.5F);
    this.shape9.setTextureSize(64, 32);
    this.shape9.mirror = true;
    this.setRotation(shape9, -0.3839724F, 0F, 0F);
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
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}
