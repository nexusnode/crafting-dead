package com.craftingdead.core.event;

import javax.annotation.Nullable;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

public class RenderArmClothingEvent extends Event {

  private final AbstractClientPlayerEntity playerEntity;
  @Nullable
  private ResourceLocation clothingTexture;

  public RenderArmClothingEvent(AbstractClientPlayerEntity playerEntity,
      ResourceLocation clothingTexture) {
    this.playerEntity = playerEntity;
    this.clothingTexture = clothingTexture;
  }

  public AbstractClientPlayerEntity getPlayerEntity() {
    return this.playerEntity;
  }

  public ResourceLocation getClothingTexture() {
    return this.clothingTexture;
  }

  public void setClothingTexture(ResourceLocation clothingTexture) {
    this.clothingTexture = clothingTexture;
  }
}
