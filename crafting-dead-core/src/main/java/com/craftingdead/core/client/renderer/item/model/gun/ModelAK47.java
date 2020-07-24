package com.craftingdead.core.client.renderer.item.model.gun;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelAK47 extends Model {
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
  ModelRenderer Shape12;
  ModelRenderer Shape13;
  ModelRenderer Shape14;
  ModelRenderer Shape15;
  ModelRenderer Shape16;
  ModelRenderer Shape17;
  ModelRenderer Shape18;
  ModelRenderer Shape19;
  ModelRenderer Shape20;
  ModelRenderer Shape21;
  ModelRenderer Shape22;
  ModelRenderer Shape23;
  // ModelRenderer Shape24;
  ModelRenderer Shape25;
  ModelRenderer Shape26;
  ModelRenderer Shape27;
  ModelRenderer Shape28;
  ModelRenderer Shape29;
  ModelRenderer Shape30;
  ModelRenderer Shape31;
  ModelRenderer Shape32;
  ModelRenderer Shape33;
  ModelRenderer Shape34;
  ModelRenderer Shape35;

  public ModelAK47() {
    super(RenderType::getEntityCutoutNoCull);
    textureWidth = 256;
    textureHeight = 128;

    Shape1 = new ModelRenderer(this, 5, 60);
    Shape1.addCuboid(0F, 0F, 0F, 36, 3, 3);
    Shape1.setRotationPoint(0F, 0.5F, 0F);
    Shape1.setTextureSize(256, 128);
    Shape1.mirror = true;
    setRotation(Shape1, 0F, 0F, 0F);
    Shape2 = new ModelRenderer(this, 40, 50);
    Shape2.addCuboid(0F, 0F, 0F, 34, 4, 4);
    Shape2.setRotationPoint(-20F, -6.25F, -0.5F);
    Shape2.setTextureSize(256, 128);
    Shape2.mirror = true;
    setRotation(Shape2, 0F, 0F, 0F);
    Shape3 = new ModelRenderer(this, 25, 45);
    Shape3.addCuboid(0F, 0F, 0F, 3, 9, 3);
    Shape3.setRotationPoint(11.8F, -4F, 0F);
    Shape3.setTextureSize(256, 128);
    Shape3.mirror = true;
    setRotation(Shape3, 0F, 0F, -0.7435722F);
    Shape4 = new ModelRenderer(this, 30, 70);
    Shape4.addCuboid(0F, 0F, 0F, 6, 4, 4);
    Shape4.setRotationPoint(14F, 0F, -0.5F);
    Shape4.setTextureSize(256, 128);
    Shape4.mirror = true;
    setRotation(Shape4, 0F, 0F, 0F);
    Shape5 = new ModelRenderer(this, 0, 70);
    Shape5.addCuboid(0F, 0F, 0F, 8, 4, 4);
    Shape5.setRotationPoint(33F, 0F, -0.5F);
    Shape5.setTextureSize(256, 128);
    Shape5.mirror = true;
    setRotation(Shape5, 0F, 0F, 0F);
    Shape6 = new ModelRenderer(this, 55, 70);
    Shape6.addCuboid(0F, 0F, 0F, 2, 6, 5);
    Shape6.setRotationPoint(-0.5F, -1.5F, -1F);
    Shape6.setTextureSize(256, 128);
    Shape6.mirror = true;
    setRotation(Shape6, 0F, 0F, 0F);
    Shape7 = new ModelRenderer(this, 75, 70);
    Shape7.addCuboid(0F, 0F, 0F, 1, 12, 6);
    Shape7.setRotationPoint(-1F, -7F, -1.5F);
    Shape7.setTextureSize(256, 128);
    Shape7.mirror = true;
    setRotation(Shape7, 0F, 0F, 0F);
    Shape8 = new ModelRenderer(this, 120, 45);
    Shape8.addCuboid(0F, 0F, 0F, 15, 6, 6);
    Shape8.setRotationPoint(-16F, -7.2F, -1.5F);
    Shape8.setTextureSize(256, 128);
    Shape8.mirror = true;
    setRotation(Shape8, 0F, 0F, 0.0087266F);
    Shape9 = new ModelRenderer(this, 100, 60);
    Shape9.addCuboid(0F, 0F, 0F, 20, 6, 6);
    Shape9.setRotationPoint(-21F, -0.5F, -1.5F);
    Shape9.setTextureSize(256, 128);
    Shape9.mirror = true;
    setRotation(Shape9, 0F, 0F, 0F);
    Shape10 = new ModelRenderer(this, 155, 60);
    Shape10.addCuboid(0F, 0F, 0F, 3, 7, 5);
    Shape10.setRotationPoint(-20.75F, -7.4F, -1F);
    Shape10.setTextureSize(256, 128);
    Shape10.mirror = true;
    setRotation(Shape10, 0F, 0F, 0F);
    Shape11 = new ModelRenderer(this, 100, 75);
    Shape11.addCuboid(0F, 0F, 0F, 20, 3, 6);
    Shape11.setRotationPoint(-21F, 3.3F, -1.5F);
    Shape11.setTextureSize(256, 128);
    Shape11.mirror = true;
    setRotation(Shape11, 0F, 0F, -0.0349066F);
    Shape12 = new ModelRenderer(this, 165, 45);
    Shape12.addCuboid(0F, 0F, 0F, 3, 4, 5);
    Shape12.setRotationPoint(-23.75F, -7.4F, -1F);
    Shape12.setTextureSize(256, 128);
    Shape12.mirror = true;
    setRotation(Shape12, 0F, 0F, 0F);
    Shape13 = new ModelRenderer(this, 120, 35);
    Shape13.addCuboid(0F, 0F, 0F, 5, 1, 5);
    Shape13.setRotationPoint(-23.5F, -4.4F, -1F);
    Shape13.setTextureSize(256, 128);
    Shape13.mirror = true;
    setRotation(Shape13, 0F, 0F, 0.1745329F);
    Shape14 = new ModelRenderer(this, 145, 32);
    Shape14.addCuboid(0F, 0F, 0F, 7, 6, 4);
    Shape14.setRotationPoint(-27F, -6F, -0.5F);
    Shape14.setTextureSize(256, 128);
    Shape14.mirror = true;
    setRotation(Shape14, 0F, 0F, 0F);
    Shape15 = new ModelRenderer(this, 162, 75);
    Shape15.addCuboid(0F, 0F, 0F, 39, 6, 6);
    Shape15.setRotationPoint(-60F, 0.9F, -1.5F);
    Shape15.setTextureSize(256, 128);
    Shape15.mirror = true;
    setRotation(Shape15, 0F, 0F, -0.0218166F);
    Shape16 = new ModelRenderer(this, 180, 58);
    Shape16.addCuboid(0F, 0F, 0F, 1, 7, 6);
    Shape16.setRotationPoint(-60.6F, -0.2F, -1.5F);
    Shape16.setTextureSize(256, 128);
    Shape16.mirror = true;
    setRotation(Shape16, 0F, 0F, -0.0698132F);
    Shape17 = new ModelRenderer(this, 10, 90);
    Shape17.addCuboid(0F, 0F, 0F, 39, 2, 6);
    Shape17.setRotationPoint(-60F, -0.25F, -1.5F);
    Shape17.setTextureSize(256, 128);
    Shape17.mirror = true;
    setRotation(Shape17, 0F, 0F, 0F);
    Shape18 = new ModelRenderer(this, 45, 35);
    Shape18.addCuboid(0F, 0F, 0F, 1, 6, 6);
    Shape18.setRotationPoint(-17F, -7.2F, -1.5F);
    Shape18.setTextureSize(256, 128);
    Shape18.mirror = true;
    setRotation(Shape18, 0F, 0F, 0.0087266F);
    Shape19 = new ModelRenderer(this, 200, 40);
    Shape19.addCuboid(0F, 0F, 0F, 15, 3, 6);
    Shape19.setRotationPoint(-59.5F, -3F, -1.5F);
    Shape19.setTextureSize(256, 128);
    Shape19.mirror = true;
    setRotation(Shape19, 0F, 0F, 0F);
    Shape20 = new ModelRenderer(this, 175, 35);
    Shape20.addCuboid(0F, 0F, 0F, 4, 2, 6);
    Shape20.setRotationPoint(-44.5F, -3F, -1.5F);
    Shape20.setTextureSize(256, 128);
    Shape20.mirror = true;
    setRotation(Shape20, 0F, 0F, 0.7853982F);
    Shape21 = new ModelRenderer(this, 220, 30);
    Shape21.addCuboid(0F, 0F, 0F, 5, 3, 5);
    Shape21.setRotationPoint(-59.5F, -3F, -1F);
    Shape21.setTextureSize(256, 128);
    Shape21.mirror = true;
    setRotation(Shape21, 0F, 0F, -0.9599311F);
    Shape22 = new ModelRenderer(this, 100, 16);
    Shape22.addCuboid(0F, 0F, 0F, 30, 8, 5);
    Shape22.setRotationPoint(-56.6F, -7.1F, -1F);
    Shape22.setTextureSize(256, 128);
    Shape22.mirror = true;
    setRotation(Shape22, 0F, 0F, 0F);
    Shape23 = new ModelRenderer(this, 180, 20);
    Shape23.addCuboid(0F, 0F, 0F, 3, 1, 6);
    Shape23.setRotationPoint(-60.6F, -0.2F, -1.5F);
    Shape23.setTextureSize(256, 128);
    Shape23.mirror = true;
    setRotation(Shape23, 0F, 0F, -1.22173F);
    // Shape24 = new ModelRenderer(this, 0, 115);
    // Shape24.addCuboid(0F, 0F, 0F, 14, 8, 5);
    // Shape24.setRotationPoint(-42F, 6F, -1F);
    // Shape24.setTextureSize(256, 128);
    // Shape24.mirror = true;
    // setRotation(Shape24, 0F, 0F, -0.0523599F);
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
    Shape29 = new ModelRenderer(this, 175, 90);
    Shape29.addCuboid(0F, 0F, 0F, 32, 7, 6);
    Shape29.setRotationPoint(-92.5F, 2.2F, -1.5F);
    Shape29.setTextureSize(256, 128);
    Shape29.mirror = true;
    setRotation(Shape29, 0F, 0F, -0.0698132F);
    Shape30 = new ModelRenderer(this, 90, 100);
    Shape30.addCuboid(0F, 0F, 0F, 33, 6, 6);
    Shape30.setRotationPoint(-93.2F, 8.5F, -1.5F);
    Shape30.setTextureSize(256, 128);
    Shape30.mirror = true;
    setRotation(Shape30, 0F, 0F, -0.2268928F);
    Shape31 = new ModelRenderer(this, 200, 52);
    Shape31.addCuboid(0F, 0F, 0F, 2, 14, 6);
    Shape31.setRotationPoint(-94F, 0.5F, -1.5F);
    Shape31.setTextureSize(256, 128);
    Shape31.mirror = true;
    setRotation(Shape31, 0F, 0F, -0.0349066F);
    Shape32 = new ModelRenderer(this, 210, 0);
    Shape32.addCuboid(0F, 0F, 0F, 16, 2, 6);
    Shape32.setRotationPoint(-92F, 0.5F, -1.5F);
    Shape32.setTextureSize(256, 128);
    Shape32.mirror = true;
    setRotation(Shape32, 0F, 0F, -0.0261799F);
    Shape33 = new ModelRenderer(this, 0, 0);
    Shape33.addCuboid(0F, 0F, 0F, 10, 20, 6);
    Shape33.setRotationPoint(-59F, 5.5F, -1.5F);
    Shape33.setTextureSize(256, 128);
    Shape33.mirror = true;
    setRotation(Shape33, 0F, 0F, 0.0872665F);
    Shape34 = new ModelRenderer(this, 175, 0);
    Shape34.addCuboid(0F, 0F, 0F, 8, 2, 6);
    Shape34.setRotationPoint(-50F, 11.2F, -1.5F);
    Shape34.setTextureSize(256, 128);
    Shape34.mirror = true;
    setRotation(Shape34, 0F, 0F, 0F);
    Shape35 = new ModelRenderer(this, 155, 0);
    Shape35.addCuboid(0F, -1F, 0F, 2, 7, 6);
    Shape35.setRotationPoint(-43.5F, 7.2F, -1.5F);
    Shape35.setTextureSize(256, 128);
    Shape35.mirror = true;
    setRotation(Shape35, 0F, 0F, 0F);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue, float alpha) {
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
    Shape9.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape10.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape11.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape12.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape13.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape14.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape15.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape16.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape17.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape18.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape19.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape20.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape21.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape22.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape23.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    // Shape24.render(matrixStack, vertexBuilder, packedLight, packedOverlay, p_225598_5_,
    // p_225598_6_, p_225598_7_, p_225598_8_);
    // Shape25.render(matrixStack, vertexBuilder, packedLight, packedOverlay, p_225598_5_,
    // p_225598_6_, p_225598_7_, p_225598_8_);
    // Shape26.render(matrixStack, vertexBuilder, packedLight, packedOverlay, p_225598_5_,
    // p_225598_6_, p_225598_7_, p_225598_8_);
    // Shape27.render(matrixStack, vertexBuilder, packedLight, packedOverlay, p_225598_5_,
    // p_225598_6_, p_225598_7_, p_225598_8_);
    // Shape28.render(matrixStack, vertexBuilder, packedLight, packedOverlay, p_225598_5_,
    // p_225598_6_, p_225598_7_, p_225598_8_); AMMO
    Shape29.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape30.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape31.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape32.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape33.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape34.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Shape35.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
  }
}
