package com.craftingdead.mod.client.renderer.entity;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.entity.SupplyDropEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;

public class SupplyDropRenderer extends EntityRenderer<SupplyDropEntity> {

  private final IBakedModel crateModel;
  private final IBakedModel parachuteModel;

  public SupplyDropRenderer(EntityRendererManager renderManager) {
    super(renderManager);
    this.crateModel = Minecraft
        .getInstance()
        .getModelManager()
        .getModel(new ResourceLocation(CraftingDead.ID, "models/block/obj/supplybox.obj"));
    this.parachuteModel = Minecraft
        .getInstance()
        .getModelManager()
        .getModel(new ResourceLocation(CraftingDead.ID, "models/block/obj/parachute.obj"));
  }

  @Override
  public void func_225623_a_(SupplyDropEntity entity, float entityYaw, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225623_6_) {


    RenderSystem.pushMatrix();

    // RenderSystem.translated(x, y, z);
    RenderSystem.rotatef(180.0F - entityYaw, 180.0F, 1.0F, 0.0F);

    float f4 = 0.75F;
    RenderSystem.scalef(f4, f4, f4);
    RenderSystem.scalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
    RenderType.func_228640_c_(this.getEntityTexture(entity));

    RenderSystem.scalef(-1.0F, -1.0F, 1.0F);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    RenderSystem.pushMatrix();

    RenderSystem.translated(0, 0.52D, 0);
    // RenderUtil.renderModel(this.crateModel, DefaultVertexFormats.BLOCK);

    RenderSystem.popMatrix();

    if (!entity.onGround) {
      RenderSystem.pushMatrix();

      RenderSystem.translated(-0.5, .65, .5);
      // RenderUtil.renderModel(this.parachuteModel, DefaultVertexFormats.BLOCK);

      RenderSystem.popMatrix();
    }


    RenderSystem.popMatrix();
  }

  @Override
  public ResourceLocation getEntityTexture(SupplyDropEntity entity) {
    return new ResourceLocation(CraftingDead.ID, "models/block/obj/yellow.png");
  }
}
