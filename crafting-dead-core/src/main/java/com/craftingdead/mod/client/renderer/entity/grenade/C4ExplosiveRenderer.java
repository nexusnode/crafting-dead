package com.craftingdead.mod.client.renderer.entity.grenade;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.renderer.entity.model.C4ExplosiveModel;
import com.craftingdead.mod.entity.grenade.GrenadeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class C4ExplosiveRenderer extends EntityRenderer<GrenadeEntity> {

  private final C4ExplosiveModel model = new C4ExplosiveModel();

  public C4ExplosiveRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void render(GrenadeEntity entity, float entityYaw, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225623_6_) {

    matrixStack.scale(0.4f, 0.4f, 0.4f);
    if (!entity.isStoppedInGround()) {
      float rotation = (entity.ticksExisted + partialTicks) * 15F;
      matrixStack.multiply(Vector3f.POSITIVE_X
          .getDegreesQuaternion(rotation));
      matrixStack.multiply(Vector3f.POSITIVE_Z
          .getDegreesQuaternion(rotation));
    }

    IVertexBuilder vertexBuilder =
        renderTypeBuffer.getBuffer(model.getLayer(this.getEntityTexture(entity)));
    model.render(matrixStack, vertexBuilder, p_225623_6_, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F,
        1.0F, 0.15F);
  }

  @Override
  public ResourceLocation getEntityTexture(GrenadeEntity entity) {
    return new ResourceLocation(CraftingDead.ID,
        "textures/entity/grenade/" + entity.getType().getRegistryName().getPath() + ".png");
  }
}
