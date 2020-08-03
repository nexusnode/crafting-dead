package com.craftingdead.core.client.renderer.entity;

import com.craftingdead.core.client.renderer.entity.model.AdvancedZombieModel;
import com.craftingdead.core.entity.monster.GiantZombieEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class GiantZombieRenderer extends
    AbstractAdvancedZombieRenderer<GiantZombieEntity, AdvancedZombieModel<GiantZombieEntity>> {

  private final float scale;

  public GiantZombieRenderer(EntityRendererManager renderManager) {
    this(renderManager, 6.0F);
  }

  public GiantZombieRenderer(EntityRendererManager renderManager, float scale) {
    super(renderManager, new AdvancedZombieModel<>(0.0F, false), 0.5F * scale);
    this.scale = scale;
  }

  @Override
  protected void preRenderCallback(GiantZombieEntity entity, MatrixStack matrixStack,
      float partialTicks) {
    matrixStack.scale(this.scale, this.scale, this.scale);
  }
}
