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

package com.craftingdead.immerse.client.renderer.entity.layer;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.client.renderer.entity.layer.AbstractClothingLayer;
import com.craftingdead.core.living.IPlayer;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.IGameClient;
import com.craftingdead.immerse.game.team.ITeam;
import com.craftingdead.immerse.game.team.ITeamGame;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class TeamClothingLayer<T extends LivingEntity, M extends BipedModel<T>>
    extends AbstractClothingLayer<T, M> {

  public TeamClothingLayer(IEntityRenderer<T, M> renderer) {
    super(renderer);
  }

  @Override
  protected ResourceLocation getClothingTexture(LivingEntity livingEntity, String skinType) {
    IGameClient game = CraftingDeadImmerse.getInstance().getClientDist().getGameClient();
    if (game instanceof ITeamGame) {
      // We need to have team as a variable because Java doesn't like it if we use flatMap on
      // ITeam.getSkin because of something to do with generics.
      ITeam team = livingEntity.getCapability(ModCapabilities.LIVING)
          .<IPlayer<?>>cast()
          .resolve()
          .flatMap(((ITeamGame<?>) game)::getPlayerTeam)
          .orElse(null);
      return team == null ? null : team.getSkin().orElse(null);
    }
    return null;
  }
}
