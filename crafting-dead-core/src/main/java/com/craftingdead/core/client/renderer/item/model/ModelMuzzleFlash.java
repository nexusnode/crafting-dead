package com.craftingdead.core.client.renderer.item.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class ModelMuzzleFlash extends Model {
  // fields
  ModelRenderer Box_0;
  ModelRenderer Box_1;
  ModelRenderer Box_3;

  public ModelMuzzleFlash() {
    super(ModelMuzzleFlash::getFlashRenderType);
    textureWidth = 64;
    textureHeight = 64;

    Box_0 = new ModelRenderer(this, 1, 1);
    Box_0.addCuboid(0F, 0F, 0F, 8, 8, 0);
    Box_0.setRotationPoint(-4F, -4F, 0F);
    Box_0.setTextureSize(64, 32);
    Box_0.mirror = true;
    setRotation(Box_0, 0F, 0F, 0F);
    Box_1 = new ModelRenderer(this, 9, 1);
    Box_1.addCuboid(-4F, 0F, 0F, 8, 0, 15);
    Box_1.setRotationPoint(0F, 0F, -15F);
    Box_1.setTextureSize(64, 32);
    Box_1.mirror = true;
    setRotation(Box_1, 0F, 0F, -0.7853982F);
    Box_3 = new ModelRenderer(this, 1, 17);
    Box_3.addCuboid(-4F, 0F, 0F, 8, 0, 15);
    Box_3.setRotationPoint(0F, 0F, -15F);
    Box_3.setTextureSize(64, 32);
    Box_3.mirror = true;
    setRotation(Box_3, 0F, 0F, -2.373648F);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue,
      float alpha) {
    Box_0.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Box_1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Box_3.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
  }

  private static RenderType getFlashRenderType(ResourceLocation texture) {
    return RenderType.of("flash",
        DefaultVertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true,
        true, RenderType.State.builder()
            .texture(new RenderState.TextureState(texture,
                false, false))
            .transparency(new RenderState.TransparencyState("translucent_transparency", () -> {
              RenderSystem.enableBlend();
              RenderSystem.defaultBlendFunc();
            }, () -> {
              RenderSystem.disableBlend();
            }))
            .build(true));
  }
}
