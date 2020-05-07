package com.craftingdead.mod.client.renderer.entity;

import com.craftingdead.mod.client.renderer.entity.model.AdvancedZombieModel;
import com.craftingdead.mod.entity.monster.AdvancedZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class AdvancedZombieRenderer extends
    AbstractAdvancedZombieRenderer<AdvancedZombieEntity, AdvancedZombieModel<AdvancedZombieEntity>> {

  public AdvancedZombieRenderer(EntityRendererManager renderManager) {
    super(renderManager, new AdvancedZombieModel<>(0.0F, false), 0.5F);
  }
}
