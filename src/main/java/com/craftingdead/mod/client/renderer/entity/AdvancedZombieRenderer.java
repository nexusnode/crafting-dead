package com.craftingdead.mod.client.renderer.entity;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.entity.monster.AdvancedZombieEntity;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;

public class AdvancedZombieRenderer
    extends AbstractZombieRenderer<AdvancedZombieEntity, ZombieModel<AdvancedZombieEntity>> {

  public AdvancedZombieRenderer(EntityRendererManager renderManager) {
    super(renderManager, new ZombieModel<>(), new ZombieModel<>(0.5F, true),
        new ZombieModel<>(1.0F, true));
  }

  @Override
  protected ResourceLocation getEntityTexture(ZombieEntity entity) {
    ResourceLocation texture = new ResourceLocation(CraftingDead.ID, "textures/entity/zombie/zombie"
        + ((AdvancedZombieEntity) entity).getTextureNumber() + ".png");
    return texture;
  }
}
