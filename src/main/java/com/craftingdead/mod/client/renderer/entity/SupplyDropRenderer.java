package com.craftingdead.mod.client.renderer.entity;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.util.RenderUtil;
import com.craftingdead.mod.entity.SupplyDropEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;

public class SupplyDropRenderer extends EntityRenderer<SupplyDropEntity> {

  private IBakedModel crateModel;
  private IBakedModel parachuteModel;

  public SupplyDropRenderer(EntityRendererManager renderManager) {
    super(renderManager);
    try {
      ResourceLocation modelSupplyLocation =
          new ResourceLocation(CraftingDead.ID, "/models/block/obj/supplybox.obj");
      IUnbakedModel modelSupplyUnbaked = OBJLoader.INSTANCE.loadModel(modelSupplyLocation);
      this.crateModel = modelSupplyUnbaked
          .bake(null, ModelLoader.defaultTextureGetter(),
              new BasicState(modelSupplyUnbaked.getDefaultState(), true),
              DefaultVertexFormats.BLOCK);

      ResourceLocation modelParachuteLocation =
          new ResourceLocation(CraftingDead.ID, "/models/block/obj/parachute.obj");
      IUnbakedModel modelParachuteUnbaked = OBJLoader.INSTANCE.loadModel(modelParachuteLocation);
      this.parachuteModel = modelParachuteUnbaked
          .bake(null, ModelLoader.defaultTextureGetter(),
              new BasicState(modelParachuteUnbaked.getDefaultState(), true),
              DefaultVertexFormats.BLOCK);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void doRender(SupplyDropEntity entity, double x, double y, double z, float entityYaw,
      float partialTicks) {

    GlStateManager.pushMatrix();

    GlStateManager.translated(x, y, z);
    GlStateManager.rotatef(180.0F - entityYaw, 180.0F, 1.0F, 0.0F);

    float f4 = 0.75F;
    GlStateManager.scalef(f4, f4, f4);
    GlStateManager.scalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
    this.bindEntityTexture(entity);

    GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    GlStateManager.pushMatrix();

    GlStateManager.translated(0, 0.52D, 0);
    RenderUtil.renderModel(this.crateModel, DefaultVertexFormats.BLOCK);

    GlStateManager.popMatrix();

    if (!entity.onGround) {
      GlStateManager.pushMatrix();

      GlStateManager.translated(-0.5, .65, .5);
      RenderUtil.renderModel(this.parachuteModel, DefaultVertexFormats.BLOCK);

      GlStateManager.popMatrix();
    }


    GlStateManager.popMatrix();
  }

  @Override
  protected ResourceLocation getEntityTexture(SupplyDropEntity entity) {
    return new ResourceLocation(CraftingDead.ID, "models/block/obj/yellow.png");
  }
}
