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

import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.SpawnPoint;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

  @Inject(method = "getRespawnPosition", at = @At("HEAD"), cancellable = true)
  public void getRespawnPosition(CallbackInfoReturnable<BlockPos> callbackInfo) {
    SpawnPoint spawnPoint = this.getSpawnPoint();
    if (spawnPoint != null) {
      callbackInfo.setReturnValue(spawnPoint.getBlockPos());
    }
  }

  @Inject(method = "getRespawnDimension", at = @At("HEAD"), cancellable = true)
  public void getRespawnDimension(CallbackInfoReturnable<RegistryKey<World>> callbackInfo) {
    SpawnPoint spawnPoint = this.getSpawnPoint();
    if (spawnPoint != null) {
      callbackInfo.setReturnValue(spawnPoint.getDimension());
    }
  }

  @Inject(method = "isRespawnForced", at = @At("HEAD"), cancellable = true)
  public void isRespawnForced(CallbackInfoReturnable<Boolean> callbackInfo) {
    if (this.getSpawnPoint() != null) {
      callbackInfo.setReturnValue(true);
    }
  }

  @Nullable
  private SpawnPoint getSpawnPoint() {
    return CraftingDeadImmerse.getInstance().getLogicalServer().getGameServer()
        .getSpawnPoint(IPlayer.getExpected((ServerPlayerEntity) (Object) this)).orElse(null);
  }
}
