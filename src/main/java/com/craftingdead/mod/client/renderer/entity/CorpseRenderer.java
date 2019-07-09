package com.craftingdead.mod.client.renderer.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.craftingdead.mod.client.renderer.entity.model.CorpseModel;
import com.craftingdead.mod.entity.CorpseEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
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
  public void doRender(CorpseEntity entity, double x, double y, double z, float entityYaw,
      float partialTicks) {
    this.bindEntityTexture(entity);
    GlStateManager.pushMatrix();
    {

      GlStateManager.translated(x, y + 0.2D, z - 0.4D);

      final double scaled = 0.1D;
      GlStateManager.scaled(scaled, scaled, scaled);

      final float scale = 0.625F;
      final int limbCount = entity.getLimbCount();
      this.modelByType.get(this.getSkinType(entity)).render(limbCount, scale);
    }
    GlStateManager.popMatrix();
  }

  private String getSkinType(CorpseEntity entity) {
    NetworkPlayerInfo playerInfo = this.getPlayerInfo(entity);
    return playerInfo == null ? DefaultPlayerSkin.getSkinType(this.getUniqueID(entity))
        : playerInfo.getSkinType();
  }

  @Override
  protected ResourceLocation getEntityTexture(CorpseEntity entity) {
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
