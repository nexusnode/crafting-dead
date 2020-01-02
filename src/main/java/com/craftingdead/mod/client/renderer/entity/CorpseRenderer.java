package com.craftingdead.mod.client.renderer.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.craftingdead.mod.client.renderer.entity.model.CorpseModel;
import com.craftingdead.mod.entity.CorpseEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
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
  public void func_225623_a_(CorpseEntity entity, float entityYaw, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225623_6_) {
    CorpseModel model = this.modelByType.get(this.getSkinType(entity));
    RenderSystem.pushMatrix();
    {

      // RenderSystem.translated(x, y + 0.2D, z - 0.4D);

      final double scaled = 0.1D;
      RenderSystem.scaled(scaled, scaled, scaled);

      final int limbCount = entity.getLimbCount();
      model.setLimbCount(limbCount);

      IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(model.func_228282_a_(this.getEntityTexture(entity)));
      model.func_225598_a_(matrixStack, vertexBuilder, p_225623_6_, 0, 1.0F, 1.0F, 1.0F, 0.15F);
    }
    RenderSystem.popMatrix();
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
