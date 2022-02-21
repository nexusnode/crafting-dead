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

import java.util.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.GameServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.fml.LogicalSide;

@Mixin(FireBlock.class)
public class FireBlockMixin {

  /**
   * Adds hook for {@link GameServer#disableBlockBurning()}.
   */
  @Inject(method = "tryCatchFire", at = @At(value = "HEAD"), cancellable = true, remap = false)
  private void tryCatchFire(Level level, BlockPos pos, int chance, Random random, int age,
      Direction face, CallbackInfo callbackInfo) {
    if (this.blockBurningDisabled(level)) {
      callbackInfo.cancel();
    }
  }

  /**
   * Adds hook for {@link GameServer#disableBlockBurning()}.
   */
  @Inject(method = "canCatchFire", at = @At(value = "HEAD"), cancellable = true, remap = false)
  private void canCatchFire(BlockGetter blockGetter, BlockPos pos, Direction face,
      CallbackInfoReturnable<Boolean> callbackInfo) {
    if (blockGetter instanceof Level && this.blockBurningDisabled((Level) blockGetter)) {
      callbackInfo.setReturnValue(false);
    }
  }

  private boolean blockBurningDisabled(Level level) {
    var game = CraftingDeadImmerse.getInstance()
        .getGame(level.isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER);
    return game != null && game.disableBlockBurning();
  }
}
