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

package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

  @Inject(at = @At("RETURN"), method = "isMovementBlocked", cancellable = true)
  private void isMovementBlocked(CallbackInfoReturnable<Boolean> callbackInfo) {
    final LivingEntity livingEntity = (LivingEntity) (Object) this;
    ILiving.getOptional(livingEntity).ifPresent(living -> {
      if (!callbackInfo.getReturnValue() && living.isMovementBlocked()) {
        callbackInfo.setReturnValue(true);
      }
    });
  }
}
