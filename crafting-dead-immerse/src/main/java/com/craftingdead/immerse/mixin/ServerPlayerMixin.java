/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

import java.util.Optional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.GameServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

  /**
   * Adds hook for {@link GameServer#getSpawnPoint}.
   */
  @Inject(method = "getRespawnPosition", at = @At("HEAD"), cancellable = true)
  public void getRespawnPosition(CallbackInfoReturnable<BlockPos> callbackInfo) {
    this.getSpawnPoint()
        .map(GlobalPos::pos)
        .ifPresent(callbackInfo::setReturnValue);
  }

  /**
   * Adds hook for {@link GameServer#getSpawnPoint}.
   */
  @Inject(method = "getRespawnDimension", at = @At("HEAD"), cancellable = true)
  public void getRespawnDimension(CallbackInfoReturnable<ResourceKey<Level>> callbackInfo) {
    this.getSpawnPoint()
        .map(GlobalPos::dimension)
        .ifPresent(callbackInfo::setReturnValue);
  }

  /**
   * Adds hook for {@link GameServer#getSpawnPoint}.
   */
  @Inject(method = "isRespawnForced", at = @At("HEAD"), cancellable = true)
  public void isRespawnForced(CallbackInfoReturnable<Boolean> callbackInfo) {
    if (this.getSpawnPoint().isPresent()) {
      callbackInfo.setReturnValue(true);
    }
  }

  private Optional<GlobalPos> getSpawnPoint() {
    var self = (ServerPlayer) (Object) this;
    self.reviveCaps();
    return CraftingDeadImmerse.getInstance().getLogicalServer().getGame()
        .getSpawnPoint(PlayerExtension.getOrThrow(self));
  }
}
