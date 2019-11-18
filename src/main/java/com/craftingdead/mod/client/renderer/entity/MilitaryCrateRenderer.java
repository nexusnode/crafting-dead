package com.craftingdead.mod.client.renderer.entity;

import org.lwjgl.opengl.GL11;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.util.RenderUtil;
import com.craftingdead.mod.entity.MilitaryCrateEntity;
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

public class MilitaryCrateRenderer extends EntityRenderer<MilitaryCrateEntity> {


  private IBakedModel modelMilitary;
  private IBakedModel modelParachute;

  public MilitaryCrateRenderer(EntityRendererManager renderManager) {
    super(renderManager);

    try {
      ResourceLocation modelMilitaryLocation =
          new ResourceLocation(CraftingDead.ID, "/models/block/obj/ammobox.obj");
      IUnbakedModel modelMilitaryUnbaked = OBJLoader.INSTANCE.loadModel(modelMilitaryLocation);
      this.modelMilitary = modelMilitaryUnbaked
          .bake(null, ModelLoader.defaultTextureGetter(),
              new BasicState(modelMilitaryUnbaked.getDefaultState(), true),
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
  public void doRender(MilitaryCrateEntity entity, double x, double y, double z, float entityYaw,
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
    GlStateManager.translated(0, 1.0, 0);
    RenderUtil.renderModel(this.modelMilitary, DefaultVertexFormats.BLOCK);
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
  protected ResourceLocation getEntityTexture(MilitaryCrateEntity entity) {
    return new ResourceLocation(CraftingDead.ID, "textures/block/yellow.png");
  }
}
