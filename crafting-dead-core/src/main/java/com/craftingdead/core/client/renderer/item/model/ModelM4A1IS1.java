package com.craftingdead.core.client.renderer.item.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelM4A1IS1 extends Model {
  // fields
  ModelRenderer Shape1;
  ModelRenderer Shape2;
  ModelRenderer Shape3;
  ModelRenderer Shape4;
  ModelRenderer Shape5;
  ModelRenderer Shape6;
  ModelRenderer Shape7;
  ModelRenderer Shape8;
  ModelRenderer Shape9;
  ModelRenderer Shape10;
  ModelRenderer Shape11;

  public ModelM4A1IS1() {
    super(RenderType::getEntityCutoutNoCull);

    textureWidth = 64;
    textureHeight = 32;

    Shape1 = new ModelRenderer(this, 20, 20);
    Shape1.addBox(0F, 0F, 0F, 4, 1, 1);
    Shape1.setRotationPoint(0F, 0F, 0F);
    Shape1.setTextureSize(64, 32);
    Shape1.mirror = true;
    setRotation(Shape1, 0F, 0F, 0F);
    Shape2 = new ModelRenderer(this, 17, 15);
    Shape2.addBox(0F, 0F, 0F, 5, 1, 3);
    Shape2.setRotationPoint(-1F, -1F, -2.5F);
    Shape2.setTextureSize(64, 32);
    Shape2.mirror = true;
    setRotation(Shape2, 0F, 0F, 0F);
    Shape3 = new ModelRenderer(this, 20, 20);
    Shape3.addBox(0F, 0F, 0F, 4, 1, 1);
    Shape3.setRotationPoint(0F, 0F, -3F);
    Shape3.setTextureSize(64, 32);
    Shape3.mirror = true;
    setRotation(Shape3, 0F, 0F, 0F);
    Shape4 = new ModelRenderer(this, 25, 10);
    Shape4.addBox(0F, 0F, 0F, 3, 3, 1);
    Shape4.setRotationPoint(2F, -3F, 0F);
    Shape4.setTextureSize(64, 32);
    Shape4.mirror = true;
    setRotation(Shape4, 0F, 0F, 0F);
    Shape5 = new ModelRenderer(this, 17, 12);
    Shape5.addBox(0F, 0F, 0F, 2, 1, 1);
    Shape5.setRotationPoint(0F, -2F, 0F);
    Shape5.setTextureSize(64, 32);
    Shape5.mirror = true;
    setRotation(Shape5, 0F, 0F, 0F);
    Shape6 = new ModelRenderer(this, 25, 10);
    Shape6.addBox(0F, 0F, 0F, 3, 3, 1);
    Shape6.setRotationPoint(2F, -3F, -3F);
    Shape6.setTextureSize(64, 32);
    Shape6.mirror = true;
    setRotation(Shape6, 0F, 0F, 0F);
    Shape7 = new ModelRenderer(this, 17, 12);
    Shape7.addBox(0F, 0F, 0F, 2, 1, 1);
    Shape7.setRotationPoint(0F, -2F, -3F);
    Shape7.setTextureSize(64, 32);
    Shape7.mirror = true;
    setRotation(Shape7, 0F, 0F, 0F);
    Shape8 = new ModelRenderer(this, 17, 8);
    Shape8.addBox(0F, 0F, 0F, 1, 1, 2);
    Shape8.setRotationPoint(1F, -1.5F, -2F);
    Shape8.setTextureSize(64, 32);
    Shape8.mirror = true;
    setRotation(Shape8, 0F, 0F, 0F);
    Shape9 = new ModelRenderer(this, 17, 5);
    Shape9.addBox(0F, 0F, 0F, 1, 1, 1);
    Shape9.setRotationPoint(2F, -2F, -1.5F);
    Shape9.setTextureSize(64, 32);
    Shape9.mirror = true;
    setRotation(Shape9, 0F, 0F, 0F);
    Shape10 = new ModelRenderer(this, 25, 6);
    Shape10.addBox(0F, 0F, 0F, 2, 2, 1);
    Shape10.setRotationPoint(2F, -2F, -4F);
    Shape10.setTextureSize(64, 32);
    Shape10.mirror = true;
    setRotation(Shape10, 0F, 0F, 0F);
    Shape11 = new ModelRenderer(this, 12, 10);
    Shape11.addBox(0F, 0F, 0F, 0, 2, 2);
    Shape11.setRotationPoint(2.5F, -4F, -2F);
    Shape11.setTextureSize(64, 32);
    Shape11.mirror = true;
    setRotation(Shape11, 0F, 0F, 0F);
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue,
      float alpha) {
    Shape1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape2.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape3.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape4.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape5.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape6.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape7.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape8.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape9.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape10.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    Shape11.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}
