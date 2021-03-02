package com.craftingdead.immerse.mixin;

import java.util.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.IGameServer;
import net.minecraft.block.FireBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

@Mixin(FireBlock.class)
public class FireBlockMixin {

  @Inject(method = "tryCatchFire", at = @At(value = "HEAD"), cancellable = true, remap = false)
  private void tryCatchFire(World worldIn, BlockPos pos, int chance, Random random, int age,
      Direction face, CallbackInfo callbackInfo) {
    if (this.blockBurningDisabled()) {
      callbackInfo.cancel();
    }
  }

  @Inject(method = "canCatchFire", at = @At(value = "HEAD"), cancellable = true, remap = false)
  private void canCatchFire(IBlockReader world, BlockPos pos, Direction face,
      CallbackInfoReturnable<Boolean> callbackInfo) {
    if (this.blockBurningDisabled()) {
      callbackInfo.setReturnValue(false);
    }
  }

  private boolean blockBurningDisabled() {
    IGameServer gameServer = CraftingDeadImmerse.getInstance().getLogicalServer().getGameServer();
    return gameServer != null && gameServer.disableBlockBurning();
  }
}
