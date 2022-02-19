/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.event;

import javax.annotation.Nullable;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

public class RenderArmClothingEvent extends Event {

  private final AbstractClientPlayer playerEntity;
  @Nullable
  private ResourceLocation clothingTexture;

  public RenderArmClothingEvent(AbstractClientPlayer playerEntity,
      ResourceLocation clothingTexture) {
    this.playerEntity = playerEntity;
    this.clothingTexture = clothingTexture;
  }

  public AbstractClientPlayer getPlayerEntity() {
    return this.playerEntity;
  }

  public ResourceLocation getClothingTexture() {
    return this.clothingTexture;
  }

  public void setClothingTexture(ResourceLocation clothingTexture) {
    this.clothingTexture = clothingTexture;
  }
}
