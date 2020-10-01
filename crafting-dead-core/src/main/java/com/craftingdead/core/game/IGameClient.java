/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core.game;

import com.craftingdead.core.capability.living.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;

public interface IGameClient<T extends ITeam, P extends Player<? extends AbstractClientPlayerEntity>>
    extends IGame<T, P, AbstractClientPlayerEntity> {

  default void renderHud(Minecraft minecraft, ClientPlayerEntity playerEntity, int width,
      int height, float partialTicks) {
    this.renderHud(minecraft, this.getPlayer(playerEntity), width, height, partialTicks);
  }

  void renderHud(Minecraft minecraft, P player, int width, int height, float partialTicks);
}
