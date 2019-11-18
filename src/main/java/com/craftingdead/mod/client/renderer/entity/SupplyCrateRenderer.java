package com.craftingdead.mod.client.renderer.entity;

import org.lwjgl.opengl.GL11;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.util.RenderUtil;
import com.craftingdead.mod.entity.SupplyCrateEntity;
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

public class SupplyCrateRenderer extends EntityRenderer<SupplyCrateEntity> {

  private IBakedModel modelSupply;
  private IBakedModel modelParachute;

  public SupplyCrateRenderer(EntityRendererManager renderManager) {
    super(renderManager);
    try {
      ResourceLocation modelSupplyLocation =
          new ResourceLocation(CraftingDead.ID, "/models/block/obj/supplybox.obj");
      IUnbakedModel modelSupplyUnbaked = OBJLoader.INSTANCE.loadModel(modelSupplyLocation);
      this.modelSupply = modelSupplyUnbaked
          .bake(null, ModelLoader.defaultTextureGetter(),
              new BasicState(modelSupplyUnbaked.getDefaultState(), true),
              DefaultVertexFormats.BLOCK);

      ResourceLocation modelParachuteLocation =
          new ResourceLocation(CraftingDead.ID, "/models/block/obj/parachute.obj");
      IUnbakedModel modelParachuteUnbaked = OBJLoader.INSTANCE.loadModel(modelParachuteLocation);
      this.modelParachute = modelParachuteUnbaked
          .bake(null, ModelLoader.defaultTextureGetter(),
              new BasicState(modelParachuteUnbaked.getDefaultState(), true),
              DefaultVertexFormats.BLOCK);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void doRender(SupplyCrateEntity entity, double x, double y, double z, float entityYaw,
      float partialTicks) {

    GL11.glPushMatrix();
    GL11.glTranslated(x, y, z);
    GL11.glRotatef(180.0F - entityYaw, 180.0F, 1.0F, 0.0F);

    float f4 = 0.75F;
    GL11.glScalef(f4, f4, f4);
    GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
    this.bindEntityTexture(entity);

    GL11.glScalef(-1.0F, -1.0F, 1.0F);
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    GlStateManager.pushMatrix();
    GlStateManager.translated(0, 0.52D, 0);
    RenderUtil.renderModel(this.modelSupply, DefaultVertexFormats.BLOCK);
    GlStateManager.popMatrix();

    GlStateManager.pushMatrix();
    if (!entity.onGround) {
      GlStateManager.translated(-0.5, .65, .5);
      RenderUtil.renderModel(this.modelParachute, DefaultVertexFormats.BLOCK);
    }
    GlStateManager.popMatrix();

    GL11.glPopMatrix();
  }

  @Override
  protected ResourceLocation getEntityTexture(SupplyCrateEntity entity) {
    return new ResourceLocation(CraftingDead.ID, "models/block/obj/yellow.png");
  }

}
