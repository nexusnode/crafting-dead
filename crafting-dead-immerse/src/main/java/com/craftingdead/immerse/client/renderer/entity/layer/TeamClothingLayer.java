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

import com.craftingdead.core.client.renderer.entity.layers.AbstractClothingLayer;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.ClientGameWrapper;
import com.craftingdead.immerse.game.module.ModuleTypes;
import com.craftingdead.immerse.game.module.team.ClientTeamModule;
import com.craftingdead.immerse.game.module.team.Team;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;

public class TeamClothingLayer<T extends LivingEntity, M extends HumanoidModel<T>>
    extends AbstractClothingLayer<T, M> {

  public TeamClothingLayer(RenderLayerParent<T, M> renderer) {
    super(renderer);
  }

  @Override
  protected ResourceLocation getClothingTexture(LivingEntity livingEntity, String skinType) {
    ClientGameWrapper gameWrapper =
        CraftingDeadImmerse.getInstance().getClientDist().getGameWrapper();
    if (gameWrapper != null) {
      ClientTeamModule<?> module =
          (ClientTeamModule<?>) gameWrapper.getModule(ModuleTypes.TEAM.get());
      if (module != null) {
        // We need to have team as a variable because the compiler doesn't like it if we use flatMap
        // on Team.getSkin because of something to do with generics.
        Team team = module.getPlayerTeam(livingEntity.getUUID()).orElse(null);
        return team == null ? null : team.getSkin().orElse(null);
      }
    }
    return null;
  }
}
