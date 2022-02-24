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

package com.craftingdead.immerse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.GameServer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.PlayerDataStorage;

@Mixin(PlayerDataStorage.class)
public class PlayerDataStorageMixin {

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
  private void load(CallbackInfoReturnable<CompoundTag> callbackInfo) {
    if (!CraftingDeadImmerse.getInstance().getLogicalServer().getGame().persistPlayerData()) {
      callbackInfo.setReturnValue(null);
    }
  }
}
