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

package com.craftingdead.core.client.renderer.entity;

import com.craftingdead.core.client.renderer.entity.model.AdvancedZombieModel;
import com.craftingdead.core.entity.monster.GiantZombieEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class GiantZombieRenderer extends
    AbstractAdvancedZombieRenderer<GiantZombieEntity, AdvancedZombieModel<GiantZombieEntity>> {

  private final float scale;

  public GiantZombieRenderer(EntityRendererManager renderManager) {
    this(renderManager, 6.0F);
  }

  public GiantZombieRenderer(EntityRendererManager renderManager, float scale) {
    super(renderManager, new AdvancedZombieModel<>(0.0F, false), 0.5F * scale);
    this.scale = scale;
  }

  @Override
  protected void scale(GiantZombieEntity entity, MatrixStack matrixStack,
      float partialTicks) {
    matrixStack.scale(this.scale, this.scale, this.scale);
  }
}
