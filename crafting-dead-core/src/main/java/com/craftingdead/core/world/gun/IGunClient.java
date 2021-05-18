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

package com.craftingdead.core.world.gun;

import com.craftingdead.core.client.animation.gun.GunAnimationController;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public interface IGunClient {

  void handleTick(LivingExtension<?, ?> living);

  void handleShoot(LivingExtension<?, ?> living);

  void handleHitEntityPre(LivingExtension<?, ?> living, Entity hitEntity,
      Vector3d hitPos, long randomSeed);

  void handleHitEntityPost(LivingExtension<?, ?> living, Entity hitEntity,
      Vector3d hitPos, boolean playSound, boolean headshot);

  void handleHitBlock(LivingExtension<?, ?> living, BlockRayTraceResult rayTrace,
      boolean playSound);

  void handleToggleRightMouseAction(LivingExtension<?, ?> living);

  boolean isFlashing();

  GunAnimationController getAnimationController();
}
