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
import com.craftingdead.immerse.game.GameServer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.PlayerData;

@Mixin(PlayerData.class)
public class PlayerDataMixin {

  /**
   * Adds hook for {@link GameServer#persistPlayerData}.
   */
  @Inject(method = "save", at = @At("HEAD"), cancellable = true)
  private void save(CallbackInfo callbackInfo) {
    if (!CraftingDeadImmerse.getInstance().getLogicalServer().getGame().persistPlayerData()) {
      callbackInfo.cancel();
    }
  }

  /**
   * Adds hook for {@link GameServer#persistPlayerData}.
   */
  @Inject(method = "load", at = @At("HEAD"), cancellable = true)
  private void load(CallbackInfoReturnable<CompoundNBT> callbackInfo) {
    if (!CraftingDeadImmerse.getInstance().getLogicalServer().getGame().persistPlayerData()) {
      callbackInfo.setReturnValue(null);
    }
  }
}
