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

import java.util.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.GameServer;
import net.minecraft.block.FireBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

@Mixin(FireBlock.class)
public class FireBlockMixin {

  /**
   * Adds hook for {@link GameServer#disableBlockBurning()}.
   */
  @Inject(method = "tryCatchFire", at = @At(value = "HEAD"), cancellable = true, remap = false)
  private void tryCatchFire(World worldIn, BlockPos pos, int chance, Random random, int age,
      Direction face, CallbackInfo callbackInfo) {
    if (this.blockBurningDisabled()) {
      callbackInfo.cancel();
    }
  }

  /**
   * Adds hook for {@link GameServer#disableBlockBurning()}.
   */
  @Inject(method = "canCatchFire", at = @At(value = "HEAD"), cancellable = true, remap = false)
  private void canCatchFire(IBlockReader world, BlockPos pos, Direction face,
      CallbackInfoReturnable<Boolean> callbackInfo) {
    if (this.blockBurningDisabled()) {
      callbackInfo.setReturnValue(false);
    }
  }

  private boolean blockBurningDisabled() {
    GameServer gameServer = CraftingDeadImmerse.getInstance().getLogicalServer().getGame();
    return gameServer != null && gameServer.disableBlockBurning();
  }
}
