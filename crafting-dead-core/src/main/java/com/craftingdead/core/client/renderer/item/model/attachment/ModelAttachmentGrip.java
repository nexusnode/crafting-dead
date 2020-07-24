package com.craftingdead.core.client.renderer.item.model.attachment;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelAttachmentGrip extends Model {
  ModelRenderer Shape1;
  ModelRenderer Shape2;

  public ModelAttachmentGrip() {
    super(RenderType::getEntityCutoutNoCull);

    textureWidth = 64;
    textureHeight = 32;

    Shape1 = new ModelRenderer(this, 0, 0);
    Shape1.addCuboid(0F, 0F, 0F, 6, 1, 3);
    Shape1.setRotationPoint(0F, 0F, -0.5F);
    Shape1.setTextureSize(64, 32);
    Shape1.mirror = true;
    setRotation(Shape1, 0F, 0F, 0F);
    Shape2 = new ModelRenderer(this, 0, 0);
    Shape2.addCuboid(0F, 0F, 0F, 2, 7, 2);
    Shape2.setRotationPoint(2F, 1F, 0F);
    Shape2.setTextureSize(64, 32);
    Shape2.mirror = true;
    setRotation(Shape2, 0F, 0F, 0F);
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue,
      float alpha) {
    matrixStack.push();
    float scale = 0.8F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
    Shape1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape2.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    matrixStack.pop();
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}
