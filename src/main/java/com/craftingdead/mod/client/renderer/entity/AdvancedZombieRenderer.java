package com.craftingdead.mod.client.renderer.entity;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.entity.monster.AdvancedZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;

public class AdvancedZombieRenderer extends ZombieRenderer {

  public AdvancedZombieRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public ResourceLocation getEntityTexture(ZombieEntity entity) {
    ResourceLocation texture = new ResourceLocation(CraftingDead.ID, "textures/entity/zombie/zombie"
        + ((AdvancedZombieEntity) entity).getTextureNumber() + ".png");
    return texture;
  }
}
