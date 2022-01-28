/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
