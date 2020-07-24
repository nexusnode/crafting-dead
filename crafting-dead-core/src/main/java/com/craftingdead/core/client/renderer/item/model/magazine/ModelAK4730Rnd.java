package com.craftingdead.core.client.renderer.item.model.magazine;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelAK4730Rnd extends Model {
  // fields
  ModelRenderer Shape24;
  ModelRenderer Shape25;
  ModelRenderer Shape26;
  ModelRenderer Shape27;
  ModelRenderer Shape28;

  public ModelAK4730Rnd() {
    super(RenderType::getEntityCutoutNoCull);

    textureWidth = 256;
    textureHeight = 128;

    Shape24 = new ModelRenderer(this, 0, 115);
    Shape24.addCuboid(0F, 0F, 0F, 14, 8, 5);
    Shape24.setRotationPoint(-42F, 6F, -1F);
    Shape24.setTextureSize(256, 128);
    Shape24.mirror = true;
    setRotation(Shape24, 0F, 0F, -0.0523599F);
    Shape25 = new ModelRenderer(this, 45, 115);
    Shape25.addCuboid(0F, 0F, 0F, 14, 6, 5);
    Shape25.setRotationPoint(-41.55F, 14F, -1F);
    Shape25.setTextureSize(256, 128);
    Shape25.mirror = true;
    setRotation(Shape25, 0F, 0F, -0.1745329F);
    Shape26 = new ModelRenderer(this, 90, 115);
    Shape26.addCuboid(0F, 0F, 0F, 14, 7, 5);
    Shape26.setRotationPoint(-40.5F, 19.95F, -1F);
    Shape26.setTextureSize(256, 128);
    Shape26.mirror = true;
    setRotation(Shape26, 0F, 0F, -0.296706F);
    Shape27 = new ModelRenderer(this, 135, 115);
    Shape27.addCuboid(0F, 0F, 0F, 14, 5, 5);
    Shape27.setRotationPoint(-38.45F, 26.5F, -1F);
    Shape27.setTextureSize(256, 128);
    Shape27.mirror = true;
    setRotation(Shape27, 0F, 0F, -0.418879F);
    Shape28 = new ModelRenderer(this, 180, 110);
    Shape28.addCuboid(0F, 0F, 0F, 14, 10, 5);
    Shape28.setRotationPoint(-36.4F, 31.05F, -1F);
    Shape28.setTextureSize(256, 128);
    Shape28.mirror = true;
    setRotation(Shape28, 0.0174533F, 0F, -0.6283185F);
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue, float alpha) {
    Shape24.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape25.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape26.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape27.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
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
