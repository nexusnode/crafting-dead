package com.craftingdead.core.client.renderer.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.craftingdead.core.client.renderer.entity.model.CorpseModel;
import com.craftingdead.core.entity.CorpseEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class CorpseRenderer extends EntityRenderer<CorpseEntity> {

  private final Map<String, CorpseModel> modelByType = new HashMap<>();

  public CorpseRenderer(EntityRendererManager renderManager) {
    super(renderManager);
    this.modelByType.put("default", new CorpseModel(false));
    this.modelByType.put("slim", new CorpseModel(true));
  }

  @Override
  public void render(CorpseEntity entity, float entityYaw, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225623_6_) {
    CorpseModel model = this.modelByType.get(this.getSkinType(entity));

    matrixStack.translate(0, 0.2D, -0.4D);
    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));

    final int limbCount = entity.getLimbCount();
    model.setLimbCount(limbCount);

    IVertexBuilder vertexBuilder =
        renderTypeBuffer.getBuffer(model.getLayer(this.getEntityTexture(entity)));
    model
        .render(matrixStack, vertexBuilder, p_225623_6_, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F,
            1.0F, 0.15F);
  }

  private String getSkinType(CorpseEntity entity) {
    NetworkPlayerInfo playerInfo = this.getPlayerInfo(entity);
    return playerInfo == null ? DefaultPlayerSkin.getSkinType(this.getUniqueID(entity))
        : playerInfo.getSkinType();
  }

  @Override
  public ResourceLocation getEntityTexture(CorpseEntity entity) {
    NetworkPlayerInfo playerInfo = this.getPlayerInfo(entity);
    return playerInfo == null ? DefaultPlayerSkin.getDefaultSkin(this.getUniqueID(entity))
        : playerInfo.getLocationSkin();
  }

  private NetworkPlayerInfo getPlayerInfo(CorpseEntity entity) {
    return Minecraft.getInstance().getConnection().getPlayerInfo(this.getUniqueID(entity));
  }

  private UUID getUniqueID(CorpseEntity entity) {
    return entity.getDeceasedId().orElse(entity.getUniqueID());
  }
}
