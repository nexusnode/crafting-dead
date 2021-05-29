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
import com.craftingdead.survival.world.entity.monster.AdvancedZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class AdvancedZombieRenderer extends
    AbstractAdvancedZombieRenderer<AdvancedZombieEntity, AdvancedZombieModel<AdvancedZombieEntity>> {

  public AdvancedZombieRenderer(EntityRendererManager renderManager) {
    super(renderManager, new AdvancedZombieModel<>(0.0F, false), 0.5F);
  }
}
