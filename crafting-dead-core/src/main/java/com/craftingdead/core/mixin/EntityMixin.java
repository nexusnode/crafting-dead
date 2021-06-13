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
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.Visibility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(Entity.class)
public class EntityMixin {

  @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
  private void isInvisible(CallbackInfoReturnable<Boolean> callbackInfo) {
    Entity entity = (Entity) (Object) this;
    entity.getCapability(Capabilities.LIVING)
        .map(LivingExtension::getVisibility)
        .filter(v -> v == Visibility.INVISIBLE || v == Visibility.PARTIALLY_VISIBLE)
        .ifPresent(__ -> callbackInfo.setReturnValue(true));
  }

  @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
  private void isInvisibleTo(PlayerEntity playerEntity,
      CallbackInfoReturnable<Boolean> callbackInfo) {
    Entity entity = (Entity) (Object) this;
    entity.getCapability(Capabilities.LIVING)
        .map(LivingExtension::getVisibility)
        .filter(Visibility.PARTIALLY_VISIBLE::equals)
        .ifPresent(__ -> callbackInfo.setReturnValue(false));
  }
}
