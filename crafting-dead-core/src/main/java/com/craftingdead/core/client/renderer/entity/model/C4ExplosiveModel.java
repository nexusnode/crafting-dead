package com.craftingdead.core.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class C4ExplosiveModel extends Model {

  private final ModelRenderer shape1;

  public C4ExplosiveModel() {
    super(RenderType::getEntityCutoutNoCull);
    this.textureWidth = 64;
    this.textureHeight = 32;

    this.shape1 = new ModelRenderer(this, 0, 0);
    this.shape1.addCuboid(0F, 0F, 0F, 10, 4, 10);
    this.shape1.setRotationPoint(-5F, 0F, -5F);
    this.shape1.setTextureSize(64, 32);
    this.shape1.mirror = true;
    this.setRotation(this.shape1, 0F, 0F, 0F);
  }

  @Override
  public void render(MatrixStack matrix, IVertexBuilder vertexBuilder, int p_225598_3_,
      int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
    this.shape1.render(matrix, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_,
        p_225598_7_, p_225598_8_);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}

