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

package com.craftingdead.survival.client.renderer.entity;

import com.craftingdead.survival.client.model.AdvancedZombieModel;
import com.craftingdead.survival.world.entity.monster.AdvancedZombie;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AdvancedZombieRenderer extends
    AbstractAdvancedZombieRenderer<AdvancedZombie, AdvancedZombieModel<AdvancedZombie>> {

  public AdvancedZombieRenderer(EntityRendererProvider.Context context) {
    super(context, new AdvancedZombieModel<>(
        context.bakeLayer(ModelLayers.ZOMBIE),
        context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
  }
}
