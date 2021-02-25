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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.living.IPlayer;
import net.minecraft.entity.player.PlayerInventory;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

  @Inject(at = @At("HEAD"), method = "changeCurrentItem", cancellable = true)
  private void changeCurrentItem(double direction, CallbackInfo callbackInfo) {
    if (CraftingDead.getInstance().getClientDist().getPlayer()
        .map(IPlayer::isCombatModeEnabled)
        .orElse(false)) {
      callbackInfo.cancel();

      PlayerInventory inventory = (PlayerInventory) (Object) this;

      if (direction > 0.0D) {
        direction = 1.0D;
      }

      if (direction < 0.0D) {
        direction = -1.0D;
      }

      do {
        inventory.currentItem -= direction;
        if (inventory.currentItem < 0) {
          inventory.currentItem = 6;
        } else if (inventory.currentItem > 6) {
          inventory.currentItem = 0;
        }
      } while (!inventory.isEmpty() && inventory.getCurrentItem().isEmpty());
    }
  }
}
