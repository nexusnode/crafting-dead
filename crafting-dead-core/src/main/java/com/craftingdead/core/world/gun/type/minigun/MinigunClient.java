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

package com.craftingdead.core.world.gun.type.minigun;

import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.gun.type.TypedGun;
import com.craftingdead.core.world.gun.type.TypedGunClient;
import net.minecraft.util.math.MathHelper;

public class MinigunClient extends TypedGunClient<TypedGun<?>> {

  private int lastBarrelRotation;
  private int barrelRotation;

  public MinigunClient(TypedGun<?> gun) {
    super(gun);
  }

  @Override
  public void handleTick(LivingExtension<?, ?> living) {
    super.handleTick(living);
    this.lastBarrelRotation = this.barrelRotation;
    if (this.gun.isPerformingSecondaryAction()) {
      this.barrelRotation += 50;
      if (this.barrelRotation >= 360) {
        this.barrelRotation = 0;
      }
    }
  }

  public float getBarrelRotation(float partialTicks) {
    return MathHelper.lerp(partialTicks, this.lastBarrelRotation, this.barrelRotation);
  }
}
