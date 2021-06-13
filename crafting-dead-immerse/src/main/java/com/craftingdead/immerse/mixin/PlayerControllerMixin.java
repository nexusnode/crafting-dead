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

package com.craftingdead.immerse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.multiplayer.PlayerController;

@Mixin(PlayerController.class)
public class PlayerControllerMixin {

  /**
   * Renders HUD for spectating player.
   */
  @Inject(at = @At("RETURN"), method = "canHurtPlayer", cancellable = true)
  private void canHurtPlayer(CallbackInfoReturnable<Boolean> callbackInfo) {
    callbackInfo.setReturnValue(callbackInfo.getReturnValue() ||
        Minecraft.getInstance().getCameraEntity() instanceof RemoteClientPlayerEntity);
  }
}
