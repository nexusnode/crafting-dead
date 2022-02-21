/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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
