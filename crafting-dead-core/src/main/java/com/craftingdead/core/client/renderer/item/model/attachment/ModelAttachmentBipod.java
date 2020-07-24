package com.craftingdead.core.client.renderer.item.model.attachment;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelAttachmentBipod extends Model {
  ModelRenderer Shape1;
  ModelRenderer Shape2;
  ModelRenderer Shape3;
  ModelRenderer Shape4;
  ModelRenderer Shape5;
  ModelRenderer Shape6;
  ModelRenderer Shape7;
  ModelRenderer Shape8;

  public ModelAttachmentBipod() {
    super(RenderType::getEntityCutoutNoCull);

    textureWidth = 64;
    textureHeight = 32;

    Shape1 = new ModelRenderer(this, 0, 0);
    Shape1.addCuboid(0F, 0F, 0F, 6, 1, 3);
    Shape1.setRotationPoint(0F, 0F, 0F);
    Shape1.setTextureSize(64, 32);
    Shape1.mirror = true;
    setRotation(Shape1, 0F, 0F, 0F);
    Shape2 = new ModelRenderer(this, 0, 7);
    Shape2.addCuboid(0F, 0F, 0F, 6, 2, 2);
    Shape2.setRotationPoint(2F, 1F, -0.5F);
    Shape2.setTextureSize(64, 32);
    Shape2.mirror = true;
    setRotation(Shape2, 0F, 0.0349066F, 0.0523599F);
    Shape3 = new ModelRenderer(this, 0, 7);
    Shape3.addCuboid(0F, 0F, -2F, 6, 2, 2);
    Shape3.setRotationPoint(2F, 1F, 3.5F);
    Shape3.setTextureSize(64, 32);
    Shape3.mirror = true;
    setRotation(Shape3, 0F, -0.0349066F, 0.0523599F);
    Shape4 = new ModelRenderer(this, 0, 0);
    Shape4.addCuboid(0F, 0F, 0F, 2, 1, 2);
    Shape4.setRotationPoint(0F, 1F, 0.5F);
    Shape4.setTextureSize(64, 32);
    Shape4.mirror = true;
    setRotation(Shape4, 0F, 0F, 0F);
    Shape5 = new ModelRenderer(this, 0, 12);
    Shape5.addCuboid(6F, 0.5F, 0F, 6, 1, 1);
    Shape5.setRotationPoint(2F, 1F, 0F);
    Shape5.setTextureSize(64, 32);
    Shape5.mirror = true;
    setRotation(Shape5, 0F, 0.0349066F, 0.0523599F);
    Shape6 = new ModelRenderer(this, 0, 15);
    Shape6.addCuboid(12F, 0F, 0F, 2, 2, 2);
    Shape6.setRotationPoint(2F, 1F, -0.5F);
    Shape6.setTextureSize(64, 32);
    Shape6.mirror = true;
    setRotation(Shape6, 0F, 0.0349066F, 0.0523599F);
    Shape7 = new ModelRenderer(this, 0, 12);
    Shape7.addCuboid(6F, 0.5F, -1.5F, 6, 1, 1);
    Shape7.setRotationPoint(2F, 1F, 3.5F);
    Shape7.setTextureSize(64, 32);
    Shape7.mirror = true;
    setRotation(Shape7, 0F, -0.0349066F, 0.0523599F);
    Shape8 = new ModelRenderer(this, 0, 15);
    Shape8.addCuboid(12F, 0F, -2F, 2, 2, 2);
    Shape8.setRotationPoint(2F, 1F, 3.5F);
    Shape8.setTextureSize(64, 32);
    Shape8.mirror = true;
    setRotation(Shape8, 0F, -0.0349066F, 0.0523599F);
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue,
      float alpha) {
    matrixStack.push();
    float scale = 0.7F;
    matrixStack.scale(scale, scale, scale);
    Shape1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape2.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape3.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape4.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape5.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape6.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape7.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape8.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    matrixStack.pop();
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}
