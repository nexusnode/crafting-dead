/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import net.minecraft.world.entity.player.Inventory;

@Mixin(Inventory.class)
public class InventoryMixin {

  /**
   * Scrolls over empty slots when in combat mode.
   */
  @Inject(at = @At("HEAD"), method = "swapPaint", cancellable = true)
  private void swapPaint(double direction, CallbackInfo callbackInfo) {
    if (CraftingDead.getInstance().getClientDist().getPlayerExtension()
        .map(PlayerExtension::isCombatModeEnabled)
        .orElse(false)) {
      callbackInfo.cancel();

      Inventory inventory = (Inventory) (Object) this;

      if (direction > 0.0D) {
        direction = 1.0D;
      }

      if (direction < 0.0D) {
        direction = -1.0D;
      }

      do {
        inventory.selected -= direction;
        if (inventory.selected < 0) {
          inventory.selected = 6;
        } else if (inventory.selected > 6) {
          inventory.selected = 0;
        }
      } while (!inventory.isEmpty() && inventory.getSelected().isEmpty());
    }
  }
}
