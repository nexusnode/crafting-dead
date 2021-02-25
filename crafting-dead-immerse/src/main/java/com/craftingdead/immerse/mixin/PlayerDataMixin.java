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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.PlayerData;

@Mixin(PlayerData.class)
public class PlayerDataMixin {

  @Inject(method = "savePlayerData", at = @At("HEAD"), cancellable = true)
  private void savePlayerData(CallbackInfo callbackInfo) {
    if (!CraftingDeadImmerse.getInstance().getLogicalServer().getGameServer().persistPlayerData()) {
      callbackInfo.cancel();
    }
  }

  @Inject(method = "loadPlayerData", at = @At("HEAD"), cancellable = true)
  private void loadPlayerData(CallbackInfoReturnable<CompoundNBT> callbackInfo) {
    if (!CraftingDeadImmerse.getInstance().getLogicalServer().getGameServer().persistPlayerData()) {
      callbackInfo.setReturnValue(null);
    }
  }
}
