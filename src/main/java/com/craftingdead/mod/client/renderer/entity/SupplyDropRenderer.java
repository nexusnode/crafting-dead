package com.craftingdead.mod.client.renderer.entity;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.renderer.entity.model.SupplyDropModel;
import com.craftingdead.mod.entity.SupplyDropEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class SupplyDropRenderer extends EntityRenderer<SupplyDropEntity> {

  private final SupplyDropModel model = new SupplyDropModel();

  public SupplyDropRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void func_225623_a_(SupplyDropEntity entity, float entityYaw, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225623_6_) {

    matrixStack.func_227861_a_(0, 1.51D, 0);
    matrixStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(180.0F));
    
    this.model.setRenderParachute(entity.fallDistance > 0 && !entity.onGround);

    IVertexBuilder vertexBuilder =
        renderTypeBuffer.getBuffer(this.model.func_228282_a_(this.getEntityTexture(entity)));
    this.model
        .func_225598_a_(matrixStack, vertexBuilder, p_225623_6_, OverlayTexture.field_229196_a_,
            1.0F, 1.0F, 1.0F, 0.15F);
  }

  @Override
  public ResourceLocation getEntityTexture(SupplyDropEntity entity) {
    return new ResourceLocation(CraftingDead.ID, "textures/entity/supply_drop.png");
  }
}
