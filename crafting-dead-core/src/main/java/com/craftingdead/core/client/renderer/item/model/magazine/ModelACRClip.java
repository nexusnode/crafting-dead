package com.craftingdead.core.client.renderer.item.model.magazine;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelACRClip extends Model {

  ModelRenderer Shape11;
  ModelRenderer Shape28;

  public ModelACRClip() {
    super(RenderType::getEntityCutoutNoCull);

    textureWidth = 256;
    textureHeight = 128;

    Shape11 = new ModelRenderer(this, 148, 84);
    Shape11.addCuboid(0.4666667F, 0F, 0F, 4, 3, 3);
    Shape11.setRotationPoint(1F, 1.5F, 0F);
    Shape11.setTextureSize(256, 128);
    Shape11.mirror = true;
    setRotation(Shape11, 0F, 0F, -0.1487195F);
    Shape28 = new ModelRenderer(this, 149, 93);
    Shape28.addCuboid(0.3F, 3F, 0F, 4, 3, 3);
    Shape28.setRotationPoint(1F, 1.5F, 0F);
    Shape28.setTextureSize(256, 128);
    Shape28.mirror = true;
    setRotation(Shape28, 0F, 0F, -0.2230767F);
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue, float alpha) {
    Shape11.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape28.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}

