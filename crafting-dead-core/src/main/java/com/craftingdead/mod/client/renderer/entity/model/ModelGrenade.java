package com.craftingdead.mod.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelGrenade extends Model {

  private final ModelRenderer Shape1;
  private final ModelRenderer Shape2;
  private final ModelRenderer Shape3;
  private final ModelRenderer Shape5;
  private final ModelRenderer Shape4;
  private final ModelRenderer Shape6;
  private final ModelRenderer Shape7;
  private final ModelRenderer Shape8;
  private final ModelRenderer Shape9;

  public ModelGrenade() {
    super(RenderType::getEntityCutoutNoCull);
    this.textureWidth = 64;
    this.textureHeight = 32;

    this.Shape1 = new ModelRenderer(this, 17, 16);
    this.Shape1.addCuboid(0F, 0F, 0F, 3, 6, 3);
    this.Shape1.setRotationPoint(0F, 0F, 0F);
    this.Shape1.setTextureSize(64, 32);
    this.Shape2 = new ModelRenderer(this, 17, 26);
    this.Shape2.addCuboid(0F, 0F, 0F, 4, 2, 4);
    this.Shape2.setRotationPoint(-0.5F, 6F, -0.5F);
    this.Shape2.setTextureSize(64, 32);
    this.Shape3 = new ModelRenderer(this, 17, 9);
    this.Shape3.addCuboid(0F, 0F, 0F, 4, 2, 4);
    this.Shape3.setRotationPoint(-0.5F, -2F, -0.5F);
    this.Shape3.setTextureSize(64, 32);
    this.Shape5 = new ModelRenderer(this, 17, 3);
    this.Shape5.addCuboid(0F, 0F, 0F, 2, 3, 2);
    this.Shape5.setRotationPoint(0.5F, -5F, 0.5F);
    this.Shape5.setTextureSize(64, 32);
    this.Shape4 = new ModelRenderer(this, 26, 4);
    this.Shape4.addCuboid(0F, 0F, 0F, 1, 2, 2);
    this.Shape4.setRotationPoint(2.5F, -5F, 0.5F);
    this.Shape4.setTextureSize(64, 32);
    this.Shape6 = new ModelRenderer(this, 36, 9);
    this.Shape6.addCuboid(0F, 0F, 0F, 4, 1, 2);
    this.Shape6.setRotationPoint(3.5F, -5F, 0.5F);
    this.Shape6.setTextureSize(64, 32);
    this.Shape7 = new ModelRenderer(this, 36, 13);
    this.Shape7.addCuboid(0F, 0F, 0F, 1, 5, 2);
    this.Shape7.setRotationPoint(4.45F, -1.5F, 0.5F);
    this.Shape7.setTextureSize(64, 32);
    this.Shape8 = new ModelRenderer(this, 36, 21);
    this.Shape8.addCuboid(0F, 0F, 0F, 1, 1, 2);
    this.Shape8.setRotationPoint(4F, 2.5F, 0.5F);
    this.Shape8.setTextureSize(64, 32);
    this.Shape9 = new ModelRenderer(this, 10, 3);
    this.Shape9.addCuboid(0F, 0F, 0F, 3, 3, 0);
    this.Shape9.setRotationPoint(0F, -5F, 0.5F);
    this.Shape9.setTextureSize(64, 32);
  }

  @Override
  public void render(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_,
      int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
    this.Shape1
        .render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);
    this.Shape2
        .render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);
    this.Shape3
        .render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);
    this.Shape5
        .render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);
    this.Shape4
        .render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);
    this.Shape6
        .render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);
    this.Shape7
        .render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);
    this.Shape8
        .render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);
    this.Shape9
        .render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
            p_225598_7_, p_225598_8_);

  }
}
